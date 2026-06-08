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
