# ADR-001 — Use a Modular Monolith

**Status:** Accepted  
**Date:** 2026-09-01

## Context

The wealth-platform project combines several financial domains:

- Customer;
- Banking;
- Brokerage;
- Ledger;
- Market;
- Portfolio;
- Administration.

The project also has explicit learning goals around:

- Test-Driven Development;
- Domain-Driven Design;
- design patterns;
- Spring;
- PostgreSQL;
- SQL;
- transaction boundaries;
- concurrency;
- financial consistency.

Several important Use Cases require changes across multiple domains within one financial operation.

Examples include:

```text
Bank transfer
→ source Bank Account
→ destination Bank Account
→ Ledger

Buy Order
→ Securities Account Cash
→ Position
→ Order
→ Execution
→ Ledger
```

These operations require strong atomicity.

A distributed architecture would introduce additional concerns such as:

- network failure;
- inter-service communication;
- message brokers;
- distributed consistency;
- service discovery;
- deployment topology;
- distributed tracing;
- distributed transactions or compensating operations.

Those concerns are useful in other systems, but they are not required to achieve the current project's primary learning objectives.

At the same time, a completely unstructured monolith would make domain boundaries difficult to understand and could encourage direct coupling between unrelated business areas.

## Decision

The project will use a **Modular Monolith**.

The initial architecture is:

```text
Single Spring Boot Application
+
Business Domain Modules
+
Single PostgreSQL Database
```

Top-level code is organized by business domain rather than only by technical layer.

Conceptually:

```text
customer
banking
brokerage
ledger
market
portfolio
administration
```

Each domain may internally contain layers such as:

```text
domain
application
presentation
infrastructure
```

The modules share one application process and one PostgreSQL database.

Cross-domain financial operations may therefore participate in one local ACID database transaction.

The modular boundaries are architectural responsibilities, not separate deployments.

## Alternatives Considered

### Alternative 1 — Traditional Layered Monolith

Example structure:

```text
controller/
service/
repository/
entity/
```

for the entire application.

#### Advantages

- simple initial structure;
- common Spring tutorial style;
- minimal package design.

#### Reasons Not Selected

This organization groups code by technical role rather than business meaning.

As the system grows, Banking, Brokerage, Ledger, and Market responsibilities can become mixed across large global folders.

It would make DDD-oriented module boundaries harder to learn and maintain.

---

### Alternative 2 — Microservices

Example:

```text
Banking Service
Brokerage Service
Ledger Service
Market Service
Portfolio Service
```

#### Advantages

- deployment independence;
- explicit runtime service boundaries;
- independent scaling;
- realistic distributed-systems learning.

#### Reasons Not Selected

Microservices would introduce substantial complexity unrelated to the current primary learning scope.

Financial operations that currently fit naturally inside one PostgreSQL transaction would require additional distributed-consistency mechanisms.

This would shift attention away from:

- domain modeling;
- SQL;
- local transaction semantics;
- concurrency;
- TDD.

Microservices may be reconsidered only if a future project requirement creates a concrete need.

## Consequences

### Positive

#### Clear business boundaries

Source code is organized around financial domains rather than generic technical folders.

#### Local ACID transactions

Important operations can atomically update:

```text
Banking + Ledger
Brokerage + Position + Ledger
```

inside one PostgreSQL transaction.

#### Lower operational complexity

The project does not require:

- service discovery;
- message infrastructure;
- distributed tracing;
- distributed deployment;
- distributed transaction design.

#### Better learning focus

Development can focus on the intended topics:

- DDD;
- TDD;
- Spring;
- SQL;
- transaction isolation;
- concurrency;
- financial invariants.

#### Future extraction remains possible

If a genuine need appears later, a well-maintained module may become a candidate for extraction into a separate service.

The project does not promise that extraction would be free or automatic.

### Negative / Trade-offs

#### Module boundaries are not physically enforced by deployment

Because everything runs in one process, developers could bypass intended module boundaries unless architecture discipline is maintained.

Package rules and later ArchUnit tests may help.

#### Shared database creates coupling risk

Modules share one PostgreSQL instance.

Poorly designed queries or direct cross-domain table manipulation could weaken ownership boundaries.

#### Independent deployment is unavailable

A single module cannot initially be deployed or scaled independently.

#### Large monolith risk remains

If responsibilities are not actively maintained, a Modular Monolith can degrade into an unstructured monolith.

This decision therefore requires continued attention to module ownership and dependency direction.

## Related Documents

- `docs/requirements.md`
- `docs/architecture.md`
- `docs/architecture/principles.md`
- `docs/architecture/modules-layers.md`
- `docs/architecture/transactions-repositories.md`
- `docs/non-functional-requirements.md`
