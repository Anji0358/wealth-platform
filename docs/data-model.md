# Data Model

## Purpose

This document defines the logical and persistence-oriented data model of the wealth-platform project.

The data model is designed to support:

- financial correctness;
- auditability;
- incremental development by phase;
- PostgreSQL-based persistence;
- intermediate-to-advanced SQL practice;
- clear separation between current state, historical facts, and derived values.

This document intentionally avoids premature JPA-specific mappings or implementation annotations. Concrete entity mappings, repository structure, locking strategy, transaction boundaries, and exact DDL are deferred to later design documents.

---

## 1. Modeling Principles

### DM-001 — Separate Current State, Historical Facts, and Derived Values

**Status:** Confirmed  
**Phase:** All

The data model distinguishes three categories.

### Current State

Examples:

- Customer
- Bank Account
- Securities Account
- Position
- Security

### Historical Facts

Examples:

- Ledger Transaction
- Ledger Entry
- Order
- Execution
- Market Price

### Derived Values

Examples:

- Available Balance
- Average Acquisition Price
- Unrealized P/L
- Total Assets

Values that can be reliably derived from authoritative persisted data should not be duplicated without a clear performance or historical reason.

---

### DM-002 — Major Entities Use UUID Identifiers

**Status:** Confirmed  
**Phase:** All

Major entities use UUID identifiers.

Examples:

```text
Customer ID
Bank Account ID
Securities Account ID
Security ID
Order ID
Execution ID
Ledger Transaction ID
Ledger Entry ID
Ledger Account ID
Market Price ID
```

PostgreSQL `uuid` is the preferred physical type.

Whether UUIDs are generated in Java or PostgreSQL is deferred to `architecture.md`.

---

## 2. Customer

### DM-003 — Customer Model

**Status:** Confirmed  
**Phase:** Phase 1

The initial Customer model is intentionally minimal.

Conceptual attributes:

```text
Customer
├── id
├── name
└── createdAt
```

No unique email or real-world identity key is required in the initial version.

Duplicate real-world Customer detection remains a Future Consideration.

Customer records are provisioned by Administrator/System responsibility rather than Customer self-registration.

---

### DM-004 — Customer to Bank Account Cardinality

**Status:** Confirmed  
**Phase:** Phase 1

A Customer may own zero or more Bank Accounts.

```text
Customer 1
   │
   └── 0..N Bank Accounts
```

Each Bank Account belongs to exactly one Customer.

---

### DM-005 — Customer to Securities Account Cardinality

**Status:** Confirmed  
**Phase:** Phase 1

A Customer may own at most one Securities Account in the initial scope.

```text
Customer 1
   │
   └── 0..1 Securities Account
```

This relationship should be protected by a uniqueness constraint on the Securities Account owner reference.

---

## 3. Bank Account

### DM-006 — Bank Account Model

**Status:** Confirmed  
**Phase:** Phase 1

Conceptual attributes:

```text
BankAccount
├── id
├── customerId
├── accountType
├── status
├── currentBalance
├── reservedAmount
├── createdAt
└── closedAt
```

`closedAt` is nullable while the account remains open.

---

### DM-007 — Available Balance Is Derived

**Status:** Confirmed  
**Phase:** Phase 1

Available Balance is defined as:

```text
Available Balance
=
Current Balance - Reserved Amount
```

Persist:

```text
current_balance
reserved_amount
```

Do not persist:

```text
available_balance
```

Persisting all three would allow contradictory state.

---

### DM-008 — Reserved Amount Exists Before Active Reservation Features

**Status:** Confirmed  
**Phase:** Phase 1

`reserved_amount` exists in the logical account model even though Phase 1-3 synchronous operations do not actively use persistent reservation.

Until Phase 6 reservation features are introduced:

```text
reserved_amount = 0
```

under normal operation.

---

### DM-009 — Initial Bank Account Types

**Status:** Confirmed  
**Phase:** Phase 1

Initial account types are:

```text
ORDINARY_DEPOSIT
SAVINGS
```

The physical representation may use text, a PostgreSQL enum, or a checked column.

That choice is deferred to `sql-strategy.md` and `architecture.md`.

---

## 4. Securities Account

### DM-010 — Securities Account Model

**Status:** Confirmed  
**Phase:** Phase 1

Conceptual attributes:

```text
SecuritiesAccount
├── id
├── customerId
├── status
├── currentBalance
├── reservedAmount
├── createdAt
└── closedAt
```

