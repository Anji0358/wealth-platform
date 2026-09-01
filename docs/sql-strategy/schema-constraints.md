# Schema, Migrations, and Constraints

## SQL-SCH-001 — Schema Is Managed by Versioned Migration

Database schema changes are represented by versioned migration files.

Hibernate automatic DDL generation is not the authoritative schema-management mechanism.

---

## SQL-SCH-002 — Migrations Grow Incrementally by Phase

Schema evolution follows project phases rather than creating all future tables up front.

Conceptually:

```text
Phase 1 → Core Banking / Ledger
Phase 2 → Market
Phase 3 → Trading
Phase 4 → P/L semantics
Phase 5 → Idempotency / concurrency additions
Phase 6 → long-lived Orders / reservation
```

---

## SQL-SCH-003 — Applied Migrations Are Immutable

Once a migration has been applied and shared, correct future schema changes by creating a new migration.

Do not rewrite migration history as normal practice.

---

## SQL-SCH-004 — SQL Naming Uses snake_case

Database names use snake_case.

Examples:

```text
bank_accounts
current_balance
ledger_transaction_id
```

Avoid obscure abbreviations.

---

## SQL-SCH-005 — Database Naming Follows Domain Language

Table and column names should preserve the agreed Ubiquitous Language where practical.

Do not use technical abbreviations that obscure business meaning.

---

## SQL-SCH-006 — UUID Is the Primary Identifier Type

Major identifiers use PostgreSQL:

```text
uuid
```

Sequence-based numeric identifiers are not the primary project identity strategy.

---

## SQL-SCH-007 — Whole-JPY Values Use BIGINT

Financial JPY values use integer semantics.

Do not use floating-point types for:

- balances;
- prices;
- Ledger amounts;
- acquisition cost;
- realized P/L.

---

## SQL-SCH-008 — Expected Return and Volatility Use NUMERIC

Simulation parameters such as:

```text
expected_return
volatility
```

use PostgreSQL `NUMERIC` semantics.

Exact precision/scale is determined in physical DDL design.

---

## SQL-SCH-009 — Business Dates and Instants Use Different Types

Use:

```text
DATE
```

for Market Business Date.

Use:

```text
TIMESTAMPTZ
```

for actual event instants.

---

## SQL-SCH-010 — Foreign Keys Reinforce Ownership Relationships

Use FK constraints where the relational model requires a referenced entity.

Examples:

```text
bank_account.customer_id
securities_account.customer_id
market_price.security_id
execution.order_id
ledger_entry.ledger_transaction_id
```

---

## SQL-SCH-011 — UNIQUE Constraints Reinforce Business Invariants

Important logical uniqueness includes:

```text
securities_account(customer_id)
security(security_code)
market_price(security_id, business_date)
position(securities_account_id, security_id)
```

---

## SQL-SCH-012 — CHECK Constraints Reinforce Row-Level Invariants

Candidate checks include:

```text
current_balance >= 0
reserved_amount >= 0
reserved_amount <= current_balance
closing_price > 0
quantity > 0
volatility >= 0
ledger_entry.amount <> 0
```

---

## SQL-SCH-013 — Cross-Row Rules Are Not Forced into CHECK

Rules such as:

```text
SUM(Ledger Entries) = 0
```

span multiple rows.

Protect them through appropriate combinations of:

- Domain logic;
- application transaction;
- database design;
- tests.

---

## SQL-SCH-014 — Triggers Are Not an Initial Default

Do not place core Business Rules into database triggers merely because the database can execute them.

Triggers make rule location and application flow harder to understand.

---

## SQL-SCH-015 — Trigger Usage Requires a Concrete Justification

A trigger may be considered later when there is a database-level requirement that cannot be handled cleanly through existing mechanisms.

Any such choice should be documented as an ADR-level decision when significant.

---

## SQL-SCH-016 — Core Financial Data Remains Normalized Initially

Do not use JSONB or duplicated snapshot columns to avoid ordinary relational modeling.

The initial core model stays relational and normalized.

---

## SQL-SCH-017 — Denormalization Requires Measured Need

Possible future denormalization such as Portfolio snapshots should be introduced only after an actual query-performance requirement is demonstrated.

---

## SQL-SCH-018 — One PostgreSQL Schema Initially

Start with one PostgreSQL schema.

Domain separation is maintained through code ownership and table design rather than separate PostgreSQL schemas.

Schema-level domain separation may be reconsidered later.
