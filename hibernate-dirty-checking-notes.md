# Hibernate — Dirty Checking
> Notes · Hibernate 5.6.5.Final · Java 8  
> Ref: [Hibernate 5.6 User Guide — Flushing](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#flushing)

---

## The core idea

When you load an entity (a Java object mapped to a DB row), Hibernate quietly takes a **snapshot** — a copy of all its field values at that moment.

Later, when the transaction commits, Hibernate walks every managed entity and **compares its current state to that snapshot**. If anything changed, it generates and runs an UPDATE. This automatic detection of changes is called **dirty checking**. An entity with changes is called "dirty".

You never call `update()`. Hibernate just figures it out.

```java
session.beginTransaction();

Employee emp = session.get(Employee.class, 1L);
// ^ Hibernate loads the row AND stores a snapshot: { name="Alice", salary=80000 }

emp.setSalary(95_000);
// ^ emp is now "dirty" — current state differs from snapshot

session.getTransaction().commit();
// ^ Hibernate compares, sees salary changed, runs:
//   UPDATE employees SET name='Alice', salary=95000 WHERE id=1
```

---

## When does the comparison actually run?

The comparison runs during a **flush**. A flush is when Hibernate synchronises your in-memory state with the database.

By default (`FlushMode.AUTO`), a flush happens:
- just before a query runs (so the query sees your latest changes), and
- when the transaction commits.

You can also trigger one manually at any time:

```java
session.flush(); // or em.flush() if using EntityManager
```

---

## The default UPDATE includes all columns — not just the dirty one

By default Hibernate puts **every column** in the UPDATE, even unchanged ones:

```sql
-- Only salary changed, but Hibernate updates everything:
UPDATE employees SET name='Alice', salary=95000, dept='Eng' WHERE id=1
```

Why? Hibernate pre-builds SQL templates to reuse them — including all columns keeps the template simple.

If your table is wide (many columns) and most are untouched, this is wasteful. The fix is `@DynamicUpdate` on the entity class:

```java
@Entity
@DynamicUpdate   // only include changed columns in the UPDATE
public class Employee { ... }
```

```sql
-- Now with @DynamicUpdate:
UPDATE employees SET salary=95000 WHERE id=1
```

The trade-off: Hibernate can no longer reuse a pre-built SQL template — it has to build the UPDATE string fresh each time. Worth it for wide tables; probably not worth it for narrow ones.

---

## Dirty checking only works while the session is open

Once the session closes, Hibernate **stops tracking your entity**. The entity becomes "detached" — it's just a plain Java object now, no snapshot, no tracking.

```java
Session s1 = sessionFactory.openSession();
Employee emp = s1.get(Employee.class, 1L);
s1.close();
// emp is now detached. Hibernate forgot about it.

emp.setSalary(99_000);
// This change is invisible to Hibernate.
```

To save those changes you have to **re-attach** the entity in a new session using `merge()`:

```java
Session s2 = sessionFactory.openSession();
s2.beginTransaction();

Employee managed = s2.merge(emp);
// merge() loads the current DB row, copies emp's values onto it,
// and gives you back a tracked copy. On commit → UPDATE.

s2.getTransaction().commit();
```

---

## When dirty checking does NOT happen

| Situation | Why |
|---|---|
| `StatelessSession` | Designed for bulk work — no L1 cache, no snapshots, no dirty checking. You must call `ss.update(entity)` explicitly. |
| Bulk HQL/JPQL UPDATE | `UPDATE Employee SET salary = ...` goes straight to the DB. Hibernate never touches the L1 cache. |
| Native SQL UPDATE | Same — Hibernate has no idea the DB changed. |

In all three cases, if you continue using the same session after the operation, any entities already in the L1 cache are now **stale** (out of sync with the DB). Fix: call `session.clear()` to wipe the cache, or `session.refresh(entity)` to reload a specific one.

---

## The `@Immutable` escape hatch

If an entity is pure read-only data (e.g. a country code table), tell Hibernate to skip dirty checking for it entirely:

```java
@Entity
@Immutable
public class Country {
    @Id private String code;
    private String name;
}
```

Hibernate will never generate an UPDATE for `Country`, regardless of what you do to it in code. Saves pointless work on every flush.

---

## Quick recap

- Hibernate **snapshots** every entity when you load it.
- On **flush**, it compares current state to the snapshot — differences → UPDATE.
- Default UPDATE covers all columns; `@DynamicUpdate` limits it to changed columns only.
- Dirty checking stops the moment the **session closes** (entity becomes detached).
- Re-attach a detached entity with `merge()` to get tracking back.
- `StatelessSession` and bulk DML **skip dirty checking entirely**.
- `@Immutable` opts a read-only entity out of dirty checking permanently.

---

## References

- Hibernate 5.6 User Guide — [Flushing](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#flushing)
- Hibernate 5.6 User Guide — [Persistence Context managed state](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#pc-managed-state)
- Hibernate 5.6 User Guide — [Immutability](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#entity-immutability)
