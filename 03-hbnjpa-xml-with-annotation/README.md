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


