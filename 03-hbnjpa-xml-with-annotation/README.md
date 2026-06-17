# Using XML with Annotations Approach

Here we use Hibernate Framework as a JPA Implementation / Provider, and we use
a mix of XML and annotations to provide configurations.

We will keep the "persistence.xml" to provide DB connection related config.

But, we will remove the Object/Relational Mapping "orm.xml" File, and instead
provide the Object (Entity) to Database mapping in the Entity Java class itself
using JPA Annotations.

We will need to refer to the this Entity java source file from the Persistence
Descriptor (i.e. "persistence.xml" file), to inform the Framework that it can
find mapping related configurations in this/these Java class(es) (via JPA
annotations).

We inform Hibernate's JPA implementation about which Java classes act as an
Entity, and provide Object/Relational Mappings via annotations, using the
`<class>...fully-qualified-class-name....</class>` element in Persistence
Descriptor, inside the `<persistence-unit>` element, like this:

```xml
<!-- "persistence.xml" file -->
...
<persistence-unit name="my-persistence-unit-1">

	<provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>

	<!-- Element : class. Managed class to be included in the persistence unit 
		and to scan for annotations. It should be annotated with either @Entity, 
		@Embeddable or @MappedSuperclass. -->
	<class>com.entity.Employee</class>

</persistence-unit>
...
```

## Few JPA annotations to be used in Entity classes

> [!IMPORTANT]
> Currently, we are using Hibernate Framework's JPA Implementation (or Hibernate
> Framework as a JPA Provider), in order to keep the option open in future, to
> switch to a different JPA Provider Framework (like Eclipse Link, OpenJPA, etc.)
> altogether.
>
> Therefore, it is important to note that, here, because of above sentence, we
> need to use interfaces, annotations, classes from the JPA packages only, i.e.
> from `javax.persistence.*` only (and never from Hibernate's package
> `org.hibernate.*`).
>
> So, as we'll see, let's say, the `@Entity` annotation is present in both JPA
> package at `javax.persistence.Entity` and also present in Hibernate package
> at `org.hibernate.annotations.Entity`. But, when we're using Hibernate's JPA
> implementation (like we *are* in this project), we need to import & use the
> `@Entity` annotation from JPA package only.


---
<br>

# Program Sample Run Output

Here is the program output on a sample run:

<details>

<summary>Sample Run Output:</summary>

