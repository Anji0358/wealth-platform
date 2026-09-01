# Phase-by-Phase Test Plan

## Phase 1 — Core Banking / Ledger

Primary tests:

- Customer/Bank Account Domain Unit Tests;
- Securities Account basics;
- LedgerTransaction invariants;
- Application Use Case tests;
- PostgreSQL/Testcontainers repository tests;
- migration tests;
- API contract tests;
- Transfer E2E;
- rollback/atomicity tests.

SQL learning:

- Ledger history query;
- basic reconciliation query;
- basic pagination query.

---

## Phase 2 — Market

Primary tests:

- deterministic GBM;
- fixed Clock;
- Business Day boundaries;
- Market Price idempotency;
- Catch-Up ordering;
- Security parameter timing;
- PostgreSQL uniqueness.

SQL learning:

- latest Market Price query;
- historical price range query;
- `DISTINCT ON` / window-function alternatives.

---

## Phase 3 — Trading

Primary tests:

- Buy/Sell Domain behavior;
- Order/Execution relationship;
- Buy E2E;
- Sell E2E;
- atomic Cash/Position/Ledger updates;
- historical Execution values.

SQL learning:

- Order history;
- Execution history;
- Position queries;
- N+1 observation and correction.

---

## Phase 4 — Portfolio / P&L

Primary tests:

- Remaining Acquisition Cost;
- Average Acquisition Price;
- Realized P/L;
- Unrealized P/L;
- Portfolio Read Model.

SQL learning:

- Portfolio multi-table JOIN;
- latest-price association;
- CTE/subquery/JOIN comparison;
- P/L aggregation;
- window functions.

---

## Phase 5 — Robustness

Primary tests:

- Race Condition reproduction;
- optimistic/pessimistic/atomic SQL comparisons;
- transaction isolation experiments;
- rollback/failure injection;
- Idempotency;
- reconciliation under concurrency.

SQL learning:

- `SELECT ... FOR UPDATE`;
- atomic conditional UPDATE;
- isolation-level behavior;
- daily transfer-limit prototype;
- lock/conflict observation.

---

## Phase 6 — Advanced SQL / Performance / Long-Lived Orders

Primary tests:

- Limit Order lifecycle;
- Cancel Order;
- Partial Execution;
- Reservation behavior;
- high-volume query performance;
- SQL regression experiments.

SQL learning:

- cursor vs offset at scale;
- composite index design;
- index write cost;
- `EXPLAIN (ANALYZE, BUFFERS)`;
- large-data aggregation;
- query-plan regression analysis.

---

## Expected Learning Progression

```text
SQL returns correct data
↓
Understand relational access patterns
↓
Read execution plans
↓
Design and justify indexes
↓
Compare query formulations
↓
Understand transaction isolation and locks
↓
Handle concurrency safely
↓
Benchmark and explain trade-offs
```

This progression is an explicit project goal.
