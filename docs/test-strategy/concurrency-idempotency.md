# Concurrency, Atomicity, and Idempotency

## Atomicity

### TS-CON-001 — Failure Atomicity Is Tested Explicitly

Introduce controlled failure during a financial Use Case and verify no partial state remains.

Example:

```text
Source balance update
↓
forced failure
↓
Destination / Ledger / source update all rolled back
```

---

### TS-CON-002 — Transaction Rollback Is a Required Integration Test

Do not assume `@Transactional` usage is correct merely because code compiles or happy-path tests pass.

---

### TS-CON-003 — Production Code Must Not Contain Test-Only Branches

Failure injection should use seams such as dependency injection or dedicated test adapters.

Do not add:

```java
if (testMode) { ... }
```

to production business logic.

---

## Concurrency

### TS-CON-004 — Reproduce the Race Before Fixing It

**Status:** Phase 5

Example:

```text
Balance = 100000

Withdrawal A = 80000
Withdrawal B = 80000
```

First demonstrate how a naive implementation can violate the invariant.

Then introduce concurrency control.

---

### TS-CON-005 — Compare Concurrency Strategies

Candidate approaches include:

- optimistic locking;
- pessimistic locking;
- atomic SQL update;
- transaction isolation.

Experiments should record both correctness and operational behavior.

---

### TS-CON-006 — Avoid Sleep-Based Concurrency Tests

Use coordination primitives such as:

```text
CountDownLatch
CyclicBarrier
```

to make the race window deliberate and reproducible.

---

### TS-CON-007 — Concurrency Assertions Focus on Invariants

Examples:

```text
final balance >= 0
exactly one operation succeeds
Ledger count matches successful operations
no duplicate financial effect
```

---

### TS-CON-008 — Isolation-Level Experiments Are Included

Phase 5 should include experiments with PostgreSQL transaction isolation such as:

```text
READ COMMITTED
REPEATABLE READ
SERIALIZABLE
```

The goal is to observe behavior, not merely memorize definitions.

---

### TS-CON-009 — Lock Behavior Is Observed

For pessimistic locking experiments, observe behavior such as:

```sql
SELECT ... FOR UPDATE
```

and verify that competing transactions wait or conflict as expected.

---

## Idempotency

### TS-IDEM-001 — Same Key and Same Request Produces One Effect

Phase 5 tests send the same logical request twice with the same Idempotency Key and verify:

- one asset movement;
- one Ledger effect;
- one Execution where applicable;
- original response can be reused.

---

### TS-IDEM-002 — Same Key and Different Request Is Conflict

Verify:

```text
409
IDEMPOTENCY_CONFLICT
```

or the final agreed API mapping.

---

### TS-IDEM-003 — Market Price Generation Is Idempotent

Generating the same Security + Business Day twice must persist only one Market Price.

---

### TS-IDEM-004 — Catch-Up Ordering Is Tested

If Monday is missing and generation runs Tuesday:

```text
Monday
↓
Tuesday
```

must be created in order.
