# SQL Experiments and Learning Tasks

## Experiment Documentation

Create experiment reports under a future folder such as:

```text
docs/sql-experiments/
```

Recommended naming:

```text
EXP-SQL-001-ledger-history-index.md
EXP-SQL-002-cursor-vs-offset.md
EXP-SQL-003-latest-market-price.md
EXP-SQL-004-portfolio-query.md
```

Each report should contain:

```text
Goal
Dataset
Baseline SQL
Baseline EXPLAIN (ANALYZE, BUFFERS)
Observed Problem
Alternative / Index
New Plan
New Result
Explanation
Trade-offs
```

---

## TS-SQLE-001 — Ledger Reconciliation Experiment

Study:

```text
SUM
GROUP BY
JOIN
aggregation
indexing
```

Goal:

```text
Current Balance
vs
SUM(Ledger Entries)
```

---

## TS-SQLE-002 — Bank Transaction History Experiment

Study:

```text
filter by Ledger Account
ORDER BY
LIMIT
cursor pagination
composite index
```

---

## TS-SQLE-003 — Latest Market Price Experiment

Compare PostgreSQL/query approaches such as:

```text
DISTINCT ON
ROW_NUMBER() OVER (...)
MAX + JOIN
```

Evaluate:

- correctness;
- readability;
- plan;
- performance.

---

## TS-SQLE-004 — Portfolio Query Experiment

Study:

```text
multi-table JOIN
CTE
subquery
aggregation
latest-price lookup
```

The goal is to understand both query composition and plan behavior.

---

## TS-SQLE-005 — Realized P/L Aggregation Experiment

Study aggregation by:

- Customer;
- Security;
- date range.

Use:

```text
SUM
GROUP BY
JOIN
date predicates
```

---

## TS-SQLE-006 — Window Function Learning

Explicitly practice appropriate uses of:

```sql
ROW_NUMBER()
RANK()
SUM() OVER(...)
LAG()
```

Example use:

```sql
LAG(closing_price)
OVER (
  PARTITION BY security_id
  ORDER BY business_date
)
```

to compare current and previous Closing Price.

---

## TS-SQLE-007 — CTE vs Subquery vs JOIN Comparison

Use a real project query and compare alternative formulations.

The goal is not to prefer CTE automatically, but to understand readability and plan behavior.

---

## TS-SQLE-008 — Cursor vs Offset Pagination Experiment

Measure increasing page depth against a large historical table.

Expected learning:

- why large OFFSET values become expensive;
- how keyset/cursor pagination changes access patterns.

---

## TS-SQLE-009 — Index Selectivity and Column Order

Experiment with:

- single-column indexes;
- composite indexes;
- unique indexes;
- low-selectivity columns;
- column order.

Explain why a given index is or is not chosen.

---

## TS-SQLE-010 — Write Cost of Indexes

Measure insert/update impact when adding indexes to write-heavy Ledger tables.

This demonstrates that indexes are not free.

---

## TS-SQLE-011 — Isolation-Level Experiment

Observe behavior under:

```text
READ COMMITTED
REPEATABLE READ
SERIALIZABLE
```

Use controlled concurrent transactions and record what changes.

---

## TS-SQLE-012 — Row Locking Experiment

Use PostgreSQL constructs such as:

```sql
SELECT ... FOR UPDATE
```

and observe:

- blocking;
- wait behavior;
- conflict timing.

---

## TS-SQLE-013 — Atomic SQL Update Experiment

Compare a read-then-update approach against an atomic conditional update such as:

```sql
UPDATE bank_accounts
SET current_balance = current_balance - :amount
WHERE id = :id
  AND current_balance - reserved_amount >= :amount;
```

Evaluate:

- correctness under concurrency;
- rows affected semantics;
- locking behavior;
- implementation complexity.

---

## TS-SQLE-014 — Daily Transfer Limit as an Advanced Future SQL Exercise

When daily transfer limits enter scope, test:

```text
daily SUM
date-range filtering
concurrent requests
race conditions
indexing
```

This combines SQL aggregation with transaction/concurrency design.

---

## TS-SQLE-015 — Every Experiment Includes an Explanation

A successful experiment is not only:

```text
faster
```

It must explain:

- what PostgreSQL changed;
- why the plan changed;
- what trade-off was introduced;
- whether the improvement generalizes.
