# SQL Performance and Query Plan Strategy

This document defines how SQL performance is studied and verified.

The goal is not merely to write working SQL, but to understand how PostgreSQL executes it.

## TS-SQLP-001 — Important Queries Use EXPLAIN ANALYZE

For important query experiments, use:

```sql
EXPLAIN (ANALYZE, BUFFERS)
...
```

when safe and appropriate.

---

## TS-SQLP-002 — Query Plan Reading Is a Learning Requirement

At minimum, inspect:

```text
Seq Scan
Index Scan
Bitmap Scan where present
Actual Time
Estimated Rows
Actual Rows
Loops
Sort
Join Type
Buffers
```

---

## TS-SQLP-003 — Estimated vs Actual Rows Must Be Compared

Large differences between estimated and actual rows are a signal worth understanding.

Possible causes may include:

- statistics;
- data skew;
- predicate selectivity;
- correlation between columns.

---

## TS-SQLP-004 — Index Changes Require Before/After Evidence

Index experiments use:

```text
Before plan
Before timing/buffers
↓
Index change
↓
After plan
After timing/buffers
↓
Explanation
```

Do not add an index merely because a query "looks like it needs one."

---

## TS-SQLP-005 — Dataset Scale Is Tiered

Recommended datasets:

```text
Small   ≈ 1,000 rows
Medium  ≈ 100,000 rows
Large   ≈ 1,000,000+ rows
```

A query that looks fast on a tiny table may behave poorly at realistic learning scale.

---

## TS-SQLP-006 — Composite Index Column Order Is an Explicit Learning Topic

Example query:

```sql
WHERE ledger_account_id = ?
ORDER BY occurred_at DESC
```

The project should evaluate an index concept such as:

```text
(ledger_account_id, occurred_at)
```

and explain why that ordering helps the access pattern.

---

## TS-SQLP-007 — More Indexes Are Not Automatically Better

Measure write cost and storage impact where useful.

Experiments may compare:

```text
0 indexes
1 useful index
multiple extra indexes
```

for Ledger-heavy insert workloads.

---

## TS-SQLP-008 — Cursor vs Offset Pagination Is Compared

Compare:

```text
OFFSET / LIMIT
```

against cursor/keyset pagination at increasing history sizes.

Record both:

- correctness;
- execution plan;
- latency/buffers.

---

## TS-SQLP-009 — N+1 Is Measured, Not Assumed

Use SQL logging/query counts or integration-level observation to demonstrate:

```text
N+1
vs
JOIN / projection / dedicated SQL
```

before and after improvement.

---

## TS-SQLP-010 — Plan Regression Is More Stable Than Tight Time Thresholds

Avoid environment-sensitive rules such as:

```text
fail if > 50 ms
```

as the primary regression mechanism.

Prefer checking for suspicious plan changes such as:

- full-table scan of a very large history table when an index path is expected;
- unexpected repeated loops;
- expensive sort introduced by a query rewrite.

Wall-clock measurements remain useful as supporting evidence.

---

## TS-SQLP-011 — Performance Tests Are Not Part of Every mvn test Run

Large-data SQL experiments must be separately invocable from fast TDD tests.

---

## TS-SQLP-012 — Performance Dataset Is Reproducible

Record:

- row counts;
- generation seed;
- date range;
- distribution assumptions;
- indexes present.

Performance results are meaningful only when the dataset is reproducible.
