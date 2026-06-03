# The Java Ecosystem — Where Does Everything Live?

> **Note to future self:** This section exists because past-me was confused
> about why some Java APIs are "just there" in Eclipse, while others need
> JARs added manually. Here's the mental model that made it click.

---

## The Big Picture — Three Layers of Java

Think of the Java world as three nested layers:

```
┌──────────────────────────────────────────────────────────┐
│                   Your Application Code                  │
├──────────────────────────────────────────────────────────┤
│     Implementations  (Hibernate, Tomcat, WildFly, …)     │
├──────────────────────────────────────────────────────────┤
│        Specs / APIs  (JavaSE / JavaEE / JPA / …)         │
└──────────────────────────────────────────────────────────┘
```

- **Specs/APIs** = interfaces + abstract contracts. Just blueprints.
- **Implementations** = actual working code that fulfils those blueprints.
- **Your code** = talks to the spec/API layer, stays portable.

---

## Layer 1 — JavaSE and the JRE System Library (JSL)

**JSL = JRE System Library.** It's the set of JARs that ship with the
JDK/JRE itself. When you create a project in Eclipse, it attaches the
JSL automatically — that's why you can use `String`, `ArrayList`,
`Thread`, etc. without downloading anything.

### What lives inside JSL (JavaSE)?

| Package(s)                      | What it gives you                      |
|---------------------------------|----------------------------------------|
| `java.lang`, `java.util`        | Core types — String, List, Map, …      |
| `java.io`, `java.nio`           | File and stream I/O                    |
| `java.net`                      | Sockets, URLs, basic HTTP              |
| `java.sql`, `javax.sql`         | **JDBC API** — interfaces like         |
|                                 | `Connection`, `Statement`,             |
|                                 | `ResultSet`, `DataSource`              |
| `java.security`, `javax.crypto` | Encryption, hashing                    |
| `java.util.logging`             | Basic logging API                      |
| `javax.xml.*`                   | XML parsing (SAX, DOM, StAX)           |
| `java.util.concurrent`          | Threads, Executors, Futures            |

**JDBC is part of JavaSE/JSL.** That is why `import java.sql.Connection`
just works in Eclipse with zero extra JARs. But JDBC is only the API —
it defines the interfaces. The *driver* (e.g., `mysql-connector-java.jar`)
is the implementation. You still add the driver JAR manually.

---

## Layer 2 — JavaEE: The "Enterprise" Extension

JavaEE (now called **Jakarta EE**) is a set of *additional* specs built
on top of JavaSE. These are **not** bundled with the JDK. They define
more APIs needed for building web and enterprise applications.

Think of it this way:

```
JavaSE  =  the language + stdlib (always present)
JavaEE  =  optional enterprise add-ons (you choose what you need)
```

### Key JavaEE Modules — Specs, Packages, and JARs

| Module            | What it does                        | API Package(s)               | Spec JAR (Maven)                  |
|-------------------|-------------------------------------|------------------------------|-----------------------------------|
| **Servlet / JSP** | Handle HTTP requests, render pages  | `jakarta.servlet.*`          | `jakarta.servlet-api`             |
| **JPA**           | ORM — map Java objects to DB tables | `jakarta.persistence.*`      | `jakarta.persistence-api`         |
| **EJB**           | Managed server-side components      | `jakarta.ejb.*`              | `jakarta.ejb-api`                 |
| **JTA**           | Distributed transactions            | `jakarta.transaction.*`      | `jakarta.transaction-api`         |
| **JMS**           | Messaging between apps              | `jakarta.jms.*`              | `jakarta.jms-api`                 |
| **JAX-RS**        | RESTful web services                | `jakarta.ws.rs.*`            | `jakarta.ws.rs-api`               |
| **CDI**           | Dependency injection                | `jakarta.inject.*`           | `jakarta.inject-api`              |
| **Bean Validation** | Validate objects via annotations  | `jakarta.validation.*`       | `jakarta.validation-api`          |
| **JSF**           | Component-based UI framework        | `jakarta.faces.*`            | `jakarta.faces-api`               |

> **Note:** Older tutorials (pre-2019) use `javax.*` packages — e.g.
> `javax.persistence.*`, `javax.servlet.*`. After the rename to
> Jakarta EE, those became `jakarta.*`. Same concept, different prefix.
> The tutorial you're watching likely uses `javax.*` since it's Java 8
> era. Don't let the naming swap confuse you.

