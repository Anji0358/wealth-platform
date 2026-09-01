# Domain and Persistence Separation

## ARCH-016 — Domain Layer Is Pure Java

**Status:** Confirmed

Domain classes should normally avoid Spring annotations such as:

```text
@RestController
@Service
@Repository
@Component
```

The Domain Model should run in ordinary Java unit tests without a Spring ApplicationContext.

---

## ARCH-017 — Domain Model and JPA Model Are Separate

**Status:** Confirmed

Recommended separation:

```text
domain.BankAccount

infrastructure.persistence.BankAccountJpaEntity
```

Mapping occurs between persistence representation and domain representation.

### Rationale

This prevents JPA concerns from dictating:

- aggregate shape;
- lazy-loading behavior;
- inheritance choices;
- constructors;
- domain invariants;
- collection loading strategies.

---

## ARCH-018 — Persistence Mapping Is Manual Initially

**Status:** Confirmed

Mapping such as:

```text
toDomain()
toEntity()
```

is implemented manually at first.

MapStruct or another mapping framework should be considered only when actual repetitive mapping cost justifies it.

---

## ARCH-019 — Do Not Introduce JPA Inheritance for Account Similarity

**Status:** Confirmed

Bank Account and Securities Account remain separate concepts.

Do not create a shared persistent `Account` superclass solely because both have:

- ID;
- status;
- balance;
- timestamps.

Shared attributes do not imply shared business abstraction.

---

## ARCH-020 — No Generic Base Entity

**Status:** Confirmed

Avoid a forced inheritance structure such as:

```java
abstract class BaseEntity {
    UUID id;
    Instant createdAt;
}
```

for all domain/persistence entities unless a concrete shared behavior later justifies it.

---

## ARCH-021 — Domain Behavior Must Not Depend on Lazy Loading

**Status:** Confirmed

A Domain operation should not accidentally trigger persistence access because a JPA relationship was lazily loaded.

Required state should be explicitly loaded by the Application/Repository boundary.

---

## ARCH-022 — Persistence Details May Evolve Without Changing Public Domain Concepts

**Status:** Confirmed

Database structure, JPA mapping, indexing, and fetch strategies may change without requiring changes to the meaning of:

- BankAccount;
- SecuritiesAccount;
- Position;
- LedgerTransaction;
- Order;
- Execution.
