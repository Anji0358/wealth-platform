# Brokerage Business Rules

## Invariants

### BR-BRK-001 — One Securities Account per Customer Initially

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

A Customer may own:

`0..1 Securities Account`

in the initial scope.

A request to open a second Securities Account for the same Customer is rejected.

#### Related Use Cases

- UC-BRK-001 Open Securities Account

---

### BR-BRK-002 — Securities Cash Must Remain Non-Negative

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

For Securities Account cash:

- `Current Balance >= 0`
- `Reserved Amount >= 0`
- `Reserved Amount <= Current Balance`
- `Available Balance = Current Balance - Reserved Amount`
- `Available Balance >= 0`

Margin trading is outside the initial scope.

---

### BR-BRK-003 — Position Quantity Must Remain Non-Negative

**Status:** Confirmed  
**Phase:** Phase 3

#### Rule

A Position Quantity must never become negative.

In Phase 3, a Sell Order Quantity must not exceed the current Position Quantity.

Short selling is outside the initial scope.

---

### BR-BRK-004 — Position Represents Current Holdings Only

**Status:** Confirmed  
**Phase:** Phase 3

#### Rule

A Position represents current holdings, not historical holdings.

When a full sale reduces Quantity to zero, the current Position ceases to exist.

Past ownership and trading facts remain traceable through Order and Execution records and related Ledger history.

A dedicated Position history model may be added later if needed.

---

## Account State Rules

### BR-BRK-005 — Securities Account Initial State

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

A newly opened Securities Account begins with:

- `Current Balance = 0`
- `Reserved Amount = 0`
- no Positions;
- `Status = ACTIVE`.

---

### BR-BRK-006 — Restricted Securities Account Behavior

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

A `RESTRICTED` Securities Account may:

- view account information;
- view Orders;
- view Executions;
- view Positions;
- transfer cash from Securities Account to an eligible Bank Account owned by the same Customer.

A `RESTRICTED` Securities Account may not:

- place a new Buy Order;
- place a new Sell Order;
- receive a Bank-to-Securities cash transfer;
- close the account.

---

### BR-BRK-007 — Securities Account Closure Conditions

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

A Securities Account may close only when:

- Status is `ACTIVE`;
- Current Balance is `0`;
- Reserved Amount is `0`;
- no Position exists;
- no unfinished Order exists.

The valid closure transition is:

`ACTIVE -> CLOSED`

A `RESTRICTED` account must first return to `ACTIVE`.

`CLOSED` is terminal.

#### Related Use Cases

- UC-BRK-011 Close Securities Account
- UC-ADM-003 Restrict Securities Account
- UC-ADM-004 Unrestrict Securities Account

---

## Cash Transfer Rules

### BR-BRK-008 — Bank and Securities Accounts Must Share the Same Customer

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

A direct Bank-to-Securities or Securities-to-Bank cash transfer is allowed only when both accounts are owned by the same Customer.

A Customer A Bank Account cannot directly fund Customer B's Securities Account.

Cross-customer cash movement must use a Banking transfer use case.

---

### BR-BRK-009 — Bank-to-Securities Transfer State Requirements

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

For Bank-to-Securities transfer:

- source Bank Account must be `ACTIVE`;
- destination Securities Account must be `ACTIVE`;
- amount must be positive;
- source Available Balance must be sufficient.

The transfer is atomic with its Ledger Transaction.

---

### BR-BRK-010 — Securities-to-Bank Transfer State Requirements

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

For Securities-to-Bank transfer:

- source Securities Account may be `ACTIVE` or `RESTRICTED`;
- destination Bank Account may be `ACTIVE` or `FROZEN`;
- neither account may be `CLOSED`;
- amount must be positive;
- source Available Balance must be sufficient.

A `RESTRICTED` account may therefore allow asset exit without allowing new risk-taking.

---

## Order Rules

### BR-BRK-011 — Phase 3 Uses Immediate Full-Fill Market Orders

**Status:** Confirmed  
**Phase:** Phase 3

#### Rule

Phase 3 supports Market Orders that are fully executed immediately in the same business operation.

A successful Order follows:

`SUBMITTED -> FILLED`

There is exactly one Execution per successful Phase 3 Order.

`Execution Quantity = Order Quantity`

Limit Orders, Partial Fills, long-lived Orders, and cancellation are deferred.

---

### BR-BRK-012 — Trading Requires Current Business-Day Market Price

**Status:** Confirmed  
**Phase:** Phase 3

#### Rule

A Buy or Sell Order is accepted only when:

