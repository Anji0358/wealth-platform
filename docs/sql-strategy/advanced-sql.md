# Advanced SQL

## SQL-ADV-001 — Aggregation Is Performed in SQL Where Appropriate

Practice and use:

```sql
SUM
COUNT
AVG
GROUP BY
HAVING
```

for data-intensive aggregation.

---

## SQL-ADV-002 — Window Functions Are an Explicit Learning Goal

Project exercises should include appropriate use of:

```sql
ROW_NUMBER()
RANK()
LAG()
SUM() OVER(...)
```

---

## SQL-ADV-003 — Latest Market Price Has Multiple Query Forms

Compare approaches such as:

```text
DISTINCT ON
ROW_NUMBER() OVER (...)
MAX(business_date) + JOIN
```

Study correctness, readability, and plan behavior.

---

## SQL-ADV-004 — PostgreSQL DISTINCT ON Is In Scope

PostgreSQL-specific `DISTINCT ON` is acceptable because PostgreSQL is the target database.

---

## SQL-ADV-005 — LAG Supports Time-Series Analysis

Market Price history provides a natural exercise for:

```sql
LAG(closing_price)
OVER (
  PARTITION BY security_id
  ORDER BY business_date
)
```

This can compare current and previous prices without Java-side iteration.

---

## SQL-ADV-006 — Running Aggregations Are Practiced

Use `SUM() OVER(...)` where a real project question benefits from cumulative calculations.

Examples may later include:

- cumulative P/L;
- cumulative cash movement;
- rolling analytics.

---

## SQL-ADV-007 — CTE Is a Readability and Query-Structure Tool, Not a Badge of Complexity

Use CTEs where they clarify a query.

Compare against Subquery and JOIN alternatives rather than assuming CTE is always superior.

---

## SQL-ADV-008 — Cursor Pagination Is the Default Large-History Pattern

Large historical APIs use keyset/cursor pagination rather than deep OFFSET navigation.

---

## SQL-ADV-009 — Cursor Ordering Has a Tie-Breaker

Example conceptual ordering:

```text
occurred_at DESC,
id DESC
```

A cursor must preserve the complete sort key required for deterministic navigation.

---

## SQL-ADV-010 — Offset Pagination Is Still Studied

OFFSET is not forbidden.

It is intentionally compared against cursor pagination so its performance characteristics can be understood.

---

## SQL-ADV-011 — Reconciliation SQL Is a Permanent Engineering Query

Maintain SQL capable of comparing:

```text
account current balance
vs
SUM(ledger entries)
```

This is useful for both auditability and advanced SQL learning.

---

## SQL-ADV-012 — JSONB Is Not Used to Avoid Relational Modeling

Financial Core Data remains relational.

JSONB may be reconsidered only for genuinely flexible auxiliary data.

---

## SQL-ADV-013 — Database Views Are Optional Read-Side Tools

Views may be introduced when they meaningfully simplify a stable Read Model.

They are not required initially.

---

## SQL-ADV-014 — Materialized Views Are Deferred

Do not introduce a Materialized View until measured query cost justifies snapshot-style optimization.

Portfolio analytics may become a future exercise.

---

## SQL-ADV-015 — SQL Functions Are Learning Candidates, Not Core Domain Hosts

PostgreSQL Functions may be explored in advanced phases.

Core Business Rules remain primarily in the Java Domain/Application architecture.

---

## SQL-ADV-016 — Stored Procedures Are Not the Main Application Architecture

Do not move primary financial business workflows into Stored Procedures simply to make them "database-centric."
