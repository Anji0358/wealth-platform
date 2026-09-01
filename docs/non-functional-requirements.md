# Non-Functional Requirements

## Purpose

This document defines the non-functional requirements (NFRs) of the wealth-platform project.

The project is a learning-oriented financial platform. Its non-functional priorities therefore emphasize:

- financial correctness and data integrity;
- traceability and auditability;
- testability;
- maintainability;
- measurable performance;
- controlled scope.

The project does not attempt to emulate a production-scale financial institution or distributed trading platform.

Implementation mechanisms such as transaction annotations, locking strategies, connection-pool sizes, HTTP status codes, and database constraints are deferred to later design documents where appropriate.

---

## 1. Quality Priority

### NFR-001 — Quality Priority Order

**Status:** Confirmed  
**Phase:** All

The system prioritizes quality attributes in the following order:

```text
1. Financial Correctness / Data Integrity
2. Traceability / Auditability
3. Testability
4. Maintainability
5. Performance
6. Availability
```

When trade-offs are required, correctness and integrity take precedence over raw performance.

A faster implementation that can produce an invalid financial state is unacceptable.

---

## 2. Financial Correctness and Consistency

### NFR-002 — Financial Operations Must Be Atomic

**Status:** Confirmed  
**Phase:** Phase 1+

Financial operations must be all-or-nothing.

Examples include:

```text
Transfer
├── Source Balance
├── Destination Balance
└── Ledger

Buy
├── Order
├── Execution
├── Securities Cash
├── Position
└── Ledger
```

Partial success is prohibited.

The concrete transaction mechanism is deferred to `architecture.md`.

---

### NFR-003 — Domain Invariants Must Survive Concurrency

**Status:** Confirmed business guarantee  
**Implementation Focus:** Phase 5

Concurrent execution must not violate established domain invariants.

Examples include:

```text
Current Balance >= 0
Position Quantity >= 0
SUM(Ledger Entry Amounts) = 0
```

Concurrency-control implementation is deferred to later architecture and SQL design.

---

### NFR-004 — Asset-Moving Requests Must Support Idempotency

**Status:** Planned Future Scope  
**Phase:** Phase 5

Asset-moving operations must support idempotent request semantics.

Examples include:

- Deposit;
- Withdrawal;
- Bank transfer;
- Bank-to-Securities transfer;
- Securities-to-Bank transfer;
- Buy Order;
- Sell Order.

Same request + same Idempotency Key must not create a second asset movement.

Same Idempotency Key + different request must be rejected as a conflict.

The specific persistence strategy, header name, retention period, and uniqueness mechanism are deferred.

---

### NFR-005 — No Silent Data Corruption

**Status:** Confirmed  
**Phase:** All

If an integrity violation is detected, the system must fail explicitly rather than continue with a potentially corrupted state.

The system must not silently ignore:

- invalid balances;
- broken ownership relations;
- invalid Ledger state;
- duplicate financial effects;
- impossible state transitions.

---

### NFR-006 — Database Integrity Must Not Depend Only on Application Code

**Status:** Confirmed principle  
**Phase:** Phase 1+

When a critical invariant can reasonably be reinforced by the database, the design should consider database-level protection in addition to application validation.

Candidate mechanisms include:

```text
NOT NULL
FOREIGN KEY
UNIQUE
CHECK
```

The exact constraints are deferred to `data-model.md` and `sql-strategy.md`.

---

## 3. Auditability and Historical Integrity

### NFR-007 — Financial Operations Must Be Traceable

**Status:** Confirmed  
**Phase:** Phase 1+

Important financial operations must be traceable to their business records.

Typical traceability includes:

```text
Business Operation
↓
Ledger Transaction
↓
Ledger Entries
```

and, for securities trading:

```text
Order
↓
Execution
↓
Cash / Position Effects
↓
Ledger
```

---

### NFR-008 — Historical Financial Facts Are Immutable by Normal Operations

**Status:** Confirmed  
**Phase:** Phase 1+

The following completed historical facts must not normally be edited or deleted:

- Ledger Transaction;
- Ledger Entry;
- Execution;
- persisted Market Price;
- completed Order history.

Corrections are represented by additional facts rather than destructive rewriting.

---

### NFR-009 — Ledger Corrections Must Preserve Audit Trail

**Status:** Confirmed  
**Phase:** Later correction scope

Ledger correction must preserve:

