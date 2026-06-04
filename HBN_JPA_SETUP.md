# Setup for Hibernate and JPA

JPA (Java Persistence API) is part of JavaEE (Java Enterprise Edition), and not
part of JavaSE (Java Standard Edition).

Therefore, JPA is not a part of the JRE System Library (JSL). Similar to how the
Servlet / JSP related interfaces from the Servlet API were not part of JRE
System Library (JSL), and we had to get them from external sources (which, in
fact, happened to be Apache Tomcat server).

> [!NOTE]
> In the case of JDBC API, we didn't have to get the API interfaces in `java.sql`
> package (like Connection, Statement, ResultSet, etc. interfaces) from any JAR
> or other external sources. The interfaces/classes of JDBC API are all part of
> JRE System Library (JSL) itself.
>
> We only needed to get the JAR for the Database Driver (like MySQL Connector J)
> externally. That is, the implementation of the JDBC specification, which is
> the JDBC Driver had to be taken externally. And the JDBC API itself and all
> the interfaces present in the JDBC API (`java.sql` and `javax.sql`) - these
> all are part of JRE System Library (JSL) and are readily available for usage
> without any external JAR(s).

Hibernate, being a third-party framework, which is outside the Java language,
i.e. outside JavaSE & JavaEE, which is developed by the Hibernate Community,
which is primarily led by Red Hat. Therefore, we definitely would require an
external JAR for Hibernate.

Hibernate is a collection of JARs working together.

These can be found at:

