# SQL Correctness Strategy

SQL correctness means that a query returns the correct business result.

It is distinct from SQL performance.

## TS-SQLC-001 — Important SQL Has Result-Level Tests

Examples include:

- Bank transaction history;
- latest Market Price per Security;
- Portfolio valuation;
- Realized P/L aggregation;
- Ledger reconciliation;
- future daily transfer limits.

---

## TS-SQLC-002 — Reconciliation Query Is a Core SQL Test

Verify:

```text
Account Current Balance
=
SUM(Ledger Entries for its Ledger Account)
```

for accounts whose initial balance is zero.

This query should be exercised both as correctness verification and as a later performance experiment.

---

## TS-SQLC-003 — Latest Market Price Query Has Multiple Valid SQL Forms

The project should eventually compare approaches such as:

```text
DISTINCT ON
ROW_NUMBER() OVER (...)
MAX(business_date) + JOIN
```

All approaches must first be proven correct before performance comparison.

---

## TS-SQLC-004 — Portfolio Query Is Tested as a Read Model

Verify correct composition of:

- Position;
- latest Market Price;
- valuation;
- Remaining Acquisition Cost;
- Unrealized P/L.

---

## TS-SQLC-005 — Realized P/L Aggregation Is Tested

Queries may aggregate by:

- Customer;
- Security;
- date range.

Expected use includes:

```text
SUM
GROUP BY
JOIN
date filtering
```

---

## TS-SQLC-006 — Cursor Pagination SQL Is Tested for Stable Semantics

Correctness tests must verify:

- stable order;
- no duplication;
- no missing rows;
- boundary behavior.

---

## TS-SQLC-007 — Query Correctness Is Not Proven by ORM Mocking

A repository mock returning expected objects does not prove that the real SQL is correct.

Important SQL must run against PostgreSQL.

---

## TS-SQLC-008 — SQL Alternatives Share the Same Expected Dataset

When comparing two SQL forms, they must be evaluated against the same deterministic dataset and expected result.
