# SQL Tools and Performance Environment

## DEV-SQL-001 — psql Is the Primary SQL CLI

Use PostgreSQL `psql` for direct SQL learning and experiments.

---

## DEV-SQL-002 — SQL Must Be Inspectable Outside ORM

Developers should be able to execute and study SQL directly against PostgreSQL rather than relying only on Hibernate output.

---

## DEV-SQL-003 — EXPLAIN ANALYZE Is a Standard Experiment Tool

Use:

```sql
EXPLAIN (ANALYZE, BUFFERS)
...
```

for controlled important-query experiments.

---

## DEV-SQL-004 — Two-Terminal Transaction Experiments Are Recommended

For transaction/locking learning:

```text
Terminal A → psql Session A
Terminal B → psql Session B
```

This makes blocking, visibility, and isolation behavior directly observable.

---

## DEV-SQL-005 — GUI Database Tools Are Optional

Tools such as DBeaver may be used as supplementary interfaces.

They are not required and should not replace understanding of SQL/psql.

---

## DEV-SQL-006 — Performance Dataset Is Isolated

Large synthetic datasets belong in the performance database or equivalent isolated environment.

---

## DEV-SQL-007 — pg_stat_activity Is a Later Diagnostic Tool

Concurrency/lock experiments may inspect:

```text
pg_stat_activity
```

to understand active sessions and waiting behavior.

---

## DEV-SQL-008 — pg_stat_statements Is an Advanced Optional Extension

Enable later when aggregate query statistics become part of the SQL-learning phase.

It is not required for Phase 1 setup.

---

## DEV-SQL-009 — Slow Query Logging Is a Later Development Capability

PostgreSQL logging may be configured during performance work to surface slow queries.

Keep verbose logging off during normal TDD unless needed.

---

## DEV-SQL-010 — Query Experiments Are Documented

Results belong under:

```text
docs/sql-experiments/
```

with dataset, plan, index, result, and interpretation.

---

## DEV-SQL-011 — HikariCP Is the Application Pool

Use Spring Boot's normal HikariCP integration.

Do not aggressively tune pool size during initial development.

---

## DEV-SQL-012 — Pool Tuning Requires Measurement

If pool tuning is studied later, compare:

- request concurrency;
- PostgreSQL connections;
- lock behavior;
- throughput;
- DB saturation.

More connections are not automatically better.
