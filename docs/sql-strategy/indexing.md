# Indexing Strategy

## SQL-IDX-001 — Indexes Come from Access Patterns

Design indexes from actual:

- `WHERE`;
- `JOIN`;
- `ORDER BY`;
- uniqueness;
- pagination;

requirements.

Do not add indexes solely because a column "seems important."

---

## SQL-IDX-002 — EXPLAIN Evidence Is Required for Important Performance Indexes

An important performance index should be justified using query-plan evidence.

Recommended flow:

```text
Baseline query
↓
EXPLAIN (ANALYZE, BUFFERS)
↓
Index candidate
↓
Repeat plan
↓
Compare
↓
Explain result
```

---

## SQL-IDX-003 — Composite Index Column Order Matters

For an access pattern such as:

```sql
WHERE ledger_account_id = ?
ORDER BY occurred_at DESC
```

study an index shaped conceptually as:

```text
(ledger_account_id, occurred_at)
```

and explain how equality filtering and ordered access influence column ordering.

---

## SQL-IDX-004 — Selectivity Is a Design Concern

An index on a low-cardinality field such as:

```text
status
```

may not be useful by itself.

The planner's decision must be inspected rather than assumed.

---

## SQL-IDX-005 — Main Index Candidates Are Hypotheses Until Measured

Potential candidates include:

```text
ledger_entries(ledger_account_id, ...)
market_prices(security_id, business_date)
executions(order_id)
orders(securities_account_id, submitted_at)
```

These are starting hypotheses, not automatic final DDL.

---

## SQL-IDX-006 — Unique Constraints May Also Provide Useful Indexes

Understand when PostgreSQL already creates an index to support a `UNIQUE` constraint so duplicate indexes are not added unnecessarily.

---

## SQL-IDX-007 — Indexes Have Write Cost

Performance experiments should demonstrate that indexes increase maintenance work for:

- INSERT;
- UPDATE;
- storage.

This is especially relevant to Ledger-heavy tables.

---

## SQL-IDX-008 — Partial Index Is an Advanced PostgreSQL Technique

A future long-lived Order model may provide a useful exercise such as indexing only open Orders.

Example concept:

```text
WHERE status IN ('OPEN', 'PARTIALLY_FILLED')
```

Adopt only when the related feature exists.

---

## SQL-IDX-009 — INCLUDE / Covering Index Is an Advanced Technique

Use PostgreSQL `INCLUDE` only for a real read pattern where reducing heap access is measurable and useful.

---

## SQL-IDX-010 — Indexes That Are Not Used Are Investigated

An unused index experiment is still useful.

Record possible reasons such as:

- table too small;
- low selectivity;
- planner statistics;
- incompatible predicate;
- query shape;
- data distribution.

---

## SQL-IDX-011 — Avoid Duplicate or Redundant Indexes

Inspect whether one composite index already serves a narrower access pattern before adding overlapping indexes.

---

## SQL-IDX-012 — Index Design Is Revisited as Data Distribution Changes

There is no universally optimal index independent of workload and data distribution.

Index strategy evolves from observed access patterns.
