# JPA - Bulk Row Operation - Approaches

JPA provides us with below 3 approaches to perform **Bulk Row Operations** (and
Hibernate as a JPA Provider - implements those):

1. **Native SQL**
1. **JPQL** (Java Persistence Query Language)
1. **Criteria API** (*Pure Java* based approach, & considered type-safe)

---

### Native SQL

- Write raw SQL using actual table and column names.
- Directly executed by the database engine.
- Supports **all** CRUD operations: INSERT, SELECT, UPDATE, DELETE.

#### Best suited when:

- You need DB-specific features (e.g., MySQL keywords, stored procedures).
- You want full control over SQL.
- You want to have access to low level SQL queries.
- You need to run highly optimized low level SQL.

---

### JPQL (Java Persistence Query Language)

- Object-oriented query language using entity class and field names.
- Translated to SQL by the JPA provider (like Hibernate).
- **Does NOT support INSERT.**
- Supports SELECT, UPDATE, DELETE.
- Best suited for writing **portable** (i.e. *Database Independent*) & clean
  logic **across databases**.

---

### Criteria API

- *Pure Java way* to build queries using `CriteriaBuilder`.
- We don't write queries directly in this approach, but instead call Java
  methods that look like SQL clauses (like SELECT, WHERE, UPDATE, etc.).
- Supports: SELECT, UPDATE, DELETE.
- **Does not support INSERT.**
- This approach is also **portable** across RDBMS vendors (i.e.
  *Database Independent*), since here we don't directly write queries, instead
  we call Java methods, which gets converted into the RDBMS vendor-specific SQL
  query by the JPA Provider Framework (e.g. Hibernate's JPA Implementation).

#### Best suited for:
- Dynamic queries.
- **Type safety.** (We get compile-time errors if we make any mistakes because
  we aren't writing any queries at all that get placed in String variables, and
  if there's any mistakes in the query, we only get to know at run time. Instead
  , here we call Java methods & any typos in method names while calling is
  flagged at compile-time).
- Query-free code.

---

### Named Queries in JPA
- Predefined queries using annotations – reused multiple times.
- Defined at Entity level using @NamedQuery or @NamedNativeQuery.
- Improves code readability, performance (can be compiled at startup).
