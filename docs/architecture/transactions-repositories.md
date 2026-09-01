# Transactions and Repositories

## ARCH-023 — Application Use Case Defines Transaction Boundary

**Status:** Confirmed

A financial command should normally execute within one transaction covering the whole Use Case.

Recommended concept:

```text
@Transactional
Application Service / Use Case method
```

Controller methods are not the preferred transaction boundary.

---

## ARCH-024 — Do Not Commit Per Repository Operation

**Status:** Confirmed

This is prohibited for one financial Use Case:

```text
source.save()
COMMIT

destination.save()
COMMIT

ledger.save()
COMMIT
```

The correct architecture is conceptually:

```text
BEGIN
source update
destination update
ledger creation
COMMIT
```

---

## ARCH-025 — Cross-Domain Changes May Share One Database Transaction

**Status:** Confirmed

Because the application is a Modular Monolith with one PostgreSQL database, Use Cases may atomically update multiple modules.

Examples:

```text
Banking + Ledger

Brokerage + Ledger + Position
```

Distributed transactions are not required.

---

## ARCH-026 — Repository Interfaces Face Inward

**Status:** Confirmed

Domain/Application code depends on a repository or port abstraction.

Infrastructure implements it.

Example:

```text
banking/domain/BankAccountRepository

banking/infrastructure/JpaBankAccountRepositoryAdapter
```

---

## ARCH-027 — Spring Data Is Infrastructure Detail

**Status:** Confirmed

Domain/Application code should not directly depend on:

```java
JpaRepository<BankAccountJpaEntity, UUID>
```

Spring Data repositories remain inside Infrastructure.

---

## ARCH-028 — Repositories Are Aggregate-Oriented, Not Table-Oriented

**Status:** Confirmed

A repository should exist because the domain needs to load/save a consistency boundary.

Do not automatically create one repository per database table.

---

## ARCH-029 — Initial Aggregate Root Candidates

**Status:** Initial design

Initial candidates include:

```text
Customer
BankAccount
SecuritiesAccount
Security
Order
LedgerTransaction
Position
```

These are not immutable architectural law.

Aggregate boundaries may be refined as invariants and transaction pressure become clearer during implementation.

---

## ARCH-030 — BankAccount as Aggregate Root Candidate

**Status:** Confirmed initial direction

BankAccount protects behavior such as:

- non-negative balance;
- account state restrictions;
- Account Type restrictions;
- withdrawal eligibility;
- outgoing transfer eligibility.

---

## ARCH-031 — SecuritiesAccount as Aggregate Root Candidate

**Status:** Confirmed initial direction

SecuritiesAccount protects:

- cash state;
- status behavior;
- cash transfer eligibility;
- whether trading is allowed for the account.

---

## ARCH-032 — Position Is an Independent Aggregate Candidate

**Status:** Confirmed initial direction

SecuritiesAccount should not be required to load all Positions as a large in-memory collection.

Position is operated using the Securities Account + Security identity context.

---

## ARCH-033 — LedgerTransaction Owns LedgerEntry

**Status:** Confirmed

Conceptually:

```text
LedgerTransaction
└── LedgerEntries
```

LedgerTransaction should validate:

- at least two Entries;
- zero-sum total.

A standalone `LedgerEntryRepository` is not normally required.

---

## ARCH-034 — Order and Execution Remain Separate Concepts

**Status:** Confirmed

Even while Phase 3 executes exactly once immediately, Order and Execution remain separate models.

This preserves the later path to:

```text
1 Order : N Executions
```

without redefining the core concepts.
