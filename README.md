# CH Labs -- Hibernate Framework

Eclipse IDE Workspace - with multiple Java projects to get comfortable with Java
RDBMS ORM-based persistence framework **HIBERNATE**.

Hibernate consists of two separate implementations of specifications:

1. **JPA Specification**: Hibernate provides implementation for the JPA (Java /
   Jakarta Persistence API) Specification.
1. **Hibernate Native API Specification**: Hibernate provides its own *native*
   specification (called as Hibernate's Native API) which is a super-set of JPA,
   and has additional features to what JPA specifies. Hibernate also provides
   the implementation of its Native API, in additional to JPA implementation.

Above two implementations are two separate ways of using Hibernate. We explore
both these ways in this Eclipse Workspace.

## When to use Hibernate's JPA Implementation vs Hibernate's Native API Implementation?

JPA Specification is implemented by multiple other frameworks apart from
Hibernate, for instance **Eclipse Link** also implements JPA Specification.

So, if we want to make sure our code-base is able to switch to an entirely
different JPA implementation framework away from Hibernate, then we must use
Hibernate's JPA Implementation, and write our code accordingly, using the
interfaces provided by JPA.

But, if we are sure that we won't be switching Frameworks, and will stick with
Hibernate Framework only; and we do want to leverage the additional features
that Hibernate's Native API provides on top of JPA, then we can consider using
Hibernate's Native API Implementation.
