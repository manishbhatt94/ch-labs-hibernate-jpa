# Database Interaction (CRUD) -- Single Row Operations - Using XML-Only Approach

Database Interaction (CRUD):
- Single Row Operation
- Bulk Row Operation

Various choices of approach for **Single Row Operation**:
1. XML (No Annotations) Approach -> Configuration & Mapping (ORM)
1. XML + Annotations Approach (Hybrid)
1. Annotations Only Approach (No XML)

## Entity to RDBMS Mappings in XML

As described in
[HBN_JPA_SETUP.md - Persistence XML](../HBN_JPA_SETUP.md#how-our-persistence-xml-file-should-look)
we need to define a new XML file that contains mappings between objects & RDBMS
table/column.

This file is called **Object/Relational Mapping (XML) File**.

It is conventionally called `orm.xml` and *can be* placed at path `META-INF/orm.xml`.

However, as stated in the **schema file (below)** for this Mapping XML,
it is not necessary that this file be named `orm.xml` or be placed at the above
path only.

Also read section on **Externalizing XML mapping files** in
*Hibernate 5.6 User Guide* at this link:
[docs.hibernate.org/orm/5.6/userguide/html_single/#bootstrap-jpa-xml-files](https://docs.hibernate.org/orm/5.6/userguide/html_single/#bootstrap-jpa-xml-files)

### Schema Definition (XSD) File for "Object/Relational Mapping" configuration file

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- Java Persistence API object/relational mapping file schema -->
<xsd:schema targetNamespace="http://xmlns.jcp.org/xml/ns/persistence/orm" 
  xmlns:orm="http://xmlns.jcp.org/xml/ns/persistence/orm" 
  xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
  elementFormDefault="qualified" 
  attributeFormDefault="unqualified" 
  version="2.2">

  <xsd:annotation>
    <xsd:documentation>
      @(#)orm_2_2.xsd 2.2  July 7 2017
    </xsd:documentation>
  </xsd:annotation>

  <xsd:annotation>
    <xsd:documentation>

  Copyright (c) 2008 - 2017 Oracle Corporation.  All rights reserved. 
  
  This program and the accompanying materials are made available under the 
  terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
  which accompanies this distribution. 
  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
  and the Eclipse Distribution License is available at 
  http://www.eclipse.org/org/documents/edl-v10.php.
  
  Contributors:
      Linda DeMichiel - Java Persistence 2.2, Version 2.2 (July 7, 2017)
      Specification available from http://jcp.org/en/jsr/detail?id=338
  
    </xsd:documentation>
  </xsd:annotation>

  <xsd:annotation>
     <xsd:documentation><![CDATA[

       This is the XML Schema for the persistence object/relational 
       mapping file.
       The file may be named "META-INF/orm.xml" in the persistence 
       archive or it may be named some other name which would be 
       used to locate the file as resource on the classpath.

       Object/relational mapping files must indicate the object/relational
       mapping file schema by using the persistence namespace:

       http://xmlns.jcp.org/xml/ns/persistence/orm

       and indicate the version of the schema by
       using the version element as shown below:

      <entity-mappings xmlns="http://xmlns.jcp.org/xml/ns/persistence/orm"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence/orm
          http://xmlns.jcp.org/xml/ns/persistence/orm_2_2.xsd"
        version="2.2">
          ...
      </entity-mappings>


     ]]></xsd:documentation>
  </xsd:annotation>

  ...

</xsd:schema>
```

## Object/Relational Mapping File (can be named "orm.xml")

Here is the XML metadata with namespace & schema attributes that we must
provide at the start of the **Object/Relational Mapping File ("orm.xml")**:

```xml
<entity-mappings xmlns="http://xmlns.jcp.org/xml/ns/persistence/orm"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence/orm
      http://xmlns.jcp.org/xml/ns/persistence/orm_2_2.xsd"
    version="2.2">

    ...

</entity-mappings>
```

## Short Notes on "orm.xml" Elements/Tags & Attributes

### "entity" Element ("entity-mappings" -> "entity")

```xml
<entity-mappings ...>
  <entity class="com.myapp.entities.Employee" access="PROPERTY">
  </entity>
</entity-mappings>
```

Attributes:
- `class`: Contains the Fully Qualified Name of the class whose object (i.e. an entity)
  we need to map to an RDBMS table.
- `access` (values either `"PROPERTY"` or `"FIELD"`): Informs whether JPA Provider
  (i.e. Hibernate) should access the entity object's members (instance variables)
  using:
  - **Getters** (when `access="PROPERTY"` - which is the default setting), or
  - **Direct field access** (when `access="FIELD"`) - which the JPA Provider does
     using Reflection API in order to access private instance variables.

### "table" Element ("entity" -> "table")

```xml
<entity-mappings ...>
  <entity class="com.myapp.entities.Employee" access="PROPERTY">
    <table name="hbn_employee" />
  </entity>
</entity-mappings>
```

Attributes:
- `name`: The `<table name="custom_table_name" />` element is provided in case
  we need the name of the DB table to be a custom name, which is derived
  automatically from the Entity class name by Hibernate. We can omit this
  `<table>` element if we are okay with letting Hibernate pick the table name
  from the name of the Entity class.

### "attributes" Element ("entity" -> "attributes")

```xml
<entity class="com.entity.Employee">
	<attributes>
		<id name="employeedId">
			<column name="employee_id" />
		</id>
		<basic name="employeeName">
			<column name="employee_name" length="40" />
		</basic>
		<basic name="employeeAddress"> <!-- Map the "employeeAddress" property in Entity class, to.. -->
			<column name="employee_address" length="160" />
		</basic>
	</attributes>
</entity>
```

The `<attributes>` Element (placed within the `<entity>` Element), is used to
list down within the element, how the fields inside the Entity Java class map
to the columns inside the corresponding DB table.

`<attributes>` Element can have within it, including others:

- An `<id>` Element which specifies the Primary Key column(s);
- Multiple `<basic>` Elements which specify other (non Primary Key) columns