---

### DM-011 — Bank and Securities Accounts Are Separate Domain Models

**Status:** Confirmed  
**Phase:** Phase 1

The system does not introduce a shared JPA inheritance hierarchy merely because Bank Account and Securities Account have similar fields.

Conceptually:

```text
Bank Account
Securities Account
```

remain distinct domain concepts.

Common fields alone are not sufficient reason to merge them into one persistent Account entity.

---

## 5. Ledger Account

### DM-012 — Ledger Uses a Dedicated Ledger Account Identity

**Status:** Confirmed  
**Phase:** Phase 1

The shared Cash Ledger needs to reference:

- Bank Accounts;
- Securities Accounts;
- External Account;
- Market Settlement Account.

A dedicated Ledger identity layer is therefore introduced.

Conceptually:

```text
LedgerAccount
├── id
└── kind
```

---

### DM-013 — Domain Accounts Map to Ledger Accounts

**Status:** Confirmed  
**Phase:** Phase 1

Conceptually:

```text
BankAccount        1 ── 1 LedgerAccount
SecuritiesAccount  1 ── 1 LedgerAccount

External Account
    └─────────────── LedgerAccount

Market Settlement Account
    └─────────────── LedgerAccount
```

This allows every Ledger Entry to reference one consistent foreign key:

```text
ledger_account_id
```

---

### DM-014 — Bank Account and Ledger Account Are Different Concepts

**Status:** Confirmed  
**Phase:** Phase 1

`BankAccount` is a Banking-domain account.

`LedgerAccount` is the accounting identity used by Ledger Entries.

The physical implementation may optimize identity relationships later, but the logical concepts remain distinct.

---

## 6. Ledger Transaction

### DM-015 — Ledger Transaction Model

**Status:** Confirmed  
**Phase:** Phase 1

Conceptual attributes:

```text
LedgerTransaction
├── id
├── transactionType
├── occurredAt
├── reversalOfTransactionId nullable
└── createdAt
```

Possible transaction types include:

```text
DEPOSIT
WITHDRAWAL
BANK_TRANSFER
BANK_SECURITIES_TRANSFER
SECURITIES_BANK_TRANSFER
TRADE_BUY
TRADE_SELL
REVERSAL
```

Exact enum naming remains an implementation detail.

---

### DM-016 — Ledger Transaction to Entry Cardinality

**Status:** Confirmed  
**Phase:** Phase 1

Each Ledger Transaction contains two or more Ledger Entries.

```text
LedgerTransaction 1
        │
        └── 2..N LedgerEntries
```

---

## 7. Ledger Entry

### DM-017 — Ledger Entry Model

**Status:** Confirmed  
**Phase:** Phase 1

Conceptual attributes:

```text
LedgerEntry
├── id
├── ledgerTransactionId
├── ledgerAccountId
└── amount
```

---

### DM-018 — Ledger Amount Uses Signed Whole JPY

**Status:** Confirmed  
**Phase:** Phase 1

Ledger Entry Amount uses signed whole-JPY semantics.

Examples:

```text
-10000
+10000
```

A zero-value Ledger Entry has no business meaning and should be prohibited.

---

### DM-019 — Ledger Zero-Sum Is a Cross-Row Invariant

**Status:** Confirmed  
**Phase:** Phase 1

For each Ledger Transaction:

```text
SUM(LedgerEntry.amount) = 0
```

This invariant spans multiple rows and cannot be reliably represented by a simple row-level `CHECK`.

It should be enforced through a combination of:

```text
Domain logic
Transaction boundaries
Database design where appropriate
Automated tests
```

The exact SQL strategy is deferred to `sql-strategy.md`.

---

### DM-020 — Ledger Is the Historical Source of Cash-Movement Facts

**Status:** Confirmed  
**Phase:** Phase 1

Ledger explains why cash moved.

Current account balances represent operational current state.

Therefore:

```text
Ledger
→ Historical Facts

BankAccount.currentBalance
SecuritiesAccount.currentBalance
→ Current Operational State
```

These are intentionally both persisted.

---

### DM-021 — Balance and Ledger Must Reconcile

**Status:** Confirmed  
**Phase:** Phase 1+

For an account whose opening balance is zero, the following relationship should hold:

```text
Current Balance
=
SUM(Ledger Entries for the account's LedgerAccount)
```

