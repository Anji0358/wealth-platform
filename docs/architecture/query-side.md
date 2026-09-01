# Query Side and Read Models

## ARCH-035 — Separate Command and Query Responsibilities Conceptually

**Status:** Confirmed

The project does not implement full CQRS infrastructure.

However, it distinguishes:

```text
Command
→ Domain Model + Repositories

Query
→ Read Model + SQL/Projection
```

---

## ARCH-036 — Query Does Not Need to Rehydrate Full Aggregates

**Status:** Confirmed

For read-only screens such as Portfolio, do not load and reconstruct every Aggregate unless the domain behavior actually requires it.

A query may directly compose a read model.

Example:

```text
PortfolioView
```

from Bank, Securities, Position, and Market data.

---

## ARCH-037 — Dedicated Query Services Are Allowed

**Status:** Confirmed

Examples:

```text
PortfolioQueryService
BankTransactionQueryService
MarketPriceQueryService
```

Query services do not need to mirror command repositories.

---

## ARCH-038 — Cross-Domain Read JOINs Are Allowed

**Status:** Confirmed

Read-side queries may intentionally join tables from multiple domain modules.

Example:

```text
Portfolio
```

may combine:

- Bank Account;
- Securities Account;
- Position;
- Security;
- Market Price.

This is an intentional read-side exception to strict update ownership.

---

## ARCH-039 — Portfolio Is a Read Model

**Status:** Confirmed

Do not create a Portfolio Aggregate solely to serve read endpoints.

Portfolio remains derived from current state and market data.

---

## ARCH-040 — Event Sourcing Is Not Used

**Status:** Confirmed

Ledger presence does not imply Event Sourcing.

Current balances remain persisted operational state.

Ledger is an immutable cash-movement history, not the sole mechanism used to reconstruct the entire application state.

---

## ARCH-041 — Full CQRS Infrastructure Is Not Used

**Status:** Confirmed

Do not introduce:

```text
Command database
Query database
Projection processor
Event bus
Separate deployment
```

for the initial project.

Logical responsibility separation is sufficient.

---

## ARCH-042 — Domain Events Are Not Initial Infrastructure

**Status:** Confirmed

Do not introduce an event bus with events such as:

```text
MoneyTransferredEvent
OrderExecutedEvent
```

for operations already handled naturally within one local transaction.

Domain Events may be reconsidered if module coupling later becomes a real problem.