- the original transaction;
- the Reversal Transaction;
- any subsequent corrected transaction.

A correction workflow must remain auditable from original event to correction.

---

## 4. Determinism and Testability

### NFR-010 — Non-Deterministic Inputs Must Be Test-Controlled

**Status:** Confirmed  
**Phase:** All

External or non-deterministic inputs that affect domain behavior must be controllable in automated tests.

Examples include:

```text
Randomness
Current Date / Time
Market Simulation Inputs
```

The exact Java abstractions are deferred to `architecture.md`.

---

### NFR-011 — Important Business Rules Must Be Automatically Testable

**Status:** Confirmed  
**Phase:** All

Important business behavior must not depend solely on manual verification.

High-priority automated test targets include:

- Balance rules;
- Ledger invariants;
- State transitions;
- Trading behavior;
- Average Cost calculations;
- GBM calculations;
- Concurrency behavior;
- Idempotency.

Test-layer responsibilities are defined later in `test-strategy.md`.

---

### NFR-012 — 100% Code Coverage Is Not a Goal

**Status:** Confirmed  
**Phase:** All

Code coverage is treated as a supporting metric, not a success criterion.

Testing priority is:

```text
Business Rules
Invariants
Failure Cases
Boundary Conditions
Concurrency Problems
```

High coverage without meaningful business-rule verification is insufficient.

---

## 5. Development Reproducibility

### NFR-013 — Development Environment Must Be Reproducible

**Status:** Confirmed  
**Phase:** Phase 1

The development environment must be reconstructable from the repository and documented setup steps.

The initial environment is:

```text
Spring Boot  → Local JVM
PostgreSQL   → Docker Compose
```

Manual machine-specific setup should be minimized.

---

### NFR-014 — Database Schema Changes Must Be Migrated

**Status:** Confirmed principle  
**Phase:** Phase 1+

Database schema evolution must be managed through versioned migration files.

The repository should be sufficient to reconstruct the schema from an empty database.

A migration tool such as Flyway is expected, with final tool selection defined in the development-environment or architecture specification.

---

### NFR-015 — Manual Database Editing Is Not a Normal Business Workflow

**Status:** Confirmed  
**Phase:** All

Direct SQL modification of financial data is not treated as the normal way to perform business corrections.

For example, Ledger corrections must use business-defined correction behavior rather than ad hoc manual `UPDATE` statements.

SQL consoles remain acceptable for:

- learning;
- inspection;
- debugging;
- controlled development analysis.

---

## 6. Query and Data-Access Performance

### NFR-016 — Growing Histories Require Pagination

**Status:** Confirmed  
**Phase:** Phase 1+

The system must not assume that historical datasets can always be returned in one unbounded response.

Pagination is required for growing history resources such as:

- Bank transaction history;
- Ledger history;
- Orders;
- Executions;
- Market Price history.

Pagination style and page-size defaults are deferred to `api-spec.md`.

---

### NFR-017 — Large Histories Must Not Be Loaded Unbounded into Memory

**Status:** Confirmed  
**Phase:** Phase 1+

The application should avoid approaches equivalent to:

```java
findAll()
```

followed by unrestricted in-memory filtering or aggregation for large historical datasets.

Where appropriate, use database-side:

- filtering;
- aggregation;
- sorting;
- pagination.

This requirement supports the project's intermediate-to-advanced SQL learning goals.

---

### NFR-018 — SQL Performance Must Be Observable

**Status:** Confirmed  
**Phase:** Phase 6

Important queries must be inspectable with tools such as:

```text
EXPLAIN
EXPLAIN ANALYZE
```

Indexes should be introduced based on observed query behavior and access patterns rather than assumption alone.

---

### NFR-019 — Performance Improvements Must Be Measured

**Status:** Confirmed  
**Phase:** Phase 5-6

Performance work must compare behavior under controlled conditions.

A useful comparison holds constant:

```text
Hardware
Dataset
Query / Workload
Application Version Context
```

before and after an optimization.

---

## 7. Performance Test Dataset

### NFR-020 — Synthetic Data Must Be Large Enough to Reveal Query Behavior

**Status:** Confirmed learning target  
**Phase:** Phase 5-6

The project should support generation of a synthetic dataset large enough to make query plans, pagination, and indexes meaningful.

Recommended target scale:

