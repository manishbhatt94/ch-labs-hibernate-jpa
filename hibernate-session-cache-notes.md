# Hibernate: Session, Cache, StatelessSession & Querying — Self Notes

> Hibernate 5.6.5.Final · Java 8  
> References: [Hibernate ORM 5.6 User Guide](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html)

---

## 1. The Big Picture First

There are two ways to talk to Hibernate:

| What you use | Where it comes from | Mental model |
|---|---|---|
| `Session` | Hibernate's own API (`org.hibernate.Session`) | Hibernate-native, more features |
| `EntityManager` | JPA standard (`javax.persistence.EntityManager`) | The "official standard" wrapper |

**They are actually the same thing underneath.** Hibernate's `Session` *implements* JPA's `EntityManager`. So everything explained here about `Session` applies to `EntityManager` too — same cache, same rules. `EntityManager` just exposes a smaller, standardised API.

```java
// These two are essentially the same object at runtime:
Session session = sessionFactory.openSession();
EntityManager em = entityManagerFactory.createEntityManager(); // wraps a Session internally
```

---

## 2. First-Level Cache (a.k.a. the "Session Cache")

### What is it?

Every `Session` has a built-in **identity map** — a `Map<EntityType, Map<Id, Object>>` held in memory. When you load an entity, Hibernate stores it here. If you load *the same row again in the same Session*, Hibernate returns the cached object instead of hitting the database.

```java
Session session = sessionFactory.openSession();

User u1 = session.get(User.class, 1L); // SELECT hits DB
User u2 = session.get(User.class, 1L); // returned from cache — NO DB hit

System.out.println(u1 == u2); // true — literally the same object
```

### Key rules

- **Scope:** one `Session` = one cache. Closed session = cache gone.
- **Automatic:** you cannot turn it off. It's always on.
- **Dirty checking:** Hibernate watches every entity in this cache. When the transaction commits, it automatically runs `UPDATE` for any entity whose fields changed. This is called *automatic dirty checking*.

```java
session.beginTransaction();
User user = session.get(User.class, 1L); // loaded into cache
user.setName("Alice");                   // just a Java setter, no explicit save()
session.getTransaction().commit();       // Hibernate auto-detects the change → runs UPDATE
```

### The memory problem with bulk operations

If you load or save 100,000 rows in one Session, all 100,000 objects sit in this cache. You can run out of heap memory.

Fix: periodically flush (sync to DB) and clear (wipe cache) manually.

```java
session.beginTransaction();
for (int i = 0; i < 100_000; i++) {
    session.save(new Product("item-" + i));
    if (i % 50 == 0) {
        session.flush(); // execute pending SQL
        session.clear(); // empty the cache
    }
}
session.getTransaction().commit();
```

