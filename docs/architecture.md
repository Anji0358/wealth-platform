# Architecture

## Purpose

This document is the entry point for the architecture of the wealth-platform project.

The architecture is designed to support:

- financial correctness;
- explicit responsibility separation;
- maintainability;
- TDD and fast unit testing;
- DDD-oriented modeling;
- incremental introduction of design patterns;
- PostgreSQL transactions and concurrency learning;
- SQL as a first-class engineering concern;
- controlled scope.

The project intentionally avoids microservices, Event Sourcing, full CQRS infrastructure, and speculative frameworks in the initial architecture.

## Core Architectural Decisions

1. **Modular Monolith**
2. **Package by Business Domain**
3. **Pure Java Domain Layer**
4. **Domain Model separated from JPA persistence model**
5. **Application Use Case as transaction boundary**
6. **Command-side domain modeling + dedicated Query Read Models**
7. **Single PostgreSQL database**
8. **No premature Event Bus / Microservices / Generic Framework**
9. **Design patterns introduced when real variation or complexity appears**
10. **Architecture rules remain testable and traceable**
11. **Development acting identity enters through a transport-agnostic ActorContext**

## Documents

- [Principles and Quality Goals](architecture/principles.md)
- [Modules and Layers](architecture/modules-layers.md)
- [Domain and Persistence Separation](architecture/domain-persistence.md)
- [Transactions and Repositories](architecture/transactions-repositories.md)
- [Query Side and Read Models](architecture/query-side.md)
- [Time, Randomness, and Scheduler](architecture/time-randomness.md)
- [Validation, Errors, and Dependency Injection](architecture/errors-di.md)
- [Concurrency, SQL, and Database Boundaries](architecture/concurrency-sql.md)
- [Design Patterns and Maintainability](architecture/patterns-maintainability.md)
- [Recommended Package Structure](architecture/package-structure.md)
- [Deferred Decisions and ADR Candidates](architecture/deferred-decisions.md)

## Dependency Direction

The default dependency direction is:

```text
presentation
      ↓
application
      ↓
domain

infrastructure
      ↓
domain / application ports
```

The Domain Layer must not depend on HTTP, Spring MVC, JPA, Docker, or infrastructure frameworks.

## Traceability

Architecture decisions should remain traceable from:

```text
Requirement
↓
Use Case
↓
Business Rule
↓
Architecture
↓
Implementation
↓
Test
```

Accepted architectural rationale is recorded in ADR-001, ADR-002, and ADR-003 under `adr/`.
