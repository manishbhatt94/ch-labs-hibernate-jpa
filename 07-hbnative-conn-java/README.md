# Hibernate Native API - Establish Connection using Pure Java approach (No XML)

In this project, we will try establishing connection to our locally running
MySQL RDBMS, using Hibernative Native API, and **Pure Java approach**, i.e.,
**without using `hibernate.cfg.xml` XML file**.

## (Bootstrap) Using Configuration class (considered Legacy in Hibernate 5.6)

As per [https://docs.hibernate.org/orm/5.6/topical/html_single/bootstrap/LegacyBootstrapping.html](https://docs.hibernate.org/orm/5.6/topical/html_single/bootstrap/LegacyBootstrapping.html),
using class `org.hibernate.cfg.Configuration` is considered a Legacy Approach
to **Bootstrap** Hibernative's Native Implementation.

In this project, and others as well, wherever we use Java based configuration for
Hibernative Native, we are however, using this approach only.

We can refer to the above link to check sample code, especially regarding the
different ways to provide Hibernate-Mapping information via either `*.hbm.xml`
files, or via an *Annotated Class*.

Here is the sample code which highlight basic usage of the
`org.hibernate.cfg.Configuration` class, which we're going to use here:

> You can obtain the `Configuration` by instantiating it directly. You then
> specify mapping metadata (XML mapping documents, annotated classes) that
> describe your applications object model and its mapping to a SQL database.
>
> If XML mapping files are in the classpath, use addResource(). For example:
> ```java
> Configuration cfg = new Configuration()
>     // addResource does a classpath resource lookup
>     .addResource("Item.hbm.xml")
>     .addResource("Bid.hbm.xml")
>
>     // calls addResource using "/org/hibernate/auction/User.hbm.xml"
>     .addClass(`org.hibernate.auction.User.class`)
>
>     // parses Address class for mapping annotations
>     .addAnnotatedClass( Address.class )
>
>     // reads package-level (package-info.class) annotations in the named package
>     .addPackage( "org.hibernate.auction" )
> ```
>
> `Configuration` also allows you to specify configuration properties. For example:
> ```java
> Configuration cfg = new Configuration()
>     .setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
>     .setProperty("hibernate.connection.datasource", "java:comp/env/jdbc/test")
>     .setProperty("hibernate.order_updates", "true");
> ```
>
> *Source: [docs.hibernate.org/orm/5.6/topical/html_single/bootstrap/LegacyBootstrapping.html#_usage](https://docs.hibernate.org/orm/5.6/topical/html_single/bootstrap/LegacyBootstrapping.html#_usage)*


## Newer way (it seems) to Bootstrap in Hibernate 5.6

Apparrently, the newer way to Bootstrap Hibernate 5.6 Native Implementation
(a.k.a. *Native Bootstrapping*) is briefly discussed in this documentation
article:
[docs.hibernate.org/orm/5.6/topical/html_single/bootstrap/NativeBootstrapping.html](https://docs.hibernate.org/orm/5.6/topical/html_single/bootstrap/NativeBootstrapping.html)

Not yet researched what this is all about currently, at all!
