# Modules and Layers

## ARCH-007 — Package by Business Domain

**Status:** Confirmed

The top-level code organization uses business domains:

```text
customer
banking
brokerage
ledger
market
portfolio
administration
shared
```

Avoid project-wide technical folders such as:

```text
controller/
service/
repository/
entity/
```

as the primary organization.

---

## ARCH-008 — Layers Exist Inside a Business Domain

**Status:** Confirmed

A typical domain may contain:

```text
domain/
application/
presentation/
infrastructure/
```

The project therefore organizes code as:

```text
Business Domain
    ↓
Layer
```

not:

```text
Layer
    ↓
All Business Domains
```

---

## ARCH-009 — Layer Creation Is Not Mechanical

**Status:** Confirmed

Not every domain must contain every layer.

For example:

- `portfolio` may have application/query and presentation code without a persistent Portfolio aggregate;
- `ledger` may have no public presentation layer initially;
- `administration` may primarily orchestrate other domain capabilities.

Packages should reflect actual responsibilities.

---

## ARCH-010 — Presentation Layer Responsibility

**Status:** Confirmed

Presentation handles:

```text
HTTP Request
↓
Boundary Validation
↓
Application Use Case Invocation
↓
Response Mapping
```

Presentation must not contain core Business Rules.

---

## ARCH-011 — Application Layer Responsibility

**Status:** Confirmed

Application represents Use Case orchestration.

Typical responsibility:

```text
load required state
↓
invoke domain behavior
↓
coordinate multiple aggregates/modules
↓
persist changes
↓
return use-case result
```

It should not become a second domain layer full of duplicated business conditions.

---

## ARCH-012 — Domain Layer Responsibility

**Status:** Confirmed

Domain contains:

- invariants;
- valid state transitions;
- domain behavior;
- Value Objects where useful;
- Aggregate behavior;
- domain-oriented repository/port abstractions when appropriate.

It should remain independent from HTTP and persistence frameworks.

---

## ARCH-013 — Infrastructure Layer Responsibility

**Status:** Confirmed

Infrastructure handles technical mechanisms such as:

- JPA;
- Spring Data;
- JDBC;
- database mappings;
- persistence adapters;
- external clock/random implementations where relevant.

Infrastructure implements inward-facing ports rather than becoming the source of business rules.

---

## ARCH-014 — Cross-Domain Command Orchestration Lives in Application Layer

**Status:** Confirmed

Example:

```text
Bank → Securities Transfer

Application Use Case
├── BankAccount
├── SecuritiesAccount
└── Ledger
```

BankAccount should not directly invoke a SecuritiesAccount repository.

Cross-domain coordination remains explicit at the Use Case level.

---

## ARCH-015 — Domain Modules May Share a Database Without Sharing Responsibilities

**Status:** Confirmed

A single PostgreSQL database does not mean all modules freely manipulate all tables.

Each module should maintain clear ownership of its persistence responsibilities.

Read-side queries are allowed broader access where explicitly intended.
