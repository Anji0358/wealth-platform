# Execution Plans and Query Analysis

## SQL-PLAN-001 — EXPLAIN Is a Standard Development Tool

Important SQL should be inspectable with PostgreSQL `EXPLAIN`.

---

## SQL-PLAN-002 — Use EXPLAIN ANALYZE for Runtime Evidence

For controlled experiments, prefer:

```sql
EXPLAIN (ANALYZE, BUFFERS)
...
```

because it reports actual execution behavior.

---

## SQL-PLAN-003 — Core Plan Concepts Must Be Understood

At minimum, learn to identify and interpret:

```text
Seq Scan
Index Scan
Bitmap Scan
Nested Loop
Hash Join
Merge Join
Sort
Aggregate
```

---

## SQL-PLAN-004 — Estimated Rows and Actual Rows Are Compared

Compare planner estimates with actual rows.

Large disagreement is an investigation signal.

---

## SQL-PLAN-005 — Loops Matter

A cheap-looking plan node executed thousands of times may dominate the query.

Always inspect:

```text
loops
```

together with row counts and time.

---

## SQL-PLAN-006 — Buffers Show Database Work Beyond Wall-Clock Time

Use `BUFFERS` to understand page access.

This is often more informative than a single elapsed-time measurement.

---

## SQL-PLAN-007 — Wall-Clock Time Alone Is Not Enough

A query becoming faster once may be caused by:

- cache state;
- machine load;
- background activity.

Plan structure, row counts, and buffers should support the conclusion.

---

## SQL-PLAN-008 — Planner Statistics Are Part of Query Performance

Learn the role of:

```text
ANALYZE
statistics
data distribution
selectivity
```

in PostgreSQL planning.

---

## SQL-PLAN-009 — Query Rewrites Must Be Compared on the Same Dataset

When comparing:

```text
JOIN
Subquery
CTE
DISTINCT ON
Window Function
```

use the same data and equivalent semantics.

---

## SQL-PLAN-010 — Performance Regression Checks Avoid Fragile Tight Timing

Avoid making a development test fail because a query took 52ms instead of 50ms.

Prefer detecting meaningful plan regressions or comparing controlled benchmark distributions.

---

## SQL-PLAN-011 — Read Rows, Not Only Returned Rows

A query returning 50 rows after scanning 1,000,000 rows is materially different from a query reading near the required range.

Learning should include understanding how much work PostgreSQL performed.

---

## SQL-PLAN-012 — Explain Why the Planner Chose the Plan

An SQL experiment is incomplete if it only records:

```text
Index Scan is faster
```

The report should explain why PostgreSQL could use the access path and what trade-off it creates.