This should be verifiable through reconciliation queries, especially during robustness and SQL-learning phases.

---

## 8. Ledger Reversal

### DM-022 — Reversal Uses Self-Reference

**Status:** Confirmed  
**Phase:** Later correction scope

A Reversal Transaction references the Ledger Transaction it reverses.

Conceptually:

```text
ReversalTransaction
        │
        └── reversalOfTransactionId
                    │
                    ▼
          OriginalTransaction
```

The initial model permits at most one full Reversal Transaction per original transaction.

---

## 9. Security

### DM-023 — Security Model

**Status:** Confirmed  
**Phase:** Phase 2

Conceptual attributes:

```text
Security
├── id
├── securityCode
├── name
├── status
├── initialPrice
├── expectedReturn
├── volatility
└── createdAt
```

`securityCode` is unique and immutable after registration.

---

## 10. Market Price

### DM-024 — Market Price Model

**Status:** Confirmed  
**Phase:** Phase 2

Conceptual attributes:

```text
MarketPrice
├── id
├── securityId
├── businessDate
├── closingPrice
└── generatedAt
```

---

### DM-025 — Market Price Is Unique per Security and Business Date

**Status:** Confirmed  
**Phase:** Phase 2

The logical unique key is:

```text
(securityId, businessDate)
```

This supports idempotent daily price generation.

---

### DM-026 — Raw GBM Values Are Not Persisted as Market Prices

**Status:** Confirmed  
**Phase:** Phase 2

The Market Price model stores the normalized official Closing Price.

It does not initially store:

- raw unrounded GBM output;
- sampled `Z`;
- simulation debugging data.

If simulation diagnostics become important later, introduce a separate model such as Simulation Run rather than overloading Market Price.

---

### DM-027 — Business Date and Generation Timestamp Are Separate

**Status:** Confirmed  
**Phase:** Phase 2

`businessDate` identifies which Business Day the price belongs to.

`generatedAt` records when the system actually generated the record.

Example:

```text
businessDate = 2026-09-01
generatedAt  = 2026-09-02T...
```

This supports Catch-Up generation.

---

## 11. Position

### DM-028 — Position Model

**Status:** Confirmed  
**Phase:** Phase 3-4

Conceptual attributes:

```text
Position
├── securitiesAccountId
├── securityId
├── quantity
└── remainingAcquisitionCost
```

A synthetic Position ID is optional from a logical perspective and may be added if useful for persistence or JPA ergonomics.

---

### DM-029 — Position Is Unique per Securities Account and Security

**Status:** Confirmed  
**Phase:** Phase 3

Logical uniqueness:

```text
(securitiesAccountId, securityId)
```

There must not be multiple current Position rows for the same Security in the same Securities Account.

---

### DM-030 — Average Acquisition Price Is Derived

**Status:** Confirmed  
**Phase:** Phase 4

Average Acquisition Price is:

```text
Remaining Acquisition Cost
/
Quantity
```

Persist:

```text
quantity
remaining_acquisition_cost
```

Do not persist:

```text
average_acquisition_price
```

This avoids three-way inconsistency.

---

### DM-031 — Remaining Acquisition Cost Uses Whole JPY

**Status:** Confirmed  
**Phase:** Phase 4

Remaining Acquisition Cost represents actual remaining acquisition cost in JPY.

It is stored as whole JPY.

Example:

```text
remainingAcquisitionCost = 3001 JPY
quantity = 3

Average Acquisition Price
= 1000.333...
```

The derived average may therefore contain fractional JPY while source monetary state remains integer JPY.

---

### DM-032 — Zero-Quantity Position Does Not Remain Persisted

**Status:** Confirmed  
**Phase:** Phase 3

Position represents current holdings.

When Quantity reaches zero, the current Position ceases to exist.

Historical ownership remains traceable through Orders, Executions, and Ledger history.

---

### DM-033 — Reserved Quantity Is Deferred to Phase 6

**Status:** Planned Future Scope  
**Phase:** Phase 6

Phase 3 persists current Quantity only.

When long-lived Sell Orders are introduced, Position may gain:

```text
reserved_quantity
```

Available Quantity remains derived:

```text
available_quantity
=
quantity - reserved_quantity
```

---

## 12. Order

### DM-034 — Order Model

**Status:** Confirmed  
**Phase:** Phase 3

Conceptual attributes:

