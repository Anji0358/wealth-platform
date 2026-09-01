# Architecture Principles and Quality Goals

## ARCH-001 — Modular Monolith

**Status:** Confirmed

The initial application is a Modular Monolith:

```text
Spring Boot
+
Single Deployable Application
+
Single PostgreSQL Database
```

Microservices are not introduced.

### Rationale

The project's learning goals center on:

- Java / Spring;
- TDD;
- DDD-oriented design;
- design patterns;
- SQL;
- transaction boundaries;
- concurrency;
- financial consistency.

Distributed systems would add network, messaging, deployment, and distributed-consistency concerns that are not required for the current learning objectives.

---

## ARCH-002 — Responsibility Separation Is a First-Class Goal

**Status:** Confirmed

Architecture should make it clear which part of the system is responsible for:

- HTTP concerns;
- Use Case orchestration;
- Business Rules;
- persistence;
- database access;
- external scheduling;
- read-side query composition.

Responsibility should not be inferred from naming alone; dependency direction and package structure should reinforce it.

---

## ARCH-003 — Maintainability Over Accidental Simplicity

**Status:** Confirmed

The architecture may accept limited additional mapping or adapter code when it meaningfully improves:

- domain independence;
- testability;
- persistence isolation;
- future refactoring safety.

However, architecture should not add abstraction that has no present responsibility.

---

## ARCH-004 — Avoid Premature Abstraction

**Status:** Confirmed

Do not introduce patterns or frameworks merely because they may be useful later.

Examples that should not be introduced without an actual need:

```text
Event Bus
Microservice
Generic Repository
Generic Service
Base Entity hierarchy
Generic Framework
Domain Event infrastructure
```

Abstraction should follow demonstrated duplication, variability, or coupling.

---

## ARCH-005 — TDD-Friendly Architecture

**Status:** Confirmed

Important Domain behavior should be testable without starting Spring.

Examples include:

```text
BankAccountTest
LedgerTransactionTest
PositionTest
GBM calculation test
```

Spring integration tests are reserved for framework and integration boundaries.

---

## ARCH-006 — Financial Correctness Dominates Performance Optimization

**Status:** Confirmed

Architecture decisions must preserve business invariants before optimizing for latency or throughput.

No optimization is acceptable if it can allow:

- negative balances;
- negative Position quantity;
- partial Transfer state;
- Ledger imbalance;
- duplicate financial effects.
