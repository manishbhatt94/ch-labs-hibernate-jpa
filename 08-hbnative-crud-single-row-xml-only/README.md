# Hibernate Native Single Row CRUD Operations -- "XML ONLY Approach"

## Hibernate Mapping File(s) -- [EntityName].hbm.xml

For each entity class we have, we create a Hibernate Mapping (hbm) XML file,
named as `[EntityClassName].hbm.xml` and place it on classpath (e.g. same location
as of `hibernate.cfg.xml` or inside `src/` folder directly).

E.g. If we have two entity classes: `Employee` and `Department`, then we will
create below two (2) Hibernate Mapping (hbm) XML files:
1. `src/Employee.hbm.xml`
1. `src/Department.hbm.xml`

Then, we also need to mention all these *Hibernate Mapping (HBM) XML Files* in
the *Hibernate Configuration (CFG) XML File* (i.e. inside `hibernate.cfg.xml`
file), using the `<mappping resource=" />` tag / element, inside the
`<session-factory>` tag / element. Like below:

```xml
<!-- File: hibernate.cfg.xml -->

<hibernate-configuration>
	<session-factory>
		<!-- JDBC Database connection settings -->
		<!-- ... -->

		<!-- Specify the Hibernate Mapping file(s): -->
		<!-- (Register your entity mapping files here) -->
		<mapping resource="Employee.hbm.xml" />
		<mapping resource="Department.hbm.xml" />
	</session-factory>
</hibernate-configuration>
```

### XML Header (DTD / Schema /Namespace / etc.) to use in Hibernate Mapping ".hbm.xml" Files

