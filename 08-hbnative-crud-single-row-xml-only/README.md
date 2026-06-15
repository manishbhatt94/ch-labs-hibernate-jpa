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
