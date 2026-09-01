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
