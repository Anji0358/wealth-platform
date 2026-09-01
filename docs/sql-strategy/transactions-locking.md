# Transactions, Locks, and Concurrency

## SQL-TXN-001 — PostgreSQL Transaction Behavior Is a Learning Goal

Transaction behavior is verified empirically rather than learned only from definitions.

---

## SQL-TXN-002 — READ COMMITTED Is Studied

Observe PostgreSQL's default isolation behavior through concurrent test sessions.

---

## SQL-TXN-003 — REPEATABLE READ Is Studied

Compare what one transaction observes when another transaction commits changes.

---

## SQL-TXN-004 — SERIALIZABLE Is Studied

Observe serialization failures and understand how PostgreSQL can reject unsafe concurrent execution.

---

## SQL-TXN-005 — Isolation Choice Follows the Use Case

Do not globally increase isolation merely to avoid thinking about concurrency.

Evaluate the behavior and cost for relevant financial commands.

---

## SQL-TXN-006 — Pessimistic Row Locking Is an Experiment

Use:

```sql
SELECT ... FOR UPDATE
```

for controlled experiments and possible future command implementations.

Observe:

- blocking;
- lock duration;
- transaction ordering.

---

## SQL-TXN-007 — Optimistic Locking Is a Comparison Strategy

A persistence version field may be introduced during Phase 5 experiments.

The public Domain/API model need not expose it automatically.

---

## SQL-TXN-008 — Atomic Conditional UPDATE Is a Comparison Strategy

Example:

```sql
UPDATE bank_accounts
SET current_balance = current_balance - :amount
WHERE id = :id
  AND current_balance - reserved_amount >= :amount;
```

Rows affected can signal whether the financial precondition was satisfied atomically.

---

## SQL-TXN-009 — Compare Multiple Concurrency Mechanisms on the Same Problem

For selected operations, compare:

```text
optimistic locking
pessimistic locking
atomic UPDATE
SERIALIZABLE
```

Measure both correctness and operational cost.

---

## SQL-TXN-010 — Deadlocks Are an Advanced Learning Topic

Phase 5+ should include understanding:

- how conflicting lock order can cause deadlock;
- how PostgreSQL detects it;
- how transaction design can reduce risk.

---

## SQL-TXN-011 — Consistent Lock Ordering Is a Candidate Mitigation

For multi-account operations, experiment with acquiring locks in deterministic order such as account ID order.

Do not adopt it blindly before testing the intended implementation.

---

## SQL-TXN-012 — Deadlock Retry Is Bounded

If retry behavior is added, it must not be infinite.

Retry policy belongs to architecture/application resilience design.

---

## SQL-TXN-013 — Transactions Are Kept Focused

Do not keep a database transaction open across unnecessary:

- user interaction;
- HTTP waiting;
- slow unrelated processing.

---

## SQL-TXN-014 — Read-Only Transaction Is a Possible Optimization

Use read-only semantics where appropriate, but only after understanding actual framework/database behavior.

---

## SQL-TXN-015 — MVCC Is Part of the Phase 5 Learning Scope

Understand how PostgreSQL MVCC relates to:

- snapshots;
- concurrent reads;
- updates;
- vacuum;
- long-running transactions.

---

## SQL-TXN-016 — Long-Running Transactions Are Investigated

Learn how long transactions can affect:

- row-version cleanup;
- locking;
- database maintenance;
- application throughput.

---

## SQL-TXN-017 — Correctness Requirement Remains Independent of Mechanism

Whatever mechanism is selected, preserve:

```text
Balance >= 0
Position Quantity >= 0
Ledger consistency
No duplicate financial effect
```