---

## Application Servers — Who Provides the Implementations?

A JavaEE spec is just a bunch of interfaces. Somebody has to implement
them. That's what **application servers** do.

### Lightweight — Servlet Containers Only

**Apache Tomcat** only implements the Servlet + JSP spec.
That's it. Nothing else from JavaEE.

```
Tomcat provides:
  ✅ Servlet API  (jakarta.servlet.*)
  ✅ JSP API      (jakarta.servlet.jsp.*)
  ❌ JPA          — you bring this yourself
  ❌ EJB          — not supported
  ❌ JMS          — not supported
  ❌ CDI (full)   — not supported
```

That's why when you used Tomcat with Eclipse Dynamic Web Project, the
Servlet + JSP classes were available — Tomcat's JARs were on the
classpath. But for anything else (like a database layer), you had to
add the JDBC driver yourself.

### Full JavaEE Servers — The "Heavy" Ones

These implement the *entire* JavaEE spec suite:

| Server          | Vendor            | Notes                              |
|-----------------|-------------------|------------------------------------|
| **WildFly**     | Red Hat           | Open-source; was JBoss AS          |
| **JBoss EAP**   | Red Hat           | Enterprise version of WildFly      |
| **GlassFish**   | Eclipse Foundation| Original JavaEE reference impl     |
| **TomEE**       | Apache            | Tomcat + the rest of JavaEE        |
| **Payara**      | Payara Ltd        | Fork of GlassFish, production-ready|
| **Liberty**     | IBM               | IBM's enterprise server            |

When you deploy to WildFly or GlassFish, **all** the JavaEE JARs are
already on the server. You still need those JARs present while writing
and compiling your code in Eclipse — but you do NOT want to bundle them
into your deployable (WAR/EAR), because the server already has them.

> **What is "provided" scope?**
> In Maven, every dependency has a *scope* that controls when the JAR
> is available. `compile` scope (the default) means: use this JAR
> during compilation AND bundle it into the final output. `provided`
> scope means: use this JAR during compilation, but do NOT bundle it —
> someone else (the JDK, or the app server) will provide it at runtime.
> Example: Servlet API on Tomcat. Tomcat ships `servlet-api.jar`
> internally. If you also bundle it in your WAR, you get two conflicting
> copies at runtime. So you mark it `provided` — Eclipse/Maven sees it
> while you type code, but it never ends up inside the WAR.

---

## JPA — The ORM Spec

JPA = **Java Persistence API**. It lives in the `jakarta.persistence`
package (or `javax.persistence` in older versions).

Key interfaces/annotations you'll use:

```
javax.persistence.EntityManager      — your main handle to the DB
javax.persistence.EntityManagerFactory
javax.persistence.Persistence        — bootstrap class
javax.persistence.Query
@Entity, @Table, @Id, @Column        — mapping annotations
@OneToMany, @ManyToOne, etc.         — relationship annotations
```

JPA is a spec. It ships as a thin JAR with **only interfaces** and
annotations. By itself it does nothing. You need an implementation.

### JPA Implementations

| Implementation  | Vendor         | Maven Artifact                   |
|-----------------|----------------|----------------------------------|
| **Hibernate ORM** | Red Hat      | `hibernate-core`                 |
| **EclipseLink** | Eclipse        | `eclipselink`                    |
| **OpenJPA**     | Apache         | `openjpa`                        |

Hibernate is by far the most popular. It is the reference implementation
of JPA, and it also predates JPA — that's why it has its *own native API*
on top of the JPA standard.

---

## Hibernate — Two APIs in One Library

This is the key thing that confused me at first. Hibernate gives you two
ways to use it:

```
┌─────────────────────────────────────────────────────┐
│                  hibernate-core.jar                 │
│                                                     │
│  ┌──────────────────────┐  ┌──────────────────────┐ │
│  │   JPA Implementation │  │  Hibernate Native API│ │
│  │  javax.persistence.* │  │  org.hibernate.*     │ │
│  │  (standard)          │  │  (Hibernate-specific)│ │
│  └──────────────────────┘  └──────────────────────┘ │
└─────────────────────────────────────────────────────┘
```

- **JPA way:** Use `EntityManager`, `EntityManagerFactory`, etc. from
  `javax.persistence.*`. This is portable — swap Hibernate for
  EclipseLink tomorrow and your code still compiles.