1. On mvnrepository.com page for [org.hibernate group-id](https://mvnrepository.com/artifact/org.hibernate).
1. On sourceforge.net where fully bundled releases are maintained for each
   versions. This is hosted at [sourceforge.net/projects/hibernate/](https://sourceforge.net/projects/hibernate/).

JPA is an API / Specification, i.e. mostly a set of interfaces; and one of the
modules of JavaEE Specification.

So, how to get JPA's interfaces in our project?

JavaEE Specification is implemented to different extents by various **Java
Application Servers**. Some Application Servers choose to implement a fair
number of JavaEE modules, while some implement only a selected very few JavaEE
modules, and some choose to implement the entire JavaEE specifications.

For working with Servlets & JSP, we picked Apache Tomcat v9.0.x as our choice of
Java Application Server. Tomcat implements only the Servlet/JSP modules of the
JavaEE Specification. Tomcat provides us the Servlet API interfaces, and also
their implementation code. So, Apache Tomcat, is a very light-weight Application
Server, which only implements the Servlet API, and not other parts of JavaEE.

Hence, Tomcat does not contain, *neither* the JPA API (i.e. the interfaces /
abstract classes / annotations for the JPA Specification), *nor* the
implementation of JPA API (for which we anyways are already going to use the
third-party Hibernate Framework).

Therefore, we will not rely on other Application Servers (like the
*Full JavaEE Servers* such as WildFly, TomEE, GlassFish, Payara, etc.), just
for getting the JPA API interfaces (since Tomcat is not providing them, and we
don't wish to use another Application Server for only working with JPA &
Hibernate).

Instead, we can just use an external JAR to get access to the JPA API
interfaces. We will use JPA API Version 2.2 from mvnrepository.com here:
[javax.persistence/javax.persistence-api/2.2](https://mvnrepository.com/artifact/javax.persistence/javax.persistence-api/2.2).

[sourceforge.net/projects/hibernate/files/hibernate-orm/5.6.5.Final/hibernate-release-5.6.5.Final.zip/download](https://sourceforge.net/projects/hibernate/files/hibernate-orm/5.6.5.Final/hibernate-release-5.6.5.Final.zip/download)


---

<br>

## Setting Up Hibernate + JPA Without Maven / Gradle


### The JPA API JAR (`javax.persistence-api-2.2.jar`)

The JPA 2.2 spec ships as a single, small (~160 KB) JAR containing only
interfaces, annotations, and abstract classes — `@Entity`, `@Id`,
`@Column`, `EntityManager`, `EntityManagerFactory`, and so on. It has
zero runtime behaviour on its own; it exists purely so your code
compiles against the standard JPA API.

**This JAR does not need to be downloaded separately** when setting up
Hibernate manually, because Hibernate's release ZIP already bundles it.
See the section below.

---

### The Hibernate Release ZIP — The Authoritative Manual Install

The Hibernate project publishes an official release ZIP on SourceForge
for each version. We will use Version 5.6.5.Final and its files (bundled as a
ZIP archive containing all JARs) on SourceForge are present here:
[sourceforge.net/projects/hibernate/files/hibernate-orm/5.6.5.Final/](https://sourceforge.net/projects/hibernate/files/hibernate-orm/5.6.5.Final/)

Or the direct ZIP download link on this page is this:
[sourceforge.net/projects/hibernate/files/hibernate-orm/5.6.5.Final/hibernate-release-5.6.5.Final.zip/download](https://sourceforge.net/projects/hibernate/files/hibernate-orm/5.6.5.Final/hibernate-release-5.6.5.Final.zip/download)

This ZIP is the canonical distribution for non-Maven setups. It bundles
`hibernate-core` together with every mandatory dependency, pre-resolved
and ready to add to a build path. The internal structure is:

```
hibernate-release-5.6.5.Final/
├── lib/
│   ├── required/      ← all mandatory JARs; add every file here
│   └── optional/      ← feature-specific extras; ignore for basics
├── documentation/
└── ...
```

The `lib/required/` folder contains approximately:

| JAR                                   | Purpose                               |
|---------------------------------------|---------------------------------------|
| `hibernate-core-5.6.5.Final.jar`      | The ORM engine; JPA impl + native API |
| `javax.persistence-api-2.2.jar`       | JPA 2.2 interfaces and annotations    |
| `jboss-logging-3.x.x.Final.jar`       | Logging facade used internally        |
| `byte-buddy-1.x.x.jar`                | Runtime proxy/bytecode generation     |
| `antlr-2.7.x.jar`                     | HQL query language parser             |
| `jandex-2.x.x.Final.jar`              | Annotation indexing                   |
| `classmate-1.x.x.jar`                 | Generic type resolution               |
| `hibernate-commons-annotations-*.jar` | Shared annotation utilities           |

All of these are required. Hibernate will not initialise if any is
missing from the classpath.

---

### The `org.hibernate` Group on mvnrepository — What to Ignore

The `org.hibernate` group on Maven Central lists many artifacts. Most
are optional extensions unrelated to core ORM functionality:

| Artifact              | What it is                                    | Needed?    |
|-----------------------|-----------------------------------------------|------------|
| `hibernate-core`      | ORM engine — JPA impl + native API            | ✅ Always |
| `hibernate-validator` | Bean Validation (JSR-380); a separate project | ℹ️ No     |
| `hibernate-hikaricp`  | HikariCP connection pool integration          | ℹ️ No     |
| `hibernate-c3p0`      | c3p0 connection pool integration              | ℹ️ No     |
| `hibernate-ehcache`   | Ehcache second-level cache integration        | ℹ️ No     |
| `hibernate-envers`    | Entity audit logging                          | ℹ️ No     |
| `hibernate-tools`     | IDE / schema reverse-engineering tooling      | ℹ️ No     |
| `hibernate-spatial`   | Geospatial data type support                  | ℹ️ No     |

For learning Hibernate ORM — both the JPA-style API and the native
`Session`/`SessionFactory` API — `hibernate-core` is the only Hibernate
artifact required. Everything else addresses specific advanced use cases.

The `lib/optional/` folder in the release ZIP mirrors these extras:
sub-folders for c3p0, Ehcache, etc. They can be ignored entirely during
a standard learning setup.

---

### Eclipse Build Path Setup

#### JARs required in total:

1. All JARs from `lib/required/` in the Hibernate release ZIP
2. The MySQL JDBC driver: `mysql-connector-java-x.x.x.jar`
   (download from https://dev.mysql.com/downloads/connector/j/)
   - We will get it from
     [com.mysql/mysql-connector-j/8.2.0](https://mvnrepository.com/artifact/com.mysql/mysql-connector-j/8.2.0)
     link on mvnrepository.com.

#### Steps in Eclipse:

1. Right-click the project →
   **Build Path** → **Configure Build Path**
2. **Libraries** tab → **Add External JARs…**
3. Select every JAR inside `lib/required/`
4. Repeat for the MySQL connector JAR
5. **Apply and Close**

This adds all JARs, i.e. in Hibernate zip bundle (inside its `lib/required/`
path) and also the MySQL Connector J's JAR all under one level, which will show
up under the **Referenced Libraries** section along-side other prominents items
in Package / Project Explorer in Eclipse like *JRE System Library [Adoptium-8]*
and *src/* Folder.

> Instead of directly putting all the JARs under Referenced Libraries by just
> using the "Add External JARs..." button in the project properties window
> option **Java Build Path** > **Libraries tab**.
> Instead, we can create two separate **User Library**'s, first that contains
> all JARs relevant to Hibernate & JPA, and second that contains the MySQL
> Connector J's JAR.
> These User Libraries in Eclipse can be used throughout projects in an Eclipse
> Workspace. For new workspaces, these might need to be created again.
> We will provide below custom names for these two User Libraries:
> 1. `hibernate-release-5.6.5--lib-required` for the User Library containing
>    all the Hibernate essential JARs.
> 1. `mysql-connector-8.2.0` for the User Library containing the JAR for MySQL
>    Connector J

#### Add JARs in separate User Libraries in Eclipse:

- Right-click the project, and select Properties from the context menu.
- From the Project Properties popup, select Java Build Path menu option from
the left side-bar.
- Then select the "Libraries" tab from the main section. This Libraries tab
represents the classpath.
- Now, the right-hand side has some buttons - select the "Add Library" button.
- This opens the "Add Library" popup. Select "User Library" from the list and
click Next.
- Now we see a list of previously created "User Libraries" - which can be empty
if we have not yet added any, or if it's a new Eclipse Workspace.
- If we have not previously created the two User Libraries that we discussed
about, then we will do that now.
- Click the "User Libraries..." button on the right-hand side.
- A new popup opens up. Here click the "New..." button from the right-hand side.
- Which opens a tiny dialog box "New User Library" - where we need to enter a
name for our library. Enter any name (the ones we discussed) and click "OK"
button. As discussed we will two User Libraries with these names & click "OK":
  1. `hibernate-release-5.6.5--lib-required`
  1. `mysql-connector-8.2.0`
- This creates a new entry with this name in the previous popup's list of
**"Defined user libraries:"** with the name(s) we provided in previous step.
- We should have entries for the two User Libraries items in this window.
- Select the newly created User Library item, one-by-one. (First perform the
further steps for the `hibernate-release-5.6.5--lib-required` User Library, and
then for the `mysql-connector-8.2.0` User Library.)
- And click on the "Add External JARs..." button from the right-hand side.
- Select the downloaded JAR from the file-picker.
  1. For `hibernate-release-5.6.5--lib-required` User Library: From the ZIP
     file download from SourceForge, select all the JAR files present in the path `hibernate-release-5.6.5.Final/lib/required`.
  1. For `mysql-connector-8.2.0` User Library: Select the JAR file
     `mysql-connector-j-8.2.0.jar` downloaded from
     [com.mysql/mysql-connector-j/8.2.0](https://mvnrepository.com/artifact/com.mysql/mysql-connector-j/8.2.0) location.
- Then click "Apply and Close" button at the bottom of the popup.
- Click "Finish" button from the previous popup.
- Finally, click the "Apply and Close" button from the original project
properties "Java Build Path" popup.

> [!INFO]
> Eclipse will resolve all JPA and Hibernate imports after this. No other
> JARs are needed for the duration of a Hibernate + JPA learning project.

#### Package Explorer after adding all JARs

Below is screenshot of Eclipse Package Explorer of a project, after creating
the two User Libraries, adding the respective JARs to them, and hooking the
Libraries up with the new Java Project.

<table align="center" border="1" cellpadding="8">
  <tr>
    <td align="center">
      <img src="assets/images/fig-02-HBN-JPA-MySQL-jars-Package-Explorer.png" 
           alt="Hibernate/JPA/MySQL JARs Added to Eclipse Java Project" 
           title="Hibernate/JPA/MySQL JARs Added to Eclipse Java Project" 
           width="760" height="588" loading="lazy" border="1">
      <br />
      <em>Figure 1: Hibernate/JPA/MySQL JARs Added to Eclipse Java Project</em>
    </td>
  </tr>
</table>