```text
Customers             ≈ 10,000
Bank Accounts          tens of thousands
Securities             ≈ 100
Ledger Entries         ≈ 1,000,000
Orders / Executions    hundreds of thousands where useful
Market Prices          multiple years
```

These are learning targets, not production-capacity requirements.

---

## 8. Performance Goals

### NFR-021 — Performance Targets Are Benchmarks, Not Production SLAs

**Status:** Confirmed  
**Phase:** Phase 5-6

The project does not claim production-grade SLAs such as:

```text
99.99% availability
1000 TPS
multi-region latency guarantees
```

Instead, performance targets are benchmark goals used to detect regressions and compare design alternatives.

---

### NFR-022 — Representative Response-Time Goals

**Status:** Confirmed benchmark target  
**Phase:** Phase 5-6

Representative local/development benchmark goals are:

```text
Simple single-Account read
→ within a few hundred milliseconds

Normal financial Command
→ within a few hundred milliseconds

Paginated history Query
→ approximately within 1 second
```

These values are not contractual production SLAs.

They may be refined after actual measurement.

---

## 9. Resource Safety

### NFR-023 — Finite Resources Must Be Bounded

**Status:** Confirmed  
**Phase:** Phase 1+

Database connections, transactions, threads, and similar finite resources must not be consumed without bounds.

Concrete configuration values such as connection-pool size are deferred to architecture and environment documents.

---

## 10. Observability

### NFR-024 — Important Failures Must Be Observable

**Status:** Confirmed  
**Phase:** Phase 1+

Important processing failures must be visible through logs or equivalent diagnostic output.

Examples include:

- Transfer failure;
- Market Price generation failure;
- Concurrency conflict;
- database failure;
- invalid state transition.

Errors must not be silently swallowed.

---

### NFR-025 — Logs Should Include Useful Business Identifiers

**Status:** Confirmed  
**Phase:** Phase 1+

When useful for diagnosis, logs should include relevant identifiers such as:

- Customer ID;
- Account ID;
- Order ID;
- Ledger Transaction ID;
- Security ID.

Sensitive values or complete request payloads must not be logged indiscriminately.

---

## 11. Security and Input Safety

### NFR-026 — Secrets Must Not Be Committed to the Repository

**Status:** Confirmed  
**Phase:** Phase 1+

Secrets such as:

```text
Database password
API secret
Private key
```

must not be committed directly into source-controlled configuration.

Secrets and ordinary configuration must be separated.

---

### NFR-027 — External Input Must Be Validated

**Status:** Confirmed  
**Phase:** Phase 1+

External input is not trusted by default.

Application-boundary validation should cover syntax-oriented concerns such as:

- required fields;
- nullability;
- format;
- basic range checks.

Business validation remains distinct and is governed by Business Rules.

---

### NFR-028 — SQL Injection Resistance Is Required

**Status:** Confirmed  
**Phase:** Phase 1+

User-controlled values must not be inserted into SQL by unsafe string concatenation.

Database access must use safe parameter binding or equivalent mechanisms.

Dynamic SQL experiments must preserve input safety.

---

### NFR-029 — Production-Grade Authentication Is Outside Initial Scope

**Status:** Confirmed  
**Phase:** Initial scope

The initial system does not require:

- OAuth/OIDC;
- MFA;
- enterprise authorization infrastructure;
- KYC;
- AML.

However, domain ownership rules remain mandatory.

For example, Customer A must not be able to operate Customer B's account through ordinary Customer operations.

---

## 12. Availability and Recovery

### NFR-030 — High Availability Is Not an Initial Requirement

**Status:** Confirmed  
**Phase:** Initial scope

The initial deployment may consist of:

```text
1 Spring Boot Application
1 PostgreSQL instance
```

The initial project does not require:

- multi-region deployment;
- automatic database failover;
- database replication;
- Kubernetes;
- distributed transactions.

These technologies must not be introduced solely to imitate production complexity.

---

### NFR-031 — Committed Financial State Must Survive Application Restart

**Status:** Confirmed  
**Phase:** Phase 1+

Application restart must not lose committed:

- account state;
- balances;
- Ledger history;
- Orders and Executions;
- Market Prices;
- Positions.

Application memory must not be the sole source of truth for committed financial state.

---

### NFR-032 — Scheduler Failure Must Be Recoverable

**Status:** Confirmed  
**Phase:** Phase 2+

