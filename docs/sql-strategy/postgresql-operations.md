# PostgreSQL Operations and Diagnostics

## SQL-PG-001 — ANALYZE Is Part of Planner Understanding

Learn how PostgreSQL statistics are collected and used by the optimizer.

---

## SQL-PG-002 — VACUUM and Autovacuum Are Conceptual Requirements

The application does not manually manage routine vacuum as part of normal business logic.

However, understanding Autovacuum is part of PostgreSQL competence.

---

## SQL-PG-003 — MVCC and Vacuum Are Studied Together

Understand that PostgreSQL updates create row versions and that maintenance affects how dead tuples are reclaimed.

---

## SQL-PG-004 — pg_stat_activity Is an Advanced Diagnostic Tool

Use `pg_stat_activity` during concurrency/locking experiments to inspect active sessions and query states.

---

## SQL-PG-005 — Lock Diagnostics Are Part of Concurrency Experiments

Learn how to inspect blocked sessions and lock relationships during controlled tests.

---

## SQL-PG-006 — pg_stat_statements Is an Advanced Learning Candidate

It may be enabled in a later development environment to study:

- call counts;
- cumulative execution time;
- average execution time;
- frequently executed statements.

---

## SQL-PG-007 — Slow Query Observation Is a Development Skill

The development environment should eventually support practical ways to observe slow SQL.

Exact PostgreSQL logging configuration belongs in `development-environment.md`.

---

## SQL-PG-008 — HikariCP Remains the Application Connection Pool

Spring's normal HikariCP-based pooling is retained.

Exact pool size is not fixed by this document.

---

## SQL-PG-009 — Larger Pool Is Not Automatically Faster

Connection-pool tuning must account for PostgreSQL capacity and workload.

Avoid treating pool size as a universal performance knob.

---

## SQL-PG-010 — Prepared Statements and Plan Caching Are Advanced Topics

They may be studied after core SQL/query planning is understood.

---

## SQL-PG-011 — Sequence Is Not the Main ID Strategy

UUID remains the project identifier strategy.

PostgreSQL sequences may still be studied as a general database concept.

---

## SQL-PG-012 — Batch Operations Are Valid Performance Experiments

Compare:

- one-row-at-a-time persistence;
- JDBC batch;
- ORM batching;

for synthetic data generation or safe bulk workloads.

---

## SQL-PG-013 — Bulk Insert Is Appropriate for Performance Dataset Generation

Synthetic million-row datasets should not require one individual HTTP-style operation per row.

Use efficient controlled bulk-generation techniques.

---

## SQL-PG-014 — Bulk Update/Delete Is Restricted for Financial History

Do not use large destructive bulk operations as ordinary financial business logic for immutable historical facts.