Refer [this README section](https://github.com/manishbhatt94/ch-labs-hibernate-jpa/tree/main/06-hbnative-conn-xml#hibernative-native-api---xsd--dtd-configuration-file--mapping-file)
for details on where to find the `hibernate-mapping-3.0.dtd` DTD File inside
the JAR of `hibernate-core` from your Eclipse IDE, and then copy the sample
`DOCTYPE` header present in that file, to the top of your `*.hbm.xml` mapping
files.

The header (or top portion) of your Hibernate Mapping files should contain:

```xml
<!-- File: EntityClassName.hbm.xml -->


<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE hibernate-mapping PUBLIC 
    "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
    "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">

<!-- DOCTYPE definition done -->

<!-- Now define the details of the mapping for EntityClassName below,
	within a <hibernate-mapping></hibernate-mapping> tag/element:
 -->
<hibernate-mapping>
	...
</hibernate-mapping>
```

Very brief explanations on parts of the Hibernate Mapping files can be found at
Hibernate 5.6 Getting Started Guide's Section on **2.3 The mapping file** here:
[docs.hibernate.org/orm/5.6/quickstart/html_single/#hibernate-gsg-tutorial-basic-mapping](https://docs.hibernate.org/orm/5.6/quickstart/html_single/#hibernate-gsg-tutorial-basic-mapping)

This TutorialsPoint article on **Hibernate - Mapping Files** explains in a
short & simple way the syntax & structure of these `*.hbm.xml` files. This
article can be found at
[www.tutorialspoint.com/hibernate/hibernate_mapping_files.htm](https://www.tutorialspoint.com/hibernate/hibernate_mapping_files.htm)
link.

---

## JPA Methods vs Hibernate (Native) Methods for CRUD Single-Row Operations


| Operation | JPA Method<br> (on EntityManager interface) | Hibernate Native Method<br> (on Session interface) |
| --------- | ---------- | ----------------------- |
| **INSERT** | **persist()**  | **save()**                  |
| Signature: | `public void persist(Object entity);` | `Serializable save(Object object);` |
| **READ**<br> *(Eager)*   | **find()**          |  **get()** |
| Signature: | `public <T> T find(Class<T> entityClass, Object primaryKey);` | `<T> T get(Class<T> entityType, Serializable id);` |
| **READ**<br> *(Lazy)*    | **getReference()**  |  **load()** |
| Signature: | `public <T> T getReference(Class<T> entityClass, Object primaryKey);` | `<T> T load(Class<T> theClass, Serializable id);` |
| **UPDATE** | **merge()** | **update()** |
| Signature: | `public <T> T merge(T entity);` | `void update(Object object);` |
| **DELETE** | **remove()** | **delete()** |
| Signature: | `public void remove(Object entity);` | `void delete(Object object);` |


---

# Hibernate / JPA — "get()"&nbsp; vs "load()" (Notes)

This topic about *Obtaining an entity object with/without data* is covered
somewhat in **Hibernate 5.6 Docs - User Guide - Section 5.5 & 5.6** as linked below:

- [5.5. Obtain an entity reference without initializing its data](https://docs.hibernate.org/orm/5.6/userguide/html_single/#pc-get-reference)
- [5.6. Obtain an entity with its data initialized](https://docs.hibernate.org/orm/5.6/userguide/html_single/#pc-find)


## TL;DR

| | Hits DB immediately? | Returns `null` if missing? | Throws if missing? | Object type returned |
|---|---|---|---|---|
| **EAGER** family (`get`, `find`) | Yes, right away | Yes | No | Real initialized entity (or `null`) |
| **LAZY** family (`load`, `getReference`) | No, deferred until field is accessed | No, never `null` | Yes, but only **on first access**, not on call | Proxy (uninitialized placeholder) |

**Rule of thumb:**
- If you need to *read* data → use the EAGER family.
- If you only need an entity to *set a reference/FK* (e.g. `invoice.setCustomer(session.load(Customer.class, id))`) and you're **sure it exists**, use the LAZY family to skip a needless `SELECT`.


## Why both exist

Sometimes you genuinely need the data (display it, validate it, etc.) — that's `get()`/`find()`.

Sometimes you only need the **identifier wrapped in an entity instance**, typically to:
- wire up an association (`order.setCustomer(ref)`), or
- issue a `DELETE`/`UPDATE` by id

without paying for a `SELECT` you don't need. That's exactly what `load()`/`getReference()` are for — they hand you a **proxy** that only goes to the DB if you actually touch a non-id field.


## Hibernate Native API

### `Session#get(Class<T> entityType, Serializable id)`
- **EAGER**. Queries immediately (after checking 1st/2nd level cache).
- Returns the real entity, or **`null`** if no row exists.
- Safe to null-check.

### `Session#load(Class<T> entityType, Serializable id)`
- **LAZY**. Returns a **proxy** instantly, no query yet.
- Never returns `null`.
- If the row doesn't actually exist, you only find out when you touch a field on the proxy → throws `org.hibernate.ObjectNotFoundException` (unchecked exception) **at that point**, not when `load()` was called.
- ⚠️ If you only ever read the id (`proxy.getId()`), no query is ever fired, since the id is already known — this is the whole point of using it for associations.

### `Session#byId(Class<T> entityClass)` &nbsp;→&nbsp; `IdentifierLoadAccess<T>`

This is just a fluent builder around the two above. **Important gotcha — naming trap:**

```java
session.byId(Person.class).load(id);          // ⚠️ NOT like Session#load()!
                                                //   This is EAGER — same as Session#get()
session.byId(Person.class).getReference(id);   // This is the LAZY one — same as Session#load()
```

So the method named `load()` on `IdentifierLoadAccess` does **not** match the behavior of `Session#load()`. Easy to mix up — worth remembering explicitly:

| Fluent call | Equivalent to |
|---|---|
| `byId(X.class).load(id)` | `session.get(X.class, id)` (EAGER) |
| `byId(X.class).getReference(id)` | `session.load(X.class, id)` (LAZY) |


## JPA Standard API

### `EntityManager#find(Class<T> entityClass, Object primaryKey)`
- **EAGER**. JPA's equivalent of `Session#get()`. Returns `null` if not found.

### `EntityManager#getReference(Class<T> entityClass, Object primaryKey)`
- **LAZY**. JPA's equivalent of `Session#load()`. Returns a proxy; throws `javax.persistence.EntityNotFoundException` unchecked exception (or causes a failure on flush) only once a non-id field is accessed and the row turns out not to exist.


## Full equivalence table

| EAGER (init now, may be null) | LAZY (proxy, may throw on access) |
|---|---|
| `session.get(X.class, id)` | `session.load(X.class, id)` |
| `session.byId(X.class).load(id)` | `session.byId(X.class).getReference(id)` |
| `entityManager.find(X.class, id)` | `entityManager.getReference(X.class, id)` |


## When to use which

- **Use `get()` / `find()`** when you're about to read/display/validate the entity's actual data, or when "does this exist?" is part of your logic (null check).
- **Use `load()` / `getReference()`** only when:
  - you just need to set a FK/association on another entity, or
  - you're deleting/updating by id and don't need the current state, **and**
  - you are confident the row exists (e.g. the id came from a valid FK already in the DB).

## When &nbsp;*not*&nbsp; to use "load()" / "getReference()"

- Don't use it if there's any chance the id doesn't exist — you'll get a runtime exception later, somewhere far from the call site, which is harder to debug than a simple `null` check.
- Don't let the returned proxy escape outside the session/transaction and then access its fields later → `LazyInitializationException`.
- Don't use it when you actually need to read a field right after — at that point it just becomes a deferred `get()` with extra proxy overhead.
