# Domain and Application Tests

## Domain Tests

### TS-DOM-001 — Avoid Mocking the Domain Model

Use real Domain Objects and Value Objects wherever possible.

Mocks are primarily for boundaries, not simple domain collaborators.

---

### TS-DOM-002 — BankAccount Test Scope

Test at least:

- Deposit;
- Withdrawal;
- insufficient balance;
- FROZEN behavior;
- SAVINGS restrictions;
- close conditions;
- invalid state transitions;
- balance invariants.

---

### TS-DOM-003 — SecuritiesAccount Test Scope

Test at least:

- cash transfer eligibility;
- RESTRICTED behavior;
- close conditions;
- balance invariants.

---

### TS-DOM-004 — Position Test Scope

Test at least:

- Buy quantity increase;
- acquisition cost update;
- derived Average Acquisition Price;
- partial Sell;
- full Sell;
- insufficient Position;
- Realized P/L;
- Remaining Acquisition Cost.

---

### TS-DOM-005 — LedgerTransaction Test Scope

Test at least:

- fewer than two Entries rejected;
- non-zero-sum transaction rejected;
- normal transfer transaction accepted;
- Full Reversal;
- double reversal prohibited.

---

### TS-DOM-006 — GBM Test Inputs Are Deterministic

Test values such as:

```text
Z = 0
Z = 1
Z = -1
σ = 0
```

The same inputs must always produce the same calculation result.

---

### TS-DOM-007 — Time-Dependent Rules Use Fixed Time

Do not rely on the real clock in tests.

Use a fixed `Clock` or equivalent time source.

---

### TS-DOM-008 — Business-Day Boundaries Are Explicitly Tested

Test at least:

- Friday;
- Saturday;
- Sunday;
- Monday;
- Asia/Tokyo day-boundary conversion.

---

### TS-DOM-009 — Parameterized Tests for Rule Matrices

Use parameterized tests where they improve clarity for:

- invalid Amounts;
- account statuses;
- Security statuses;
- weekday/weekend cases;
- boundary values.

---

### TS-DOM-010 — Exception Tests Also Check State

An `assertThrows` result is not enough for financial failure behavior.

Also verify:

```text
balance unchanged
Position unchanged
Ledger unchanged
```

where applicable.

---

## Application Tests

### TS-APP-001 — Application Tests Verify Orchestration

Application tests verify Use Case coordination such as:

```text
load
↓
domain operation
↓
cross-domain coordination
↓
save
```

---

### TS-APP-002 — Repository Ports May Use Fake or Stub Implementations

Use lightweight fake/stub repository implementations where a real database is not necessary to verify Use Case orchestration.

---

### TS-APP-003 — Mockito Is Used at Boundaries

Mockito is appropriate for collaborators such as:

- external ports;
- repositories;
- clock/random adapters when needed.

Do not mock primitive domain values or trivial Domain Objects.

---

### TS-APP-004 — Failure Cases Reflect Use Case Postconditions

Failure-case tests should verify the same no-change guarantees documented in Use Case Postconditions and Business Rules.

---

### TS-APP-005 — UC/BR IDs Are Traceable but Not Forced into Every Test Name

Tests do not need names like `UC-BNK-003_BR-BNK-005_test1`.

Traceability may instead be maintained through class organization, comments, or documentation.
