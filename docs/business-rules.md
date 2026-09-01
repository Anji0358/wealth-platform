# Business Rules

## Purpose

This document is the entry point for the business rules of the wealth-platform project.

Business rules define:

- what operations are allowed or prohibited;
- which invariants must always hold;
- which state transitions are valid;
- what must remain unchanged when an operation fails;
- which business facts must be recorded when a financial operation succeeds.

Implementation details such as Java types, JPA mappings, database tables, lock strategies, isolation levels, HTTP endpoints, and HTTP status codes are intentionally deferred to later design documents.

## Rule Status

- **Confirmed**: fixed for the current scope.
- **Planned Future Scope**: intended to be implemented in a later phase.
- **Future Consideration**: a possible future extension whose adoption is not yet fixed.
- **Open Question**: a decision still required before the related scope is implemented.

## Rule ID Prefixes

| Domain | Prefix |
|---|---|
| Customer | `BR-CUS-` |
| Banking | `BR-BNK-` |
| Brokerage | `BR-BRK-` |
| Ledger | `BR-LDG-` |
| Market | `BR-MKT-` |
| Portfolio | `BR-PFL-` |
| Administration | `BR-ADM-` |
| Cross-Domain | `BR-XDM-` |

## Documents

- [Customer Rules](business-rules/customer.md)
- [Banking Rules](business-rules/banking.md)
- [Brokerage Rules](business-rules/brokerage.md)
- [Ledger Rules](business-rules/ledger.md)
- [Market Rules](business-rules/market.md)
- [Portfolio Rules](business-rules/portfolio.md)
- [Administration Rules](business-rules/administration.md)
- [Cross-Domain Rules](business-rules/cross-domain.md)
- [Open Questions and Future Scope](business-rules/open-questions.md)

## Business Rule Format

Each rule uses the following structure where applicable:

- ID / Title
- Status
- Phase
- Rule
- Rationale
- Example
- Violation Result
- Related Use Cases
- Open Questions

`Rule` is mandatory. Other fields are included when they improve traceability or clarify behavior.

## Domain Boundaries

The current business domains are:

- Customer
- Banking
- Brokerage
- Ledger
- Market
- Portfolio
- Administration

These are business-domain boundaries and possible future Bounded Context candidates. Their exact implementation boundaries are deferred to `data-model.md` and `architecture.md`.

## Implementation Boundary

The following do **not** belong in this business-rules specification:

- placement of `@Transactional`;
- JPA Entity structure;
- database table structure;
- database `UNIQUE` / `CHECK` constraints;
- optimistic or pessimistic locking;
- transaction isolation level;
- Java numeric types;
- API endpoint structure;
- HTTP status codes.

Those decisions belong in `data-model.md`, `architecture.md`, `sql-strategy.md`, and `api-spec.md`.
