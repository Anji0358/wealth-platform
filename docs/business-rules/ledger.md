# Ledger Business Rules

## Invariants

### BR-LDG-001 — Every Cash Movement Has One Ledger Transaction

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

Every successful cash movement is represented by exactly one Ledger Transaction.

A Ledger Transaction contains at least two Ledger Entries.

---

### BR-LDG-002 — Ledger Transaction Must Be Zero-Sum

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

For every Ledger Transaction:

`SUM(Ledger Entry Amount) = 0`

Signed amounts are used:

- positive Amount increases the referenced account's cash;
- negative Amount decreases the referenced account's cash.

---

### BR-LDG-003 — Historical Ledger Facts Are Immutable

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

A historical Ledger Transaction or Ledger Entry must not be directly modified or deleted through normal business operations.

Corrections are represented by additional transactions.

---

## Counterparty Rules

### BR-LDG-004 — External Account Balances Deposits and Withdrawals

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

System-external Deposit and Withdrawal cash movements use a shared `External Account` as the Ledger counterparty.

#### Example

Deposit 10,000 JPY:

- External Account: `-10,000`
- Bank Account: `+10,000`

Withdrawal 3,000 JPY:

- Bank Account: `-3,000`
- External Account: `+3,000`

---

### BR-LDG-005 — Market Settlement Account Balances Securities Trades

**Status:** Confirmed  
**Phase:** Phase 3

#### Rule

Securities Buy and Sell cash settlement uses a dedicated `Market Settlement Account`.

#### Example

Buy for 10,000 JPY:

- Securities Account Cash: `-10,000`
- Market Settlement Account: `+10,000`

Sell for 12,000 JPY:

- Market Settlement Account: `-12,000`
- Securities Account Cash: `+12,000`

This is distinct from the External Account used for Deposit and Withdrawal.

---

### BR-LDG-006 — System Accounts Are Not Customer-Operable Accounts

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

`External Account` and `Market Settlement Account` are system Ledger counterparties.

Customers cannot select, open, close, or directly operate them through ordinary business use cases.

---

## Atomicity Rules

### BR-LDG-007 — Ledger and Balance Changes Succeed or Fail Together

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

A business operation that changes cash must atomically update the relevant business balance state and create the corresponding Ledger Transaction.

A balance change without the Ledger fact, or a Ledger fact without the corresponding balance change, is prohibited.

---

### BR-LDG-008 — Failed Financial Operation Creates No Ledger Transaction

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

If a financial operation fails before successful completion, no Ledger Transaction for that operation is created.

---

## Correction Rules

### BR-LDG-009 — Corrections Use Full Reversal

**Status:** Confirmed  
**Phase:** Later robustness/correction scope

#### Rule

To reverse an original Ledger Transaction, create a new Reversal Transaction whose Ledger Entries exactly negate all Ledger Entries of the original transaction.

The original Ledger Transaction remains unchanged.

If a corrected business transaction is required, create it separately after the full reversal.

---

### BR-LDG-010 — Reversal Must Itself Be Zero-Sum

**Status:** Confirmed  
**Phase:** Later robustness/correction scope

#### Rule

A Reversal Transaction is a Ledger Transaction and must satisfy all normal Ledger invariants, including the zero-sum rule.

It must retain a relation to the original transaction being reversed.

---

### BR-LDG-011 — Original Transaction Is Reversed At Most Once Initially

**Status:** Confirmed  
**Phase:** Later robustness/correction scope

#### Rule

An original Ledger Transaction may have at most one full Reversal Transaction in the initial correction model.

Double reversal of the same original transaction is prohibited.

Reversing a Reversal Transaction is not introduced initially.

If a later correction is required, represent it as a new correcting transaction rather than mutating history.

---

### BR-LDG-012 — Reversal and Balance Correction Are Atomic

**Status:** Confirmed  
**Phase:** Later robustness/correction scope

#### Rule

When a Reversal Transaction represents a cash correction, the corresponding balance correction and Reversal Ledger Transaction must succeed or fail together.