```txt
Jun 08, 2026 3:29:48 PM org.hibernate.jpa.internal.util.LogHelper logPersistenceUnitInformation
INFO: HHH000204: Processing PersistenceUnitInfo [name: my-persistence-unit-1]
Jun 08, 2026 3:29:48 PM org.hibernate.Version logVersion
INFO: HHH000412: Hibernate ORM core version 5.6.5.Final
Jun 08, 2026 3:29:48 PM org.hibernate.annotations.common.reflection.java.JavaReflectionManager <clinit>
INFO: HCANN000001: Hibernate Commons Annotations {5.1.2.Final}
Jun 08, 2026 3:29:49 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl configure
WARN: HHH10001002: Using Hibernate built-in connection pool (not for production use!)
Jun 08, 2026 3:29:49 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001005: using driver [com.mysql.cj.jdbc.Driver] at URL [jdbc:mysql://localhost:3306/ch_labs_hibernate_01]
Jun 08, 2026 3:29:49 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001001: Connection properties: {user=root, password=****}
Jun 08, 2026 3:29:49 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001003: Autocommit mode: false
Jun 08, 2026 3:29:49 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl$PooledConnections <init>
INFO: HHH000115: Hibernate connection pool size: 20 (min=1)
Jun 08, 2026 3:29:49 PM org.hibernate.dialect.Dialect <init>
INFO: HHH000400: Using dialect: org.hibernate.dialect.MySQL8Dialect
Jun 08, 2026 3:29:49 PM org.hibernate.resource.transaction.backend.jdbc.internal.DdlTransactionIsolatorNonJtaImpl getIsolatedConnection
INFO: HHH10001501: Connection obtained from JdbcConnectionAccess [org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator$ConnectionProviderJdbcConnectionAccess@20921b9b] for (non-JTA) DDL execution was not in auto-commit mode; the Connection 'local transaction' will be committed and the Connection will be set into auto-commit mode.
Jun 08, 2026 3:29:49 PM org.hibernate.engine.transaction.jta.platform.internal.JtaPlatformInitiator initiateService
INFO: HHH000490: Using JtaPlatform implementation: [org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform]
EntityManager created successfully: SessionImpl(1075996552<open>)
Connection to the database established successfully!


Inserting an Employee entity into the database...
Hibernate: insert into hbn_employee (employee_address, employee_name, employee_salary, employee_id) values (?, ?, ?, ?)
Insert operation completed successfully!


Reading an Employee entity from the database...
Hibernate: select employee0_.employee_id as employee1_0_0_, employee0_.employee_address as employee2_0_0_, employee0_.employee_name as employee3_0_0_, employee0_.employee_salary as employee4_0_0_ from hbn_employee employee0_ where employee0_.employee_id=?
Employee found: Employee [employeedId=2, employeeName=Nasir Jones, employeeAddress=21st Main, Queens Bridge, NY, employeeSalary=11200]


Updating an Employee entity in the database...
Hibernate: update hbn_employee set employee_address=?, employee_name=?, employee_salary=? where employee_id=?
Update operation completed successfully!


Reading an Employee entity from the database...
Employee found: Employee [employeedId=2, employeeName=Nasir Jones, employeeAddress=21st Main, Queens Bridge, NY, employeeSalary=15700]


Deleting an Employee entity from the database...
Hibernate: delete from hbn_employee where employee_id=?
Delete operation completed successfully!


Reading an Employee entity from the database...
Hibernate: select employee0_.employee_id as employee1_0_0_, employee0_.employee_address as employee2_0_0_, employee0_.employee_name as employee3_0_0_, employee0_.employee_salary as employee4_0_0_ from hbn_employee employee0_ where employee0_.employee_id=?
Employee found: null
Jun 08, 2026 3:29:49 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl$PoolState stop
INFO: HHH10001008: Cleaning up connection pool [jdbc:mysql://localhost:3306/ch_labs_hibernate_01]


EntityManager and EntityManagerFactory closed successfully. Resources released.

```

</details>

<br>

---

## Sample output for Hibernate JPA Implementation - Eager vs Lazy -- "find()" vs "getReference()"

<details>

<summary>Hibernate JPA Implementation - "find()" vs "getReference()" -- Program Run Output</summary>

