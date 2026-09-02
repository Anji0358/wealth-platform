# Test Strategy

## Purpose

This document is the entry point for the testing strategy of the wealth-platform project.

The testing strategy is designed to support:

- Test-Driven Development (TDD);
- financial correctness;
- deterministic tests;
- PostgreSQL-specific integration testing;
- transaction and rollback verification;
- concurrency experiments;
- idempotency verification;
- SQL correctness and performance learning;
- reproducible performance experiments;
- maintainable test structure.

The project explicitly treats SQL as a first-class learning target. Passing functional tests alone is not sufficient for important database behavior.

## Core Principles

1. Follow Red → Green → Refactor for new business behavior.
2. Keep most Business Rule tests as fast Domain Unit Tests.
3. Use PostgreSQL through Testcontainers for database integration tests.
4. Do not use H2 as a substitute for PostgreSQL behavior.
5. Test important financial flows end-to-end, but keep E2E coverage selective.
6. Test rollback and partial-failure behavior explicitly.
7. Reproduce race conditions before selecting concurrency mechanisms.
8. Treat SQL correctness and SQL performance as separate concerns.
9. Use `EXPLAIN (ANALYZE, BUFFERS)` and repeatable datasets for SQL experiments.
10. Separate performance experiments from normal fast test execution.
11. Treat a compile failure caused by a deliberately missing production type or signature as a valid intermediate Red state.
12. Introduce interfaces and Test Doubles only when the current test or implementation creates a concrete need for them.

## TDD Boundaries and Test Doubles

### Compiler Red and Behavioral Red

Java TDD may move through:

```text
Compiler Red
→ minimum cohesive compile-enabling slice
→ Behavioral Red, or Green when that slice already satisfies the test
→ minimum implementation
→ Green
```

`Compiler Red` means that the current test does not compile because a required class, constructor, method, or interface signature is missing.

The compile-enabling change may contain the smallest cohesive slice needed by the current test: the missing declaration and, when necessary, directly required fields and a trivial method body. It must not add behavior beyond the current test. If this makes the test Green, it is a valid Green outcome.

`Behavioral Red` means that the test compiles and fails because the behavior is absent or incorrect.

An unrelated compilation, configuration, or environment failure is not an acceptable Red and must be resolved before continuing.

### Production Interfaces

Defining an interface and creating its production implementation are separate decisions.

Do not define an interface merely because:

- it may be useful later;
- DDD or SOLID is being studied;
- a Repository is conventionally represented by an interface;
- a framework integration will eventually need one.

Prefer to let an interface emerge during Refactor when the current code reveals a concrete boundary, role, dependency-direction problem, or testability problem.

An interface may also be introduced during Red when the current behavior cannot be tested without a seam to an external dependency. In that case, define only the signature required by the current test. Do not create the production implementation in the same micro-task unless it is independently the current task.

The governing rule is not `interfaces are created only during Refactor`. It is:

```text
Do not introduce an interface before its current necessity can be explained from evidence.
```

### Test Doubles

Test Doubles are valid and common in TDD. They may be used to isolate a dependency or observe a collaboration without accessing the real database, clock, network, or other external mechanism.

Possible roles include:

- Dummy;
- Stub;
- Fake;
- Spy;
- Mock.

Introduce a Test Double only when the current test requires it. Implement only the behavior or observation needed by that one test.

Do not create a finished Fake, Spy, or Mock in anticipation of later tests. Do not add stored values, counters, verification methods, or branches that the current test does not use.

Use the simplest suitable form. A small handwritten Test Double is often preferable while learning because its role remains visible; a mocking framework may be used when it solves a present problem, not by default.

The test should verify externally meaningful behavior or collaboration. It should not become unnecessarily coupled to internal implementation steps.

## Documents

- [TDD and Test Levels](test-strategy/tdd-levels.md)
- [Domain and Application Tests](test-strategy/domain-application.md)
- [Database Integration and Testcontainers](test-strategy/database-integration.md)
- [API and End-to-End Tests](test-strategy/api-e2e.md)
- [Concurrency, Atomicity, and Idempotency](test-strategy/concurrency-idempotency.md)
- [SQL Correctness Strategy](test-strategy/sql-correctness.md)
- [SQL Performance and Query Plan Strategy](test-strategy/sql-performance.md)
- [SQL Experiments and Learning Tasks](test-strategy/sql-experiments.md)
- [Performance Testing](test-strategy/performance.md)
- [Fixtures, Determinism, and Test Quality](test-strategy/fixtures-quality.md)
- [Phase-by-Phase Test Plan](test-strategy/phases.md)

## Final Test Responsibility Map

```text
Domain Unit
→ Business Rules / Invariants / calculations

Application
→ Use Case orchestration

Repository Integration
→ PostgreSQL / mappings / constraints / migrations

API
→ HTTP contract / validation / error semantics

E2E Integration
→ cross-layer financial flows and atomicity

Concurrency
→ race conditions / locking / isolation / invariants

SQL Correctness
→ query result correctness

SQL Performance
→ execution plans / buffers / index effects

SQL Experiment
→ compare alternative query/transaction strategies

Performance
→ repeatable large-data benchmark comparison
```

Every Confirmed Business Rule must be assigned to at least one responsibility in this map before its implementation starts. Rules protected by a database constraint retain Domain-level tests when they also represent Domain behavior.

For financial operations, the Application or E2E responsibility must explicitly verify that balance, Ledger, and any Position / Order / Execution changes succeed or fail together.
