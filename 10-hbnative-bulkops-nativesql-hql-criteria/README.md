# Hibernate Native - Bulk Row Operations - Approaches

We earlier saw the various approaches to perform Bulk Row Operations using pure
JPA, with JPA Native SQL method, JPA JPQL method, and JPA Criteria API method,
in: [05-hbnjpa-bulkops-nativesql-jpql-criteria](../05-hbnjpa-bulkops-nativesql-jpql-criteria/README.md)
project.

Here, we discuss the Bulk Row Operations approaches available to use under
Hibernate's Native API, and how these differ from JPA provided approaches.

Hibernate Native API provides below approaches for Bulk Row Operations:

- Native SQL
- HQL (Hibernate Query Language)
- Hibernative Native Criteria API (Deprecated)

Below is a more descriptive introduction to these approaches in Hibernate Native
presented in short bullet points:

- Native SQL (`Session#createNativeQuery()`)
  - Allows writing raw SQL queries.
  - Supports INSERT, UPDATE, and DELETE.
  - Suitable for performance-critical or database-specific tasks.

- HQL i.e. Hibernate Query Language (`Session#createQuery()`)
  - Object-oriented query language (uses entity names).
  - Supports READ, UPDATE and DELETE (bulk).
  - Cannot be used for INSERT operations.
  - HQL is slightly more powerful (i.e. feature-rich) than JPQL.
    - In HQL, You can copy data from one table to another ( `insert into (…..) select` )
    - In HQL, there is this feature called as *Polymorphic Query* which isn't
      available in JPQL.
    - etc.
  - Documentation links for HQL:
    - [Hibernate ORM 5.6.15.Final User Guide &sect; Hibernate Query Language for DML](https://docs.hibernate.org/orm/5.6/userguide/html_single/#batch-bulk-hql)
    - [Hibernate ORM 5.6.15.Final User Guide &sect; HQL and JPQL](https://docs.hibernate.org/orm/5.6/userguide/html_single/#hql)
    - [Hibernate 6.6.53.Final &sect; A Guide to Hibernate Query Language](https://docs.hibernate.org/orm/6.6/querylanguage/html_single/)
    - [Hibernate 7.4.2.Final &sect; A Guide to Hibernate Query Language](https://docs.hibernate.org/orm/7.4/querylanguage/html_single/)

- Hibernate Native's Criteria API (Deprecated)
  - This is the old Hibernate Criteria API (`Session#createCriteria()`).
  - **Only supports SELECT operations (bulk read).**
  - Does not support bulk UPDATE, DELETE, or INSERT.
    - Whereas, in JPA's Criteria API, we can perform SELECT, UPDATE & DELETE
      operations using CriteriaQuery, CriteriaUpdate & CriteriaDelete
      interfaces from `javax.persistence.criteria` package.
  - Hibernative Native's Criteria API has been **DEPRECATED** in Hibernate 5,
    and removed in Hibernate 6.
  - **Note:** Criteria API is *NOT DEPRECATED in JPA*. Only Hibernative Native's
    Criteria API has been deprecated.