- the current date is a Business Day;
- a persisted Market Price for that same Business Day exists for the Security.

A fresh random price is never generated at Order time.

Weekend Orders and Business-Day Orders before that day's Market Price generation are rejected.

---

### BR-BRK-013 — Disabled Security Buy/Sell Behavior

**Status:** Confirmed  
**Phase:** Phase 3

#### Rule

When a Security is `DISABLED`:

- new Buy Orders are prohibited;
- Sell Orders that reduce an existing Position are allowed;
- historical Order, Execution, Position, and Market Price information remains available.

---

### BR-BRK-014 — Failed Order Request Does Not Create a Rejected Order Record Initially

**Status:** Confirmed  
**Phase:** Phase 3

#### Rule

If an Order request fails a Business Rule before becoming a valid Order:

- no Order is created;
- no Execution is created;
- no cash balance changes;
- no Position changes;
- no Ledger Transaction is created.

A dedicated rejected-request or audit model may be added later.

---

### BR-BRK-015 — Buy Order Requires Sufficient Securities Cash

**Status:** Confirmed  
**Phase:** Phase 3

#### Rule

For Phase 3 Buy Orders:

`Execution Amount = Execution Price × Execution Quantity`

The Securities Account must have sufficient Available Balance for the full Execution Amount.

Fees and taxes are excluded from the initial calculation.

---

### BR-BRK-016 — Sell Order Requires Sufficient Position Quantity

**Status:** Confirmed  
**Phase:** Phase 3

#### Rule

For Phase 3 Sell Orders:

`Sell Quantity <= Current Position Quantity`

Phase 3 does not use Reserved Quantity because the Market Order executes atomically.

---

## Execution and Position Rules

### BR-BRK-017 — Phase 3 Buy Is Atomic

**Status:** Confirmed  
**Phase:** Phase 3

#### Rule

A successful Buy operation atomically:

- creates the Order;
- creates its Execution;
- decreases Securities Account cash;
- creates or updates the Position;
- creates the corresponding Ledger Transaction.

Partial success is prohibited.

---

### BR-BRK-018 — Phase 3 Sell Is Atomic

**Status:** Confirmed  
**Phase:** Phase 3

#### Rule

A successful Sell operation atomically:

- creates the Order;
- creates its Execution;
- reduces the Position;
- increases Securities Account cash;
- creates the corresponding Ledger Transaction;
- determines the Realized P/L for that sale.

Partial success is prohibited.

---

### BR-BRK-019 — Average Cost Is the Initial Cost Basis Method

**Status:** Confirmed  
**Phase:** Phase 4

#### Rule

The initial acquisition-cost method is Average Cost.

FIFO is not used initially.

When additional cost-basis methods are introduced, a strategy-oriented design may be considered rather than introducing that abstraction prematurely.

---

### BR-BRK-020 — Average Acquisition Price May Be Fractional

**Status:** Confirmed  
**Phase:** Phase 4

#### Rule

Cash, Ledger Amount, and persisted Market Price use whole-JPY semantics in the initial scope.

Average Acquisition Price may contain fractional JPY and must not be forced to whole JPY at every purchase.

The exact precision and Java/database representation are deferred to `data-model.md`.

---

### BR-BRK-021 — Remaining Acquisition Cost Preserves Cost-Basis Integrity

**Status:** Confirmed  
**Phase:** Phase 4

#### Rule

Conceptually, a Position maintains the relationship among:

- Quantity;
- Remaining Acquisition Cost;
- Average Acquisition Price.

Average Acquisition Price is conceptually:

`Remaining Acquisition Cost / Quantity`

Which values are persisted versus derived is deferred to `data-model.md`.

---

### BR-BRK-022 — Partial Sale Uses Average Cost Allocation

**Status:** Confirmed  
**Phase:** Phase 4

#### Rule

A partial sale allocates acquisition cost using the Average Cost method.

The remaining Position retains the appropriate remaining acquisition cost.

When the final shares are sold, all remaining acquisition cost is allocated so rounding residue does not accumulate indefinitely.

The exact decimal precision and rounding implementation are deferred to the data-model design.

## Planned Future Scope

- Limit Orders.
- Cancel Order.
- Partial Execution.
- `1 Order : N Executions`.
- Reserved Amount for pending Buy Orders.
- Reserved Quantity and Available Quantity for pending Sell Orders.
- FIFO cost basis.

## Out of Initial Scope

- Margin trading.
- Short selling.
- Trading fees.
- Taxes.
