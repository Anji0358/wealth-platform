# Architecture Decision Records

## Purpose

This directory contains Architecture Decision Records (ADRs) for the wealth-platform project.

An ADR records an important architectural decision together with:

- the context that created the decision;
- the chosen option;
- realistic alternatives;
- positive and negative consequences;
- related design documents.

ADRs exist to preserve **why** an architectural decision was made.

They do not replace requirements or architecture documentation.

## Responsibility of Each Document Type

```text
requirements.md
→ What the system must achieve

architecture.md
→ What the current architecture is

ADR
→ Why an important architectural choice was made
```

## When to Create an ADR

Create an ADR when a decision is likely to be reconsidered later and understanding the original reasoning would materially help.

Typical ADR subjects include:

```text
Modular Monolith adoption
Domain Model / Persistence Model separation
Concurrency-control strategy
Introduction of Event-Driven Architecture
PostgreSQL schema separation
Materialized View adoption
Major persistence strategy changes
```

Do not create an ADR for ordinary implementation details such as:

```text
private method extraction
test method renaming
minor package cleanup
DTO field renaming
small refactoring
```

## File Naming

Use sequential immutable IDs:

```text
ADR-001-modular-monolith.md
ADR-002-separate-domain-and-persistence-models.md
ADR-003-example-decision.md
```

Once an ADR number is assigned, it is never reused.

Rejected, Deprecated, or Superseded ADRs keep their original number.

## Status

Allowed statuses:

```text
Proposed
Accepted
Rejected
Superseded
Deprecated
```

### Proposed

The decision is still under consideration.

### Accepted

The decision is currently adopted by the project.

### Rejected

The proposal was evaluated but intentionally not adopted.

### Superseded

A newer ADR replaces the decision.

The old ADR remains in the repository and should reference the replacement.

Example:

```text
Status: Superseded by ADR-007
```

### Deprecated

The decision remains part of project history but is no longer recommended or relevant.

## ADR Template

```text
# ADR-XXX — Title

Status: Proposed | Accepted | Rejected | Superseded | Deprecated
Date: YYYY-MM-DD

## Context

Describe the architectural problem, constraints, and forces that created the need for a decision.

## Decision

State the selected architectural direction clearly.

## Alternatives Considered

### Alternative A

Describe the realistic alternative and why it was not selected.

### Alternative B

Describe another meaningful alternative if applicable.

## Consequences

### Positive

Describe benefits.

### Negative / Trade-offs

Describe costs, constraints, and new responsibilities created by the decision.

## Related Documents

List relevant requirements, architecture, test, SQL, or other ADR documents.
```

## ADR Writing Rules

1. `Context` explains the problem, not merely the selected answer.
2. `Decision` states one clear adopted direction.
3. `Alternatives Considered` contains realistic alternatives, not an exhaustive brainstorm.
4. `Consequences` includes disadvantages and operational cost as well as benefits.
5. ADRs should remain architectural rather than recording every code-level detail.
6. Existing Accepted ADRs should not normally be rewritten to make history look cleaner.
7. When architecture changes, create a new ADR and mark the old decision as Superseded where appropriate.
8. Significant architectural choices should reference the current architecture documentation.
9. Architecture documentation may link back to relevant ADRs.
10. Future possibilities are not ADRs until an actual architectural decision is needed.

## Current ADRs

| ID | Title | Status |
| --- | --- | --- |
| ADR-001 | Use a Modular Monolith | Accepted |
| ADR-002 | Separate Domain Models from Persistence Models | Accepted |

## Planned ADRs

No additional ADR is currently required.

In particular, the concurrency-control strategy is intentionally **not decided yet**.

The project will first compare approaches such as:

```text
Optimistic Locking
Pessimistic Locking
Atomic SQL Update
SERIALIZABLE
```

during the robustness/concurrency phase.

An ADR should be created only after those experiments produce enough evidence for a decision.
