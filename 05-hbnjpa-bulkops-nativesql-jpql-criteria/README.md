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

---

## Sample Program Output - Using Native SQL Query

<details>

<summary>Here's a sample run output for program - `Launch_NativeSQL.java`</summary>

```txt
Jun 09, 2026 5:37:39 PM org.hibernate.jpa.internal.util.LogHelper logPersistenceUnitInformation
INFO: HHH000204: Processing PersistenceUnitInfo [name: my-persistence-unit-1]
Jun 09, 2026 5:37:39 PM org.hibernate.Version logVersion
INFO: HHH000412: Hibernate ORM core version 5.6.5.Final
Jun 09, 2026 5:37:39 PM org.hibernate.annotations.common.reflection.java.JavaReflectionManager <clinit>
INFO: HCANN000001: Hibernate Commons Annotations {5.1.2.Final}
Jun 09, 2026 5:37:39 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl configure
WARN: HHH10001002: Using Hibernate built-in connection pool (not for production use!)
Jun 09, 2026 5:37:39 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001005: using driver [com.mysql.cj.jdbc.Driver] at URL [jdbc:mysql://localhost:3306/ch_labs_hibernate_01]
Jun 09, 2026 5:37:39 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001001: Connection properties: {user=root, password=****}
Jun 09, 2026 5:37:39 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001003: Autocommit mode: false
Jun 09, 2026 5:37:39 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl$PooledConnections <init>
INFO: HHH000115: Hibernate connection pool size: 20 (min=1)
Jun 09, 2026 5:37:39 PM org.hibernate.dialect.Dialect <init>
INFO: HHH000400: Using dialect: org.hibernate.dialect.MySQL8Dialect
Jun 09, 2026 5:37:40 PM org.hibernate.resource.transaction.backend.jdbc.internal.DdlTransactionIsolatorNonJtaImpl getIsolatedConnection
INFO: HHH10001501: Connection obtained from JdbcConnectionAccess [org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator$ConnectionProviderJdbcConnectionAccess@15fc442] for (non-JTA) DDL execution was not in auto-commit mode; the Connection 'local transaction' will be committed and the Connection will be set into auto-commit mode.
Jun 09, 2026 5:37:40 PM org.hibernate.engine.transaction.jta.platform.internal.JtaPlatformInitiator initiateService
INFO: HHH000490: Using JtaPlatform implementation: [org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform]
EntityManager created successfully: SessionImpl(1074630954<open>)
Connection to the database established successfully!


Inserting Employee records into the database...

Hibernate: INSERT INTO hbn_employee 
    (employee_id, employee_name, employee_address, employee_salary) 
    VALUES (?, ?, ?, ?);
Hibernate: INSERT INTO hbn_employee 
    (employee_id, employee_name, employee_address, employee_salary) 
    VALUES (?, ?, ?, ?);
Hibernate: INSERT INTO hbn_employee 
    (employee_id, employee_name, employee_address, employee_salary) 
    VALUES (?, ?, ?, ?);
Hibernate: INSERT INTO hbn_employee 
    (employee_id, employee_name, employee_address, employee_salary) 
    VALUES (?, ?, ?, ?);
Hibernate: INSERT INTO hbn_employee 
    (employee_id, employee_name, employee_address, employee_salary) 
    VALUES (?, ?, ?, ?);

Insert operation completed successfully!


Reading (multiple) Employee records from the database...

Using [Query createNativeQuery(String sqlString)] method 
to create a native SQL query without specifying the result mapping. 
The query results will be returned as an untyped List of Object arrays (List<Object[]>).
Hibernate: SELECT * FROM hbn_employee;
Number of Employee records retrieved: 10

Raw query results (untyped List): [[Ljava.lang.Object;@773bd77b, [Ljava.lang.Object;@6b580b88, [Ljava.lang.Object;@6d91790b, [Ljava.lang.Object;@6e6fce47, [Ljava.lang.Object;@47c64cfe, [Ljava.lang.Object;@6ce90bc5, [Ljava.lang.Object;@a567e72, [Ljava.lang.Object;@1a5b8489, [Ljava.lang.Object;@6f8f8a80, [Ljava.lang.Object;@4b1c0397]

Employee records retrieved from the database:
1 | 122nd Ave., Compton, CA | Eric Wright | 9265 | 
2 | 21st Main, Queens Bridge, NY | Nasir Jones | 15700 | 
3 | Park Hill Projects, Staten Island, NY | Method Man | 9555 | 
4 | Brooklyn, NY | Ol' Dirty Bastard | 10500 | 
5 | 8 Mile Road, Detroit, MI | Slim Shady | 10145 | 
10234 | Indiranagar, Bangalore, Karnataka | Arjun Sharma | 55000 | 
45678 | T. Nagar, Chennai, Tamil Nadu | Sneha Reddy | 52000 | 
98765 | Malviya Nagar, Jaipur, Rajasthan | Vikram Singh | 45000 | 
234567 | Panampilly Nagar, Kochi, Kerala | Priya Nair | 48000 | 
8765432 | Madhapur, Hyderabad, Telangana | Ravi Kumar | 60000 | 

Using [Query createNativeQuery(String sqlString, Class resultClass)] 
method to create a native SQL query with result mapping to the 
Employee entity class. The query results will be returned as an 
untyped List whose entries can be cast to Employee entity objects.
Hibernate: SELECT * FROM hbn_employee;
Number of Employee records retrieved: 10

Raw query results (untyped List): [Employee [employeedId=1, employeeName=Eric Wright, employeeAddress=122nd Ave., Compton, CA, employeeSalary=9265], Employee [employeedId=2, employeeName=Nasir Jones, employeeAddress=21st Main, Queens Bridge, NY, employeeSalary=15700], Employee [employeedId=3, employeeName=Method Man, employeeAddress=Park Hill Projects, Staten Island, NY, employeeSalary=9555], Employee [employeedId=4, employeeName=Ol' Dirty Bastard, employeeAddress=Brooklyn, NY, employeeSalary=10500], Employee [employeedId=5, employeeName=Slim Shady, employeeAddress=8 Mile Road, Detroit, MI, employeeSalary=10145], Employee [employeedId=10234, employeeName=Arjun Sharma, employeeAddress=Indiranagar, Bangalore, Karnataka, employeeSalary=55000], Employee [employeedId=45678, employeeName=Sneha Reddy, employeeAddress=T. Nagar, Chennai, Tamil Nadu, employeeSalary=52000], Employee [employeedId=98765, employeeName=Vikram Singh, employeeAddress=Malviya Nagar, Jaipur, Rajasthan, employeeSalary=45000], Employee [employeedId=234567, employeeName=Priya Nair, employeeAddress=Panampilly Nagar, Kochi, Kerala, employeeSalary=48000], Employee [employeedId=8765432, employeeName=Ravi Kumar, employeeAddress=Madhapur, Hyderabad, Telangana, employeeSalary=60000]]

Employee records retrieved from the database:
Employee [employeedId=1, employeeName=Eric Wright, employeeAddress=122nd Ave., Compton, CA, employeeSalary=9265]
Employee [employeedId=2, employeeName=Nasir Jones, employeeAddress=21st Main, Queens Bridge, NY, employeeSalary=15700]
Employee [employeedId=3, employeeName=Method Man, employeeAddress=Park Hill Projects, Staten Island, NY, employeeSalary=9555]
Employee [employeedId=4, employeeName=Ol' Dirty Bastard, employeeAddress=Brooklyn, NY, employeeSalary=10500]
Employee [employeedId=5, employeeName=Slim Shady, employeeAddress=8 Mile Road, Detroit, MI, employeeSalary=10145]
Employee [employeedId=10234, employeeName=Arjun Sharma, employeeAddress=Indiranagar, Bangalore, Karnataka, employeeSalary=55000]
Employee [employeedId=45678, employeeName=Sneha Reddy, employeeAddress=T. Nagar, Chennai, Tamil Nadu, employeeSalary=52000]
Employee [employeedId=98765, employeeName=Vikram Singh, employeeAddress=Malviya Nagar, Jaipur, Rajasthan, employeeSalary=45000]
Employee [employeedId=234567, employeeName=Priya Nair, employeeAddress=Panampilly Nagar, Kochi, Kerala, employeeSalary=48000]
Employee [employeedId=8765432, employeeName=Ravi Kumar, employeeAddress=Madhapur, Hyderabad, Telangana, employeeSalary=60000]

Read operation completed successfully!


Updating Employee records in the database...

Hibernate: UPDATE hbn_employee 
    SET employee_salary = employee_salary + 345 
    WHERE employee_salary < 11000;
Number of Employee records updated: 4

Update operation completed successfully!


Deleting an Employee entity from the database...

Hibernate: DELETE FROM hbn_employee 
    WHERE employee_id > 10;
Number of Employee records deleted: 5

Delete operation completed successfully!
Jun 09, 2026 5:37:40 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl$PoolState stop
INFO: HHH10001008: Cleaning up connection pool [jdbc:mysql://localhost:3306/ch_labs_hibernate_01]


EntityManager and EntityManagerFactory closed successfully. Resources released.

```

</details>
