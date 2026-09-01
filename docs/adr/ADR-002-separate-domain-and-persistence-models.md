# ADR-002 — Separate Domain Models from Persistence Models

**Status:** Accepted  
**Date:** 2026-09-01

## Context

The wealth-platform project has significant Domain behavior around:

- account state transitions;
- financial invariants;
- Cash Balance rules;
- Position calculations;
- Order and Execution behavior;
- Ledger consistency;
- Market simulation.

The project also uses Spring Data JPA for relational persistence.

A common Spring application design is to annotate Domain Objects directly with JPA annotations and use the same class simultaneously as:

```text
Domain Model
+
Persistence Entity
```

This minimizes mapping code.

However, JPA introduces persistence-oriented concerns such as:

- default constructors;
- entity lifecycle;
- lazy loading;
- relationship ownership;
- cascade behavior;
- proxy behavior;
- persistence identity;
- fetch strategies;
- mutable collection requirements.

The project explicitly aims to learn DDD-oriented responsibility separation and to keep important Business Rules testable without Spring or a database.

If the same classes represent both persistence structure and Domain design, changes required by JPA may begin to shape Aggregate boundaries and Domain behavior.

## Decision

The project will separate:

```text
Pure Java Domain Model
```

from:

```text
JPA Persistence Model
```

Example:

```text
banking/domain/BankAccount.java
```

is conceptually separate from:

```text
banking/infrastructure/persistence/BankAccountJpaEntity.java
```

Infrastructure is responsible for mapping between the two representations.

Initial mapping is implemented manually.

Conceptually:

```text
PostgreSQL
↓
JPA Entity
↓ Mapper
Domain Object
```

and on persistence:

```text
Domain Object
↓ Mapper
JPA Entity
↓
PostgreSQL
```

The Domain Model should not require JPA annotations to execute its Business Rules.

## Alternatives Considered

### Alternative 1 — Domain Object Is Also the JPA Entity

Example:

```java
@Entity
public class BankAccount {
    ...
}
```

#### Advantages

- less mapping code;
- fewer classes;
- straightforward Spring Data integration;
- common pattern for simple CRUD systems.

#### Reasons Not Selected

The wealth-platform is not intended to be only a CRUD application.

The project has meaningful invariants and Aggregate behavior.

Combining the models would make it easier for:

- persistence annotations;
- lazy relationships;
- JPA collection behavior;
- entity lifecycle requirements

to influence the shape of the Domain Model.

It would also reduce the clarity of testing the Domain independently from persistence.

---

### Alternative 2 — Active Record Style

Domain objects would directly perform persistence operations.

Conceptually:

```text
bankAccount.save()
security.find(...)
```

#### Advantages

- simple persistence access;
- fewer repository abstractions;
- compact implementation for small systems.

#### Reasons Not Selected

This mixes:

```text
Business behavior
+
Persistence behavior
```

inside the same object.

It conflicts with the project's dependency-direction and responsibility-separation goals.

It would also make Domain Unit Tests more dependent on infrastructure concerns.

## Consequences

### Positive

#### Pure Domain tests

Important Business Rules can be tested with ordinary Java tests without:

```text
Spring Context
PostgreSQL
JPA
```

#### Persistence isolation

JPA-specific changes do not automatically require changes to Domain semantics.

#### Better Aggregate design freedom

Domain Objects can be designed around invariants rather than ORM convenience.

#### Explicit dependency direction

Infrastructure depends on Domain abstractions rather than Domain Objects depending on persistence frameworks.

#### Easier experimentation

The project can compare JPA, native SQL, and JdbcClient approaches without requiring the Domain Model itself to become a database representation.

### Negative / Trade-offs

#### More classes

The project may contain both:

```text
BankAccount
BankAccountJpaEntity
```

and similar pairs.

#### Mapping code

Values must be mapped between Domain and persistence representations.

This creates additional implementation and testing work.

#### Risk of mapping defects

Incorrect mapping can create bugs even when each model is individually valid.

Repository Integration Tests must cover this boundary.

#### Potential over-engineering for trivial models

Some simple records may not benefit strongly from complete separation.

The project should therefore avoid mechanically creating complex Domain models where no business behavior exists.

#### Refactoring cost

A field change may require updates in:

- Domain Object;
- Persistence Entity;
- Mapper;
- tests.

This cost is accepted because the project values Domain independence and explicit architecture.

## Implementation Guidance

Manual mapping is preferred initially.

Do not introduce MapStruct or another mapping framework before repetitive mapping becomes a measurable maintenance problem.

JPA-specific repositories remain inside Infrastructure.

Domain/Application layers should not directly depend on Spring Data interfaces.

## Related Documents

- `docs/data-model.md`
- `docs/architecture.md`
- `docs/architecture/domain-persistence.md`
- `docs/architecture/transactions-repositories.md`
- `docs/architecture/patterns-maintainability.md`
- `docs/test-strategy.md`
