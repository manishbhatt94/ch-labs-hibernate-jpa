# Database Interaction (CRUD) -- Single Row Operations - Using XML-Only Approach

Database Interaction (CRUD):
- Single Row Operation
- Bulk Row Operation

Single Row Operation:
1. XML (No Annotations) -> Configuration & Mapping (ORM)
1. XML + Annotations (Hybrid)
1. Annotations Only (No XML)

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
