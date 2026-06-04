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

**JARs required in total:**

1. All JARs from `lib/required/` in the Hibernate release ZIP
2. The MySQL JDBC driver: `mysql-connector-java-x.x.x.jar`
   (download from https://dev.mysql.com/downloads/connector/j/)
   - We will get it from
     [com.mysql/mysql-connector-j/8.2.0](https://mvnrepository.com/artifact/com.mysql/mysql-connector-j/8.2.0)
     link on mvnrepository.com.

**Steps in Eclipse:**

1. Right-click the project →
   **Build Path** → **Configure Build Path**
2. **Libraries** tab → **Add External JARs…**
3. Select every JAR inside `lib/required/`
4. Repeat for the MySQL connector JAR
5. **Apply and Close**

Eclipse will resolve all JPA and Hibernate imports after this. No other
JARs are needed for the duration of a Hibernate + JPA learning project.