> Ref: [Hibernate User Guide § Persistence Context](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#pc)

---

## 3. StatelessSession — Cache-Free Mode

`StatelessSession` is a stripped-down alternative to `Session`. It has **no first-level cache** and **no dirty checking**.

```java
StatelessSession ss = sessionFactory.openStatelessSession();
Transaction tx = ss.beginTransaction();

// You must call explicit methods — no auto dirty-checking
ss.insert(new Product("widget"));
ss.update(existingProduct);
ss.delete(existingProduct);

tx.commit();
ss.close();
```

### When to use it

| Situation | Use |
|---|---|
| Loading one or a few rows, then updating them | `Session` — dirty checking saves boilerplate |
| Inserting / updating / deleting millions of rows | `StatelessSession` — no memory bloat |
| You need cascades, lazy loading, events/interceptors | `Session` — `StatelessSession` doesn't support these |

### What StatelessSession gives up

- No cascade operations (saving a parent won't auto-save children)
- No lazy loading of associations
- No Hibernate event system / interceptors
- No second-level cache interaction

> Ref: [Hibernate User Guide § StatelessSession](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#stateless-sessions)

---

## 4. Querying: HQL/JPQL, Criteria, Native SQL

These are three ways to query. Cache behaviour differs between them.

### 4a. HQL / JPQL (`SELECT`, `UPDATE`, `DELETE`)

HQL (Hibernate Query Language) and JPQL (its JPA equivalent) are SQL-like but operate on **entity class names**, not table names.

```java
// SELECT — loaded entities DO go into the first-level cache
List<User> users = session.createQuery("FROM User WHERE active = true", User.class)
                          .getResultList();

// Bulk UPDATE — bypasses the cache entirely, goes straight to DB
session.createQuery("UPDATE User SET active = false WHERE lastLogin < :date")
       .setParameter("date", someDate)
       .executeUpdate();
// ⚠️ Any User objects already in your Session cache are now STALE after this
```

### 4b. Criteria API (`SELECT`, `UPDATE`, `DELETE`)

A type-safe, programmatic way to build queries — same cache rules as HQL.

```java
CriteriaBuilder cb = session.getCriteriaBuilder();
CriteriaQuery<User> cq = cb.createQuery(User.class);
Root<User> root = cq.from(User.class);
cq.select(root).where(cb.equal(root.get("active"), true));

List<User> users = session.createQuery(cq).getResultList(); // entities cached in session
```

### 4c. Native SQL (`SELECT`, `INSERT`, `UPDATE`, `DELETE`)

Raw SQL. Hibernate doesn't parse it, so it has **no awareness of your entity cache**.

```java
// SELECT with entity mapping — entities go into cache
List<User> users = session.createNativeQuery("SELECT * FROM users WHERE active = 1", User.class)
                          .getResultList();

// Bulk UPDATE via native SQL — cache is completely bypassed
session.createNativeQuery("UPDATE users SET active = 0 WHERE last_login < '2023-01-01'")
       .executeUpdate();
// Same staleness problem as HQL bulk updates
```

### Summary: does the cache get involved?

| Operation type | Cache involved? |
|---|---|
| `session.get()` / `session.find()` | ✅ Yes — reads from & writes to cache |
| HQL/JPQL `SELECT` | ✅ Yes — results loaded into cache |
| Criteria `SELECT` | ✅ Yes — same as HQL |
| Native SQL `SELECT` (mapped to entity) | ✅ Yes |
| HQL/JPQL/Native bulk `UPDATE` / `DELETE` | ❌ No — goes straight to DB, cache becomes stale |
| HQL/JPQL/Native `INSERT` | ❌ No |

> Ref: [Hibernate User Guide § HQL](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#hql), [§ Criteria](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#criteria), [§ Native SQL](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#sql)

---

## 5. Does EntityManager Have All This Too?

Yes. The JPA `EntityManager` has the exact same first-level cache (JPA calls it the **Persistence Context**). The method names are just different:

| Hibernate `Session` | JPA `EntityManager` | What it does |
|---|---|---|
| `session.get(User.class, 1L)` | `em.find(User.class, 1L)` | Load by primary key (uses cache) |
| `session.save(entity)` | `em.persist(entity)` | Insert |
| `session.update(entity)` | `em.merge(entity)` | Update a detached entity |
| `session.delete(entity)` | `em.remove(entity)` | Delete |
| `session.flush()` | `em.flush()` | Sync cache to DB |
| `session.clear()` | `em.clear()` | Wipe the cache |
| `session.contains(entity)` | `em.contains(entity)` | Is this object in the cache? |

JPA has **no `StatelessSession` equivalent**. If you need that, you must cast to Hibernate's native API:

```java
Session session = em.unwrap(Session.class); // get the Hibernate Session from EntityManager
StatelessSession ss = sessionFactory.openStatelessSession(); // or open one directly
```

> Ref: [Hibernate User Guide § JPA EntityManager](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#_bootstrap_jpa_way), [JPA 2.2 Spec § 3.1 — Persistence Contexts](https://jakarta.ee/specifications/persistence/2.2/)

---

## 6. One-Page Cheatsheet

```
SINGLE ROW WORK
  → Use Session / EntityManager
  → Dirty checking = free UPDATEs on commit
  → First-level cache = no duplicate SELECTs

BULK OPERATIONS (thousands of rows)
  Option A: Session + flush/clear every N rows   (simple, some overhead)
  Option B: StatelessSession                      (fastest, but no cascades/lazy load)
  Option C: HQL/JPQL bulk UPDATE/DELETE           (one SQL, cache bypass — call clear() after)
  Option D: Native SQL                            (same as C, even more direct)

CACHE STALENESS WARNING
  After any bulk UPDATE/DELETE (HQL or Native), call session.clear() or em.clear()
  to avoid working with outdated objects.
```

---

## 7. Managing the First-Level Cache

You have three tools: `flush()`, `clear()`, and `evict()`. They do very different things.

**`flush()`** — tells Hibernate "execute all pending SQL right now" but keeps every object in the cache. Dirty checking still watches them after a flush.

**`clear()`** — empties the entire cache. Every object becomes detached (Hibernate forgets about them all). Use after bulk operations to prevent memory bloat.

**`evict(entityObj)`** — removes *one specific object* from the cache. Everything else stays. Useful when you know one particular object is now stale but don't want to throw away the rest of the cache.

```java
// --- Session API ---

User user = session.get(User.class, 1L);  // user is now in cache

session.flush();           // runs pending SQL, user still in cache
session.evict(user);       // removes just this user from cache
session.clear();           // removes everything from cache

// Checking if an object is currently in the cache:
boolean isManaged = session.contains(user); // false after evict/clear
```

```java
// --- EntityManager API (same concepts, different method names) ---

User user = em.find(User.class, 1L);  // user is now in persistence context

em.flush();                // runs pending SQL, user still tracked
em.detach(user);           // JPA's evict() — removes just this one object
em.clear();                // removes everything

boolean isManaged = em.contains(user); // false after detach/clear
```

The only difference worth noting: Hibernate calls it `evict(obj)`, JPA calls it `detach(obj)`. They do the same thing — the object moves from *Persistent* state to *Detached* state (see Section 8).

A common pattern when doing a large read-then-process loop with `Session`:

```java
List<User> users = session.createQuery("FROM User", User.class).getResultList();
for (User u : users) {
    process(u);
    session.evict(u); // free memory as you go, one object at a time
}
```

If you don't need any of the objects after processing, `clear()` after every N iterations is simpler. Use `evict()` when you want fine-grained control — e.g. you're holding some objects in the cache intentionally while discarding others.

> Ref: [Hibernate User Guide § Flushing](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#flushing), [`Session.evict()` Javadoc](https://docs.jboss.org/hibernate/orm/5.6/javadocs/org/hibernate/Session.html#evict-java.lang.Object-), [`EntityManager.detach()` Javadoc](https://docs.oracle.com/javaee/7/api/javax/persistence/EntityManager.html#detach-java.lang.Object-)

---

## 8. Entity States (Quick Reference — Needed to Understand Cache)

Every entity object you work with is always in one of four states from Hibernate's point of view. The state determines whether Hibernate is watching the object, and whether it exists in the DB.

**Transient** — you created the object with `new` but haven't told Hibernate about it yet. It's just a plain Java object. Not in the cache, not in the DB.

```java
User user = new User("Bob"); // transient — Hibernate has no idea this exists
```

**Persistent** — Hibernate knows about this object. It's in the first-level cache and Hibernate is watching it for changes (dirty checking). This happens after you call `save()`, `get()`, `find()`, or get it back from a query.

```java
session.save(user);                        // was transient, now persistent
User u = session.get(User.class, 1L);      // loaded from DB, immediately persistent
// any change to u's fields will be auto-UPDATE'd on commit
```

**Detached** — the object still exists in your Java code but the Session that was tracking it is now gone (closed, or you called `evict()`/`clear()`). Hibernate is no longer watching it — changes to its fields won't be saved automatically. To start tracking it again, you re-attach it via `session.update(obj)` or `em.merge(obj)`.

```java
session.close();          // cache is gone — user object is now detached
user.setName("Alice");    // this change will NOT be saved anywhere

// To save it, open a new session and merge:
Session newSession = sessionFactory.openSession();
newSession.update(user);  // re-attaches; the name change will now be saved on commit
```

**Removed** — you called `delete()`/`remove()` on it. The object is still in the cache momentarily, but it's marked for deletion. The actual `DELETE` SQL runs on the next flush/commit.

```java
session.delete(user); // marked for removal — still in cache until flush
session.flush();      // DELETE SQL runs here
```

A rough flow through the states:

```
new User()          →  [Transient]
session.save(user)  →  [Persistent]  ← dirty checking active
session.evict(user) →  [Detached]    ← Hibernate stops watching
session.update(user)→  [Persistent]  ← back to watching
session.delete(user)→  [Removed]     → deleted on flush
```

> Ref: [Hibernate User Guide § Entity States](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#pc-cascade-detach)