```text
Order
├── id
├── securitiesAccountId
├── securityId
├── side
├── quantity
├── status
└── submittedAt
```

Initial Order side values:

```text
BUY
SELL
```

---

### DM-035 — Phase 3 Order Does Not Store Limit Price

**Status:** Confirmed  
**Phase:** Phase 3

Phase 3 supports Market Orders only.

Do not add unused future fields such as:

```text
requestedPrice
limitPrice
```

until Limit Orders are actually introduced.

---

## 13. Execution

### DM-036 — Execution Model

**Status:** Confirmed  
**Phase:** Phase 3-4

Conceptual attributes:

```text
Execution
├── id
├── orderId
├── quantity
├── price
├── executedAt
├── allocatedAcquisitionCost nullable
└── realizedProfitLoss nullable
```

Cost-basis-related fields are relevant to Sell Executions.

---

### DM-037 — Order to Execution Is Modeled as One-to-Many

**Status:** Confirmed  
**Phase:** Phase 3+

The relational model permits:

```text
Order 1
  │
  └── 0..N Executions
```

Phase 3 Business Rules still require exactly one Execution for a successful immediate full-fill Order.

The 1:N relational relationship reflects the already-confirmed conceptual independence of Order and Execution and supports later Partial Execution.

---

### DM-038 — Execution Price Is Persisted

**Status:** Confirmed  
**Phase:** Phase 3

Execution Price is a historical fact and must be stored.

It must not be reconstructed later by joining to the current or historical Market Price.

---

### DM-039 — Execution Quantity Is Persisted

**Status:** Confirmed  
**Phase:** Phase 3

Execution Quantity is stored even though Phase 3 uses the same Quantity as the Order.

This preserves Execution as an independent historical fact and supports later Partial Execution.

---

### DM-040 — Execution Amount Is Derived Initially

**Status:** Confirmed  
**Phase:** Phase 3

Initial Execution Amount is:

```text
Execution Price × Execution Quantity
```

Because fees and taxes are out of scope, there is no need to persist a redundant Execution Amount initially.

If fees are introduced later, revisit Gross Amount / Fee / Net Amount modeling.

---

### DM-041 — Sell Execution Persists Cost Allocation and Realized P/L

**Status:** Confirmed  
**Phase:** Phase 4

Sell Execution persists:

```text
allocatedAcquisitionCost
realizedProfitLoss
```

These values represent historical results determined at execution time.

They should remain stable even if the system later introduces another cost-basis method such as FIFO.

---

## 14. Portfolio

### DM-042 — Portfolio Is Not a Persisted Entity Initially

**Status:** Confirmed  
**Phase:** Phase 4

Portfolio is a derived read model composed from:

- Bank Accounts;
- Securities Account Cash;
- Positions;
- Market Prices.

The initial version does not create a dedicated Portfolio table.

---

### DM-043 — Total Assets Is Derived

**Status:** Confirmed  
**Phase:** Phase 4

Total Assets is calculated from current account balances and Position valuations.

It is not persisted initially.

If future performance requirements justify snapshots or projections, those may be introduced later.

---

### DM-044 — Unrealized P/L Is Derived

**Status:** Confirmed  
**Phase:** Phase 4

Unrealized P/L changes as Market Prices change.

It should be calculated from:

```text
Position
+
Latest persisted Market Price
```

rather than persisted as mutable current state.

---

## 15. Administration and Identity

### DM-045 — Administrator Is Not a Persisted Identity Entity Initially

**Status:** Confirmed  
**Phase:** Initial scope

Administrator is an Actor in use cases, but production-grade authentication is outside scope.

The initial data model therefore does not require:

```text
administrator table
```

A dedicated identity model should be introduced only when authentication/authorization becomes actual project scope.

The development-only Acting Customer header and `ActorContext` are not persisted identity entities. Existing Customer ownership foreign keys remain the authoritative persisted relationship for Application ownership checks.

---

## 16. Idempotency and Status History

### DM-046 — Idempotency Persistence Is Deferred to Phase 5

**Status:** Planned Future Scope  
**Phase:** Phase 5

An Idempotency persistence model may later include concepts such as:

```text
IdempotencyRecord
├── key
├── operationType
├── requestFingerprint
├── resultReference
└── createdAt
```

The exact structure is intentionally deferred until the idempotency implementation phase.

---

### DM-047 — Status History Tables Are Not Introduced Initially

**Status:** Confirmed  
**Phase:** Initial scope

