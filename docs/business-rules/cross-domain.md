# Cross-Domain Business Rules

## Monetary Rules

### BR-XDM-001 — Initial Currency Is JPY Only

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

The initial system supports JPY only.

Foreign currency and FX conversion are outside the initial scope.

---

### BR-XDM-002 — Ordinary Cash Amounts Use Whole-JPY Semantics

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

Cash balances, Ledger amounts, persisted Market Prices, and ordinary trading cash amounts use whole-JPY semantics.

Fractional precision may still exist for derived concepts such as Average Acquisition Price.

---

### BR-XDM-003 — User-Requested Financial Amounts Must Be Positive

**Status:** Confirmed  
**Phase:** Phase 1+

#### Rule

Deposit, Withdrawal, Transfer, and similar user-requested cash movement Amounts must be greater than `0`.

Ledger Entry signed Amounts are an exception because they intentionally represent both increases and decreases.

---

### BR-XDM-004 — Order Quantity Is a Positive Integer

**Status:** Confirmed  
**Phase:** Phase 3

#### Rule

Buy and Sell Order Quantity must be a positive integer.

---

## Atomicity Rules

### BR-XDM-005 — Financial Operations Are All-or-Nothing

**Status:** Confirmed  
**Phase:** Phase 1+

#### Rule

A financial business operation either succeeds completely or leaves the relevant business state unchanged.

Partial updates across balances, Positions, Orders, Executions, and Ledger records are prohibited.

---

### BR-XDM-006 — Failure Must Not Produce Partial Financial State

**Status:** Confirmed  
**Phase:** Phase 1+

#### Rule

When a business validation, state transition, concurrency, or transactional failure occurs, any business state that belongs to the failed operation must not be partially committed.

The specific technical rollback mechanism is deferred to architecture.

---

## Idempotency Rules

### BR-XDM-007 — Asset-Moving Operations Are Idempotent in Phase 5

**Status:** Planned Future Scope  
**Phase:** Phase 5

#### Rule

Asset-moving operations such as:

- Deposit;
- Withdrawal;
- Bank transfer;
- Bank-to-Securities transfer;
- Securities-to-Bank transfer;
- Buy Order;
- Sell Order;

must support idempotent request semantics.

Same Idempotency Key + same request:

- does not create a second asset movement;
- returns/reuses the original result.

Same Idempotency Key + different request:

- is rejected as a conflict.

Different keys represent distinct operations.

The concrete HTTP header, storage, retention period, and database constraints are deferred.

---

## Concurrency Guarantees

### BR-XDM-008 — Concurrent Operations Must Preserve Invariants

**Status:** Confirmed  
**Phase:** Phase 5 robustness

#### Rule

Concurrent operations must not violate domain invariants.

Examples:

- Bank Current Balance must not become negative;
- Securities Cash must not become negative;
- Position Quantity must not become negative;
- transfer or order limits, once introduced, must not be bypassed by races.

The choice among optimistic locking, pessimistic locking, transaction isolation, or other implementation mechanisms is not decided here.

---

## History Rules

### BR-XDM-009 — Historical Facts Are Append-Oriented

**Status:** Confirmed  
**Phase:** Phase 1+

#### Rule

Business records representing completed facts should normally remain immutable and append-oriented.

This includes:

- Ledger Transaction / Entry;
- Execution;
- persisted Market Price;
- completed Order history.

Position is different because it represents current state rather than historical fact.

---

## Reservation Semantics

### BR-XDM-010 — Reservation Exists Only for Meaningfully Pending Operations

**Status:** Confirmed  
**Phase:** Phase 6 introduction

#### Rule

Reserved Amount and Reserved Quantity are used only when an operation remains pending across a meaningful period or transaction boundary.

Phase 1-3 synchronous operations do not create transient reservation state merely for internal processing.

Phase 6 long-lived Orders may introduce:

- Reserved Amount for pending Buy Orders;
- Reserved Quantity / Available Quantity for pending Sell Orders.

---

## Implementation Boundary

### BR-XDM-011 — Business Semantics Are Separated from Technical Mechanisms

**Status:** Confirmed  
**Phase:** All

#### Rule

Business Rules specify required behavior and invariants, not the concrete mechanism used to implement them.

The following remain later design decisions:

- Java numeric types;
- JPA mappings;
- database schemas;
- `UNIQUE` / `CHECK` constraints;
- lock strategy;
- isolation level;
- transaction annotation placement;
- API shape and HTTP status codes.