- **Hibernate Native way:** Use `Session`, `SessionFactory`, etc. from
  `org.hibernate.*`. More features, but locks you to Hibernate.

The tutorial teaches both. Start with JPA style to understand the
standard, then look at the native API to see what extra power it adds.

---

## What JARs Do You Actually Need?

### Minimal setup — Hibernate + JPA + MySQL (Maven)

```xml
<!-- JPA API spec — the interfaces -->
<dependency>
    <groupId>javax.persistence</groupId>
    <artifactId>javax.persistence-api</artifactId>
    <version>2.2</version>
</dependency>

<!-- Hibernate — the implementation of the above JPA spec,
     ALSO includes its own native API (org.hibernate.*) -->
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>5.6.15.Final</version>
</dependency>

<!-- MySQL JDBC Driver — Hibernate talks to MySQL through this -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

> **Note:** `hibernate-core` already bundles the JPA API internally.
> In many tutorials you'll see only `hibernate-core` listed — and that
> is fine. Adding the `javax.persistence-api` dependency explicitly is
> harmless and makes the dependency chain more readable.

### Minimal setup — Hibernate + JPA + MySQL (without Maven)

If you're not using Maven or Gradle, you download the JARs manually and
add them to your project's build path in Eclipse.

**Step 1 — Download Hibernate ORM**

Go to https://hibernate.org/orm/releases/ and download the release ZIP
for Hibernate 5.x (e.g. 5.6.15.Final). Extract it. Inside the `lib/`
folder you'll find:

```
lib/required/          ← these are the mandatory JARs
lib/optional/          ← add only what extra features you need
```

Add **all** JARs from `lib/required/` to your Eclipse project's build
path. That folder contains `hibernate-core-5.x.x.Final.jar` and its
dependencies (like `jboss-logging`, `byte-buddy`, `antlr`, etc.).

> The JPA API JAR (`hibernate-jpa-2.1-api-*.jar` or similar) is also
> included inside the Hibernate distribution — you do not need to
> download it separately.

**Step 2 — Download MySQL JDBC Driver**

Go to https://dev.mysql.com/downloads/connector/j/ and download the
platform-independent ZIP. Extract it and grab
`mysql-connector-java-x.x.x.jar`.

**Step 3 — Add JARs to Eclipse Build Path**

1. Right-click your project → **Build Path** → **Configure Build Path**
2. Go to the **Libraries** tab → click **Add External JARs…**
3. Select all the JARs from Hibernate's `lib/required/` folder
4. Also add the MySQL connector JAR
5. Click **Apply and Close**

Eclipse will now resolve all Hibernate and JPA imports correctly.

### Why no Servlet JAR here?

Because this is a standalone Java SE application talking to a database.
Servlets are only needed when there's a web layer. Hibernate works
fine without Tomcat or any server.

---

## The Classpath Mental Model — Quick Summary

| What you want to use        | Where it comes from         | How you get it                   |
|-----------------------------|-----------------------------|----------------------------------|
| `String`, `List`, JDBC      | JSL (JRE System Library)    | Automatic — part of JDK          |
| MySQL JDBC driver           | MySQL / Oracle              | Download JAR, add to build path  |
| Servlet / JSP API           | Tomcat (at runtime)         | Tomcat provides it at runtime;   |
|                             |                             | in Maven use `provided` scope    |
| JPA interfaces              | `javax.persistence-api` JAR | Download JAR, add to build path  |
| Hibernate (JPA impl + native API) | `hibernate-core` JAR  | Download ZIP, add `lib/required/`|
| Full JavaEE (all modules)   | WildFly / GlassFish / TomEE | Deploy to server; it ships all   |

---

## One-Line Takeaways

- **JSL** = JARs bundled with the JDK. JDBC is here. Servlet/JPA are not.
- **JavaEE** = extra specs for enterprise apps. Not in the JDK. You bring
  them via Maven or they come from an app server.
- **JPA** = just interfaces. Lives in `javax.persistence.*`. Does nothing
  alone.
- **Hibernate** = implements JPA + adds its own `org.hibernate.*` API.
  One JAR (`hibernate-core`) gives you both.
- **Tomcat** = implements only Servlet/JSP. Lightweight. Good for web
  apps that manage their own DB layer.
- **WildFly/GlassFish/TomEE** = implement all of JavaEE. Heavy. Good when
  you want the server to manage transactions, messaging, injection, etc.
