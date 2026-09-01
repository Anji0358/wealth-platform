# Concurrency, SQL, and Database Boundaries

## ARCH-058 — Single PostgreSQL Database

**Status:** Confirmed

All initial modules use one PostgreSQL database.

This allows cross-module ACID transactions for financial operations.

---

## ARCH-059 — Concurrency Mechanism Is Deferred Until Phase 5

**Status:** Confirmed

Do not prematurely fix one mechanism before tests reveal the problem.

Candidates include:

```text
Optimistic Locking
Pessimistic Locking
Atomic SQL Update
Transaction Isolation
```

The final approach may differ by use case.

---

## ARCH-060 — Concurrency Guarantee Is Not Deferred

**Status:** Confirmed

Regardless of mechanism, concurrent requests must preserve:

```text
Balance >= 0
Position Quantity >= 0
No duplicate financial effect
Ledger consistency
```

Requirement and mechanism remain separate decisions.

---

## ARCH-061 — Persistence Version Is Not a Public Domain/API Concept by Default

**Status:** Confirmed

If optimistic locking uses a persistence `version` field, that does not automatically make `version` part of the domain language or public API.

Expose concurrency metadata only if a client-facing use case later requires it.

---

## ARCH-062 — SQL Is a First-Class Implementation Option

**Status:** Confirmed

The project should not hide all persistence behavior behind generated ORM operations.

Appropriate tools may include:

```text
JPA
JPQL
native SQL
JdbcClient
```

depending on the problem.

`sql-strategy.md` defines usage guidance.

---

## ARCH-063 — Query Performance and N+1 Are Architecture Concerns

**Status:** Confirmed

Query design must consider N+1 behavior.

Possible solutions include:

```text
JOIN
projection
fetch strategy
dedicated SQL
```

Do not solve N+1 by blindly switching all relationships to eager loading.

---

## ARCH-064 — Migration History and Application Schema Must Agree

**Status:** Confirmed

Application code should run against a schema reproducible from versioned migrations.

Schema drift caused by undocumented manual changes is not acceptable normal practice.
