# Deferred Architecture Decisions and ADR Candidates

## Decisions Intentionally Deferred

### Concurrency Strategy

To be decided in Phase 5 after concurrency tests compare approaches such as:

- optimistic locking;
- pessimistic locking;
- atomic SQL;
- isolation-level changes.

The chosen strategy may differ by financial operation.

---

### Exact Aggregate Boundaries

Initial Aggregate candidates are documented, but boundaries may be revised when implementation reveals:

- overly large load graphs;
- transaction pressure;
- invariants crossing current boundaries;
- unnecessary coupling.

---

### Java Numeric Types

Database semantics are already defined in `data-model.md`.

Concrete Java choices such as `long` versus dedicated Money Value Object and `BigDecimal` usage will be finalized during implementation/architecture refinement.

---

### Persistence Mapping Details

Deferred:

- exact JPA annotations;
- fetch strategies;
- cascade rules;
- optimistic-lock columns;
- persistence constructors.

---

### SQL Access Strategy

`sql-strategy.md` will define where to prefer:

- Spring Data JPA;
- JPQL;
- native SQL;
- JdbcClient;
- specialized projections.

---

### Architecture Testing

ArchUnit may be added after packages stabilize.

---

## Initial ADR Candidates

### ADR-001 — Use Modular Monolith

Record:

- context;
- why microservices are not justified;
- benefits for local ACID transactions;
- future migration implications.

### ADR-002 — Separate Domain and Persistence Models

Record:

- why Domain Model remains framework-independent;
- mapping cost;
- testability benefit;
- JPA isolation benefit.

### Future ADR — Concurrency Control

Create only after Phase 5 experiments determine the preferred solution.

---

## Explicit Non-Decisions

The architecture does not currently adopt:

```text
Microservices
Event Sourcing
Full CQRS infrastructure
Kafka
Redis
Kubernetes
Message Queue
Distributed Transactions
Generic Repository Framework
Generic Base Entity hierarchy
```

These may be reconsidered only if a concrete requirement appears.
