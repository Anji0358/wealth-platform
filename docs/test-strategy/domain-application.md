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

Application tests verify the externally meaningful result of Use Case coordination such as:

```text
load
↓
domain operation
↓
cross-domain coordination
↓
save
```

Do not assert every internal call or call order merely because the implementation currently uses these steps.

Verify a collaboration directly only when that collaboration is part of the Use Case responsibility or is the only meaningful way to observe the required outcome.

---

### TS-APP-002 — Repository Ports and Test Doubles Emerge from Current Need

Do not create a Repository port or Test Double in anticipation of later Application tests.

First identify the current behavior to verify. If verifying that behavior requires isolating persistence or observing a save/load collaboration, introduce the smallest boundary required by the current test.

A Repository interface may emerge:

- during Refactor, when the current code reveals a persistence boundary or inappropriate dependency;
- or during Red, when the current behavior cannot be tested without a seam to persistence.

When a real database is unnecessary for the current orchestration test, a lightweight Stub, Fake, or Spy repository may be used.

The Test Double must implement only what the current test needs. Do not add stored entities, counters, lookup behavior, verification methods, or failure modes for hypothetical future tests.

Defining the Repository interface, creating a Test Double, and creating the production persistence implementation are separate decisions and should not be combined automatically into one task.

---

### TS-APP-003 — Mockito Is Optional at Boundaries

Mockito may be appropriate for collaborators such as:

- external ports;
- repositories;
- clock/random adapters when needed.

Do not mock primitive domain values or trivial Domain Objects.

Do not select Mockito automatically because a collaborator is represented by an interface.

While learning, prefer a small handwritten Test Double when it makes the dependency role and observed behavior easier to understand. Use Mockito when it makes the current test clearer or avoids disproportionate Test Double code.

Whether handwritten or framework-generated, verify only interactions that matter to the Use Case behavior. Avoid coupling the test to incidental implementation calls.

---

### TS-APP-004 — Failure Cases Reflect Use Case Postconditions

Failure-case tests should verify the same no-change guarantees documented in Use Case Postconditions and Business Rules.

---

### TS-APP-005 — UC/BR IDs Are Traceable but Not Forced into Every Test Name

Tests do not need names like `UC-BNK-003_BR-BNK-005_test1`.

Traceability may instead be maintained through class organization, comments, or documentation.

---

### TS-APP-006 — ActorContext Ownership Is Tested at the Application Boundary

Application tests verify that the Acting Customer can access owned resources, a different Acting Customer is rejected without state change, and a path/body target `customerId` cannot impersonate another Customer.

Ownership failure creates no balance, Position, Order, Execution, or Ledger effect. Tests use `ActorContext` directly and do not model HTTP headers.

---

### TS-APP-007 — Disabled Security Capabilities Are Tested as a Matrix

Application tests for BR-MKT-012 and BR-MKT-013 verify that DISABLED rejects a new Buy Order while allowing an eligible Sell Order, Portfolio inclusion, Order/Execution history, Market Price history, and direct Security read.

### Confirmed Rule Coverage Additions

| Business Rule | Primary Test Responsibility |
| --- | --- |
| BR-CUS-003 | Customer provisioning Application/API tests |
| BR-CUS-004 | TS-APP-006 and TS-API-005 |
| BR-MKT-012 | TS-APP-007 plus Buy/Sell and Portfolio tests |
| BR-MKT-013 | TS-API-006 and Security query integration tests |