```txt
EntityManager created successfully: SessionImpl(600657906<open>)
Connection to the database established successfully!


======== Demo: EntityManager#find() [Eager] vs. EntityManager#getReference() [Lazy] =========


~-~-~-~-~-~-~-~ EntityManager#find() [Eager] ~-~-~-~-~~-~-~-~-~-~-~


------>>> When entity with given ID - EXISTS in DB:

-@-- Calling EntityManager#find() with ID: 1 ...
Hibernate: select employee0_.employee_id as employee1_0_0_, employee0_.employee_address as employee2_0_0_, employee0_.employee_name as employee3_0_0_, employee0_.employee_salary as employee4_0_0_ from hbn_employee employee0_ where employee0_.employee_id=?
-@---- Called EntityManager#find() with ID: 1
-@-- Returned employee object class is: [class com.entity.Employee] -- which IS NOT A proxy!
-@-- Received <non-null> entity object with ID: 1
-@-- DB table record with ID: 1 -- EXISTS!
-@-- Calling employee.getEmployeeId() ... 
-@---- Received employeeId: 1
-@- Accessing non-ID fields of employee ... 
-@-- Calling employee.getEmployeeName() ... 
-@---- Received employeeName: Eric Wright
-@-- Calling employee.toString() ... 
-@---- employee.toString(): Employee [employeeId=1, employeeName=Eric Wright, employeeAddress=122nd Ave., Compton, CA, employeeSalary=8920]
-@- Able to access non-ID fields of employee!

------>>> When entity with given ID - DOES NOT EXIST in DB:

-@-- Calling EntityManager#find() with ID: 9999 ...
Hibernate: select employee0_.employee_id as employee1_0_0_, employee0_.employee_address as employee2_0_0_, employee0_.employee_name as employee3_0_0_, employee0_.employee_salary as employee4_0_0_ from hbn_employee employee0_ where employee0_.employee_id=?
-@---- Called EntityManager#find() with ID: 9999
-@-- Received <null> from EntityManager#find() call with ID: 9999
-@-- DB table record with ID: 9999 -- DOES NOT EXIST!


~-~-~-~-~-~-~-~ EntityManager#getReference() [Lazy] ~-~-~-~-~~-~-~-~-~-~-~


------>>> When entity with given ID - EXISTS in DB:

-@-- Calling EntityManager#getReference() with ID: 1 ...
-@---- Called EntityManager#getReference() with ID: 1
-@-- Returned employee object class is: [class com.entity.Employee$HibernateProxy$6KRpWtkm] -- which IS A proxy!
-@-- Received <non-null> entity object with ID: 1
-@-- DB table record with ID: 1 -- MIGHT or MIGHT NOT EXIST!
-@-- Calling employee.getEmployeeId() ... 
-@---- Received employeeId: 1
-@- Accessing non-ID fields of employee ... 
-@-- Calling employee.getEmployeeName() ... 
Hibernate: select employee0_.employee_id as employee1_0_0_, employee0_.employee_address as employee2_0_0_, employee0_.employee_name as employee3_0_0_, employee0_.employee_salary as employee4_0_0_ from hbn_employee employee0_ where employee0_.employee_id=?
-@---- Received employeeName: Eric Wright
-@-- Calling employee.toString() ... 
-@---- employee.toString(): Employee [employeeId=1, employeeName=Eric Wright, employeeAddress=122nd Ave., Compton, CA, employeeSalary=8920]
-@- Able to access non-ID fields of employee!
-@-- Therefore, DB table record with ID: 1 -- EXISTS!

------>>> When entity with given ID - DOES NOT EXIST in DB:

-@-- Calling EntityManager#getReference() with ID: 9999 ...
-@---- Called EntityManager#getReference() with ID: 9999
-@-- Returned employee object class is: [class com.entity.Employee$HibernateProxy$6KRpWtkm] -- which IS A proxy!
-@-- Received <non-null> entity object with ID: 9999
-@-- DB table record with ID: 9999 -- MIGHT or MIGHT NOT EXIST!
-@-- Calling employee.getEmployeeId() ... 
-@---- Received employeeId: 9999
-@- Accessing non-ID fields of employee ... 
-@-- Calling employee.getEmployeeName() ... 
Hibernate: select employee0_.employee_id as employee1_0_0_, employee0_.employee_address as employee2_0_0_, employee0_.employee_name as employee3_0_0_, employee0_.employee_salary as employee4_0_0_ from hbn_employee employee0_ where employee0_.employee_id=?
-@-- Caught EntityNotFoundException while accessing non-ID fields. Exception message: Unable to find com.entity.Employee with id 9999
-@-- Therefore, DB table record with ID: 9999 -- DOES NOT EXIST!

Jun 17, 2026 4:37:20 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl$PoolState stop
INFO: HHH10001008: Cleaning up connection pool [jdbc:mysql://localhost:3306/ch_labs_hibernate_01]


EntityManager and EntityManagerFactory closed successfully. Resources released.

```

</details>

<br>

> [!NOTE]
> Also refer to section which shows Sample Output for Hibernate Native API's
> **"get()" vs "load()"** eager/lazy methods in
> [this README](https://github.com/manishbhatt94/ch-labs-hibernate-jpa/tree/main/08-hbnative-crud-single-row-xml-only)