Initial models persist current status only.

Do not introduce tables such as:

```text
bank_account_status_history
security_status_history
```

without a concrete audit requirement.

A future Audit Event or Status Change History model may be added if needed.

---

## 17. Deletion Policy

### DM-048 — Historical and Financial Entities Avoid Hard Delete

**Status:** Confirmed  
**Phase:** All

Normal business operations should not physically delete:

- Customer;
- Bank Account;
- Securities Account;
- Security;
- Ledger Transaction;
- Ledger Entry;
- Order;
- Execution;
- Market Price.

Closed or disabled states should be represented through status where applicable.

Position is an exception because it represents current state and may cease to exist when Quantity becomes zero.

---

## 18. PostgreSQL Type Strategy

### DM-049 — Whole-JPY Monetary Values Use BIGINT Semantics

**Status:** Confirmed  
**Phase:** Phase 1+

Whole-JPY monetary values should use PostgreSQL `BIGINT` semantics.

Examples include:

```text
current_balance
reserved_amount
closing_price
ledger_entry.amount
execution.price
remaining_acquisition_cost
realized_profit_loss
allocated_acquisition_cost
initial_price
```

Floating-point database types such as `double precision` must not be used for financial monetary values.

---

### DM-050 — Expected Return and Volatility Use NUMERIC Semantics

**Status:** Confirmed  
**Phase:** Phase 2

Expected Return `μ` and Volatility `σ` require decimal precision.

PostgreSQL `NUMERIC` semantics are preferred.

Exact precision and scale are deferred until physical DDL design.

---

### DM-051 — Security Quantity Uses Integer Semantics

**Status:** Confirmed  
**Phase:** Phase 3

Security Quantity is an integer in the initial scope.

PostgreSQL `BIGINT` is preferred for:

```text
quantity
```

Fractional shares are outside scope.

---

### DM-052 — Event Timestamps Use TIMESTAMPTZ

**Status:** Confirmed  
**Phase:** Phase 1+

Event timestamps should use timezone-aware PostgreSQL timestamp semantics.

Preferred type:

```text
TIMESTAMPTZ
```

Examples:

```text
created_at
submitted_at
executed_at
occurred_at
generated_at
closed_at
```

---

### DM-053 — Market Business Date Uses DATE

**Status:** Confirmed  
**Phase:** Phase 2

Market Price Business Day uses:

```text
DATE
```

because it represents a market calendar date rather than an instant.

---

### DM-054 — Currency Column Is Not Repeated Initially

**Status:** Confirmed  
**Phase:** Initial scope

The initial platform supports JPY only.

The model does not repeat:

```text
currency = JPY
```

on every monetary row.

Multi-currency support requires an explicit future data-model extension.

---

## 19. Constraint Strategy

### DM-055 — Database Constraint Candidates

**Status:** Confirmed principle  
**Phase:** Phase 1+

Where suitable, the physical model should consider constraints such as:

```text
FK Customer → BankAccount
FK Customer → SecuritiesAccount

UNIQUE SecuritiesAccount.customerId
UNIQUE Security.securityCode
UNIQUE MarketPrice(securityId, businessDate)
UNIQUE Position(securitiesAccountId, securityId)

CHECK currentBalance >= 0
CHECK reservedAmount >= 0
CHECK reservedAmount <= currentBalance
CHECK quantity > 0
CHECK closingPrice > 0
CHECK volatility >= 0
CHECK ledgerEntry.amount <> 0
```

Exact DDL is deferred to `sql-strategy.md`.

---

### DM-056 — Cross-Row Invariants Need Multiple Enforcement Layers

**Status:** Confirmed  
**Phase:** Phase 1+

Some invariants are difficult or inappropriate to enforce with simple row-level constraints.

Examples:

```text
SUM(Ledger Entries) = 0
Ledger Transaction has at least 2 Entries
Phase 3 successful Order has exactly 1 Execution
```

These should be protected through an intentional combination of:

```text
Domain model
Application transaction
Database design
Automated tests
```

Complex triggers should not be introduced prematurely merely to centralize every rule in the database.

---

## 20. Incremental Schema Evolution

### DM-057 — Schema Evolves by Project Phase

**Status:** Confirmed  
**Phase:** All

The project should not create the entire future schema at the beginning.

Recommended progression:

### Phase 1

```text
Customer
BankAccount
SecuritiesAccount
LedgerAccount
LedgerTransaction
LedgerEntry
```