If Market Price generation is interrupted, the system must be able to recover missing Business Days using the Catch-Up rules defined in `market-simulation.md`.

A transient scheduler failure must not permanently corrupt or skip the price path.

---

## 13. Maintainability and Design Discipline

### NFR-033 — Domain Responsibilities Must Remain Clear

**Status:** Confirmed  
**Phase:** All

Business Rules should not become arbitrarily scattered across:

- Controllers;
- SQL queries;
- persistence mappings;
- unrelated infrastructure code.

The concrete architecture and layer boundaries are defined later in `architecture.md`.

---

### NFR-034 — Avoid Premature Abstraction

**Status:** Confirmed  
**Phase:** All

The project must not introduce abstraction solely because it might be useful someday.

Examples include premature introduction of:

```text
Strategy
Factory
Event Bus
Microservice
Generic Framework
```

A pattern or abstraction should be introduced when actual variability, duplication, or complexity makes it useful.

This supports learning design patterns through real refactoring pressure.

---

### NFR-035 — Documentation Must Support Traceability

**Status:** Confirmed  
**Phase:** All

The project should preserve traceability where practical:

```text
Requirement
↓
Use Case
↓
Business Rule
↓
Design
↓
Test
```

Existing identifiers such as UC IDs and BR IDs should be maintained to support this relationship.

---

## 14. Error Semantics

### NFR-036 — Error Categories Must Remain Distinguishable

**Status:** Confirmed  
**Phase:** Phase 1+

The system should distinguish meaningful categories such as:

- input validation failure;
- Business Rule violation;
- invalid state transition;
- not found;
- idempotency conflict;
- concurrency conflict;
- infrastructure/database failure.

The concrete Exception classes and HTTP status mappings are deferred to `architecture.md` and `api-spec.md`.

---

## 15. Scope Discipline

### NFR-037 — Non-Functional Requirements Must Not Cause Uncontrolled Scope Expansion

**Status:** Confirmed  
**Phase:** All

The project must not automatically add infrastructure such as:

```text
Microservices
Kafka
Kubernetes
Redis
Distributed Cache
Message Queue
```

unless a concrete requirement or measured problem justifies it.

Learning goals remain centered on:

- Java / Spring;
- TDD;
- DDD-oriented design;
- design patterns;
- PostgreSQL;
- intermediate-to-advanced SQL;
- transactions;
- concurrency;
- data integrity.

---

## 16. Future Considerations

The following are not initial requirements and remain future candidates.

### Scalability / Availability

- Kubernetes;
- horizontal scaling;
- load balancing;
- read replicas;
- automatic failover;
- high availability;
- disaster recovery;
- multi-region deployment.

### Observability

- distributed tracing;
- metrics dashboard;
- alerting;
- centralized log aggregation.

### Security / Infrastructure

- production TLS termination;
- external Secrets Manager;
- rate limiting;
- production authentication/authorization infrastructure.

### Performance Architecture

- cache;
- distributed cache;
- message queue;
- asynchronous processing infrastructure.

These items should be promoted only when project scope or measured behavior justifies them.

---

## 17. Implementation Decisions Deferred

This document defines required quality characteristics, not implementation details.

The following are intentionally deferred:

### `architecture.md`

- transaction boundary placement;
- lock strategy;
- transaction isolation strategy;
- layer structure;
- time/randomness abstractions;
- connection-pool strategy.

### `data-model.md`

- table structure;
- persistence choices;
- exact integrity constraints;
- numeric storage types.

### `sql-strategy.md`

- index strategy;
- query tuning;
- execution-plan analysis;
- SQL-based aggregation strategies.

### `api-spec.md`

- pagination representation;
- HTTP status codes;
- error response schema;
- request/response conventions.

### `test-strategy.md`

- test pyramid;
- unit/integration boundaries;
- Testcontainers usage;
- concurrency-test strategy;
- performance-test execution.

### `development-environment.md`

- Docker Compose details;
- local environment setup;
- migration tooling;
- environment configuration.

---

## 18. Summary

The non-functional direction of wealth-platform is:

```text
Correctness before raw speed
Auditability before convenience
Testability as a design requirement
Measured performance instead of fictional production SLAs
Database and SQL as first-class engineering concerns
Explicit recovery instead of silent corruption
Controlled scope instead of premature infrastructure
```

The system should become more robust through measured problems and later-phase requirements rather than by introducing production-scale complexity prematurely.
