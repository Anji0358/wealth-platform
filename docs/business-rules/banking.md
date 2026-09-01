# Banking Business Rules

## Invariants

### BR-BNK-001 — Cash Balances Must Remain Non-Negative

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

For every Bank Account:

- `Current Balance >= 0`
- `Reserved Amount >= 0`
- `Reserved Amount <= Current Balance`
- `Available Balance = Current Balance - Reserved Amount`
- `Available Balance >= 0`

Negative Bank Account balances are prohibited.

#### Rationale

Overdraft is outside the initial scope.

---

### BR-BNK-002 — Bank Account Initial State

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

A newly opened Bank Account begins with:

- `Current Balance = 0`
- `Reserved Amount = 0`
- `Status = ACTIVE`

#### Related Use Cases

- UC-BNK-001 Open Bank Account

---

### BR-BNK-003 — Bank Account Closure Conditions

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

A Bank Account may be closed only when all of the following hold:

- Status is `ACTIVE`;
- Current Balance is `0`;
- Reserved Amount is `0`;
- no unfinished cash movement involving the account exists.

The valid closure transition is:

`ACTIVE -> CLOSED`

A `FROZEN` account must first return to `ACTIVE`.

`CLOSED` is a terminal state.

#### Violation Result

If any closure condition fails, the account remains unchanged.

#### Related Use Cases

- UC-BNK-008 Close Bank Account
- UC-ADM-001 Freeze Bank Account
- UC-ADM-002 Unfreeze Bank Account

---

## Validation Rules

### BR-BNK-004 — Deposit Amount Must Be Positive

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

A Deposit Amount must be greater than `0`.

#### Violation Result

No balance or Ledger state changes.

#### Related Use Cases

- UC-BNK-002 Deposit Money

---

### BR-BNK-005 — Withdrawal Amount Must Be Positive and Available

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

A Withdrawal Amount must:

- be greater than `0`;
- not exceed the Bank Account's Available Balance.

#### Violation Result

If the rule fails:

- Current Balance does not change;
- Reserved Amount does not change;
- no Ledger Transaction is created.

#### Related Use Cases

- UC-BNK-003 Withdraw Money

---

### BR-BNK-006 — Transfer Amount Must Be Positive and Available

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

A Transfer Amount must:

- be greater than `0`;
- not exceed the source Bank Account's Available Balance.

A Bank Account cannot transfer money to itself.

#### Violation Result

No source balance, destination balance, or Ledger state changes.

#### Related Use Cases

- UC-BNK-004 Transfer Between Own Accounts
- UC-BNK-005 Transfer to Another Customer

---

## Account Type Rules

### BR-BNK-007 — Ordinary Deposit Account Behavior

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

`ORDINARY_DEPOSIT` supports:

- Deposit;
- Withdrawal;
- transfer between the Customer's own Bank Accounts;
- transfer to another Customer;
- incoming transfers.

---

### BR-BNK-008 — Savings Account Behavior

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

`SAVINGS` supports:

- Deposit;
- Withdrawal;
- transfer to another Bank Account owned by the same Customer;
- incoming transfers.

A `SAVINGS` account cannot be used as the source account for a transfer to another Customer.

#### Violation Result

A prohibited outgoing transfer does not change balances and creates no Ledger Transaction.

---

## State Transition Rules

### BR-BNK-009 — Frozen Bank Account Behavior

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

A `FROZEN` Bank Account may:

- view balance information;
- view transaction history;
- receive Deposits;
- receive incoming transfers.

A `FROZEN` Bank Account may not:

- withdraw money;
- initiate transfers;
- initiate Bank-to-Securities cash transfers;
- close the account.

`FROZEN` is therefore primarily a restriction on outgoing cash movement.

#### Related Use Cases

- UC-ADM-001 Freeze Bank Account
- UC-ADM-002 Unfreeze Bank Account

---

### BR-BNK-010 — Closed Bank Account Behavior

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

A `CLOSED` Bank Account may be used only for historical viewing.

It cannot participate as the source or destination of a new financial operation.

No transition out of `CLOSED` is allowed.

---

### BR-BNK-011 — Valid Bank Account Status Transitions

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

Valid transitions are:

- `ACTIVE -> FROZEN`
- `FROZEN -> ACTIVE`
- `ACTIVE -> CLOSED` when closure conditions are satisfied.

Undefined transitions are rejected.

Examples of rejected transitions include:

- `CLOSED -> ACTIVE`
- `FROZEN -> CLOSED`
- `ACTIVE -> ACTIVE`
- `FROZEN -> FROZEN`

---

## Transaction Rules

### BR-BNK-012 — Transfer Is Atomic

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

A successful Bank Account transfer must atomically:

- decrease the source Bank Account balance;
- increase the destination Bank Account balance;
- create exactly one corresponding Ledger Transaction.

Partial success is prohibited.

#### Example

For a 10,000 JPY transfer:

- Source: `-10,000`
- Destination: `+10,000`
- Ledger Transaction: one zero-sum transaction describing the same movement.

---

### BR-BNK-013 — Frozen Destination May Receive Transfer

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

A transfer from an eligible source Bank Account to a `FROZEN` destination Bank Account is allowed.

A `FROZEN` Bank Account cannot be the source of an outgoing transfer.

---

### BR-BNK-014 — Phase 1 Synchronous Cash Movements Do Not Use Reservation

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

The following synchronous operations do not use Reserved Amount:

- Deposit;
- Withdrawal;
- Bank-to-Bank Transfer;
- Bank-to-Securities Transfer;
- Securities-to-Bank Transfer.

They complete atomically within one database transaction.

Reserved Amount is reserved for later operations with a meaningful pending state.

## Planned Future Scope

- Per-transfer limit.
- Daily transfer limit.
- Reserved Amount for long-lived pending operations.
- Scheduled transfers.
- External-bank transfers.
- Transfer fees.

Per-transfer and daily transfer limits should preferably be introduced together as an advanced SQL and concurrency exercise.