### Phase 2

```text
Security
MarketPrice
```

### Phase 3

```text
Order
Execution
Position
```

### Phase 4

```text
Remaining Acquisition Cost
Allocated Acquisition Cost
Realized P/L semantics
```

### Phase 5

```text
Idempotency persistence
Concurrency-related schema changes if required
```

### Phase 6

```text
Reserved Quantity
Limit Order fields
Long-lived Order support
Partial Execution extensions
```

Each phase should introduce schema changes through versioned migrations.

---

## 21. Logical ER Model

The central logical relationships are:

```text
Customer
 ├── 0..N BankAccount
 └── 0..1 SecuritiesAccount
              │
              ├── 0..N Position ───── Security
              │
              └── 0..N Order ──────── Security
                         │
                         └── 0..N Execution

Security
 └── 0..N MarketPrice


BankAccount ───────── LedgerAccount
SecuritiesAccount ─── LedgerAccount
External Account ──── LedgerAccount
Market Settlement ─── LedgerAccount
                          │
                          └── 0..N LedgerEntry
                                       │
                                       N
                                       │
                                LedgerTransaction
```

Additional logical constraints:

```text
Position
UNIQUE(securitiesAccountId, securityId)

MarketPrice
UNIQUE(securityId, businessDate)

SecuritiesAccount
UNIQUE(customerId)
```

---

## 22. Persisted vs Derived Summary

### Persisted Current State

```text
Customer
BankAccount.currentBalance
BankAccount.reservedAmount
SecuritiesAccount.currentBalance
SecuritiesAccount.reservedAmount
Security
Position.quantity
Position.remainingAcquisitionCost
```

### Persisted Historical Facts

```text
LedgerTransaction
LedgerEntry
Order
Execution
MarketPrice
Sell Execution allocatedAcquisitionCost
Sell Execution realizedProfitLoss
```

### Derived Values

```text
Bank Available Balance
Securities Available Balance
Average Acquisition Price
Execution Amount
Portfolio
Position Valuation
Unrealized P/L
Total Assets
Available Quantity in Phase 6
```

---

## 23. Deferred Decisions

This document defines logical persistence semantics but intentionally defers several implementation choices.

### `architecture.md`

- Java ID generation location;
- JPA mapping style;
- Entity/Aggregate boundaries;
- Repository boundaries;
- transaction boundaries;
- locking strategy;
- Java numeric types;
- domain-object versus persistence-object separation.

### `sql-strategy.md`

- exact DDL;
- enum representation;
- exact `NUMERIC(p,s)` choices;
- index strategy;
- reconciliation SQL;
- cross-row invariant enforcement strategy;
- execution-plan analysis.

### `api-spec.md`

- external identifier representation;
- pagination format;
- resource DTO shape.

### `test-strategy.md`

- database integration tests;
- constraint tests;
- migration tests;
- reconciliation tests;
- concurrency tests.

---

## 24. Future Considerations

Potential later extensions include:

- multiple Securities Accounts per Customer;
- duplicate Customer identity model;
- multi-currency modeling;
- fractional shares;
- Position history;
- status-change audit history;
- richer system Ledger Accounts;
- Portfolio snapshots;
- valuation projections;
- explicit simulation-run diagnostics;
- fee and tax persistence;
- scheduled transfers;
- external-bank data;
- authentication/authorization identities.

These are not part of the initial data model unless promoted through later requirements.

---

## 25. Summary

The data-model direction is:

```text
Persist authoritative current state
Persist immutable historical facts
Derive values that can be reliably calculated
Avoid redundant columns that create inconsistent states
Use Ledger for cash-movement history
Use current balances for operational reads and commands
Model Order and Execution separately
Model Position as current holdings only
Use PostgreSQL constraints where appropriate
Use domain/application/tests for cross-row invariants
Evolve the schema incrementally by phase
```

The most important initial modeling decisions are:

```text
1. Available Balance is derived.
2. Average Acquisition Price is derived.
3. Portfolio / Total Assets / Unrealized P/L are derived.
4. LedgerAccount is separate from Banking/Brokerage account concepts.
5. Current Balance and Ledger history are both persisted and must reconcile.
6. Order → Execution is modeled as 1:N relationally.
7. Sell Execution preserves historical acquisition-cost allocation and Realized P/L.
8. Schema growth follows project phases rather than speculative future design.
```
