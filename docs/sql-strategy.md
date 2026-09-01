# SQL Strategy

## Purpose

This document is the entry point for the SQL strategy of the wealth-platform project.

SQL is treated as a first-class engineering and learning concern. The project does not consider database access complete merely because a Repository test passes.

The SQL strategy covers:

- PostgreSQL-specific development;
- JPA / JPQL / native SQL / JdbcClient usage;
- schema and migration discipline;
- constraints;
- indexing;
- execution plans;
- advanced SQL;
- cursor pagination;
- transaction isolation;
- locking and concurrency;
- reconciliation;
- large-data experiments;
- SQL performance documentation.

## Core Principles

1. PostgreSQL is the target database.
2. ORM is useful, but SQL must remain visible and understandable.
3. Query technique is selected by the problem, not by one universal abstraction.
4. Database constraints reinforce important invariants.
5. Indexes are designed from actual access patterns and evidence.
6. Important queries are inspected with `EXPLAIN (ANALYZE, BUFFERS)`.
7. Correctness and performance are separate verification concerns.
8. Transaction and lock behavior is learned through reproducible experiments.
9. Denormalization and database-side business logic are introduced only when justified.
10. SQL experiments are complete only when the observed behavior can be explained.

## Documents

- [Data Access Strategy](sql-strategy/access-strategy.md)
- [Schema, Migrations, and Constraints](sql-strategy/schema-constraints.md)
- [Indexing Strategy](sql-strategy/indexing.md)
- [Execution Plans and Query Analysis](sql-strategy/query-plans.md)
- [Advanced SQL](sql-strategy/advanced-sql.md)
- [Transactions, Locks, and Concurrency](sql-strategy/transactions-locking.md)
- [PostgreSQL Operations and Diagnostics](sql-strategy/postgresql-operations.md)
- [SQL Experiments](sql-strategy/experiments.md)
- [Deferred and Future Techniques](sql-strategy/future-techniques.md)

## Learning Goal

```text
Write SQL
↓
Prove correctness
↓
Read the execution plan
↓
Design and justify indexes
↓
Compare query formulations
↓
Understand transactions and locks
↓
Preserve invariants under concurrency
↓
Explain performance trade-offs
```
