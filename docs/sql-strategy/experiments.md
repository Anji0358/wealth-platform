# SQL Experiments

## Purpose

SQL experiments turn database behavior into explicit engineering evidence.

Experiment reports should be stored under:

```text
docs/sql-experiments/
```

## Report Template

Each experiment should record:

```text
Experiment ID / Title

Question

Related Use Case / Business Rule

Dataset
- row counts
- distributions
- seed
- indexes

Baseline SQL

Baseline EXPLAIN (ANALYZE, BUFFERS)

Observation

Alternative SQL / Index / Transaction Strategy

New EXPLAIN (ANALYZE, BUFFERS)

Result

Explanation

Trade-offs

Conclusion
```

---

## EXP-SQL-001 — Ledger History Index

Compare account-history lookup before and after a suitable composite index.

Study:

- filtering;
- sort;
- LIMIT;
- buffers;
- write cost.

---

## EXP-SQL-002 — Cursor vs Offset

Use a large Ledger or Execution history.

Compare shallow and deep pages.

Study how database work changes as OFFSET increases.

---

## EXP-SQL-003 — Latest Market Price

Compare:

```text
DISTINCT ON
ROW_NUMBER()
MAX + JOIN
```

Use the same dataset and expected result.

---

## EXP-SQL-004 — Portfolio Query

Compare readable alternatives using:

- JOIN;
- CTE;
- Subquery;
- latest Market Price lookup.

---

## EXP-SQL-005 — Realized P/L Aggregation

Aggregate historical Sell Executions by:

- Customer;
- Security;
- date range.

Study GROUP BY, JOIN, predicates, and indexes.

---

## EXP-SQL-006 — Reconciliation Query

Validate:

```text
Current Balance
=
SUM(Ledger Entry Amount)
```

at scale.

Measure the cost of auditing many accounts.

---

## EXP-SQL-007 — N+1 vs Projection

First reproduce N+1 behavior.

Then compare with:

- JOIN;
- projection;
- dedicated SQL.

Record executed statement counts as well as latency.

---

## EXP-SQL-008 — Index Write Cost

Compare Ledger insertion throughput with different index sets.

Explain why read improvements may increase write cost.

---

## EXP-SQL-009 — Isolation Levels

Use two or more controlled connections to compare:

```text
READ COMMITTED
REPEATABLE READ
SERIALIZABLE
```

Record observed reads, updates, blocking, or serialization errors.

---

## EXP-SQL-010 — Pessimistic Lock

Use `FOR UPDATE` and observe competing transactions.

---

## EXP-SQL-011 — Atomic Withdrawal

Compare:

```text
SELECT → Java validation → UPDATE
```

against:

```text
conditional atomic UPDATE
```

under concurrent load.

---

## EXP-SQL-012 — Lock Ordering and Deadlock

Construct a controlled two-account lock-order experiment.

Compare inconsistent ordering with deterministic ordering.

---

## EXP-SQL-013 — Window Function Market Analysis

Use `LAG` to calculate previous Closing Price and derive a daily price change query.

---

## EXP-SQL-014 — Index Selectivity

Create queries where a low-selectivity status index is and is not useful.

Observe planner behavior.

---

## EXP-SQL-015 — Small / Medium / Large Dataset Scaling

Run the same important query at approximately:

```text
1,000 rows
100,000 rows
1,000,000+ rows
```

Explain how the access strategy changes.

---

## Experiment Completion Rule

An experiment is not complete when it only says:

```text
Before = 800ms
After = 20ms
```

It must explain:

- what changed in the plan;
- why less/more work occurred;
- which workload benefits;
- what cost or limitation was introduced.
