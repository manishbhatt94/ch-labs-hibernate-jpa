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
<!-- hibernate.cfg.xml File: -->

<hibernate-configuration>
	<session-factory>
		<!-- JDBC Database connection settings -->
		<!-- ... -->

		<!-- Specify the Hibernate Mapping file(s): -->
		<mapping resource="Employee.hbm.xml" />
		<mapping resource="Department.hbm.xml" />
	</session-factory>
</hibernate-configuration>
```
