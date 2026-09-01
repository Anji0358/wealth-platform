# API Specification

## Purpose

This document is the entry point for the HTTP API specification of the wealth-platform project.

The API is designed to expose the project's Use Cases without coupling the external contract directly to JPA entities or database tables.

The API specification defines:

- REST/JSON conventions;
- resource and command endpoint structure;
- request/response conventions;
- error semantics;
- pagination and filtering;
- idempotency behavior;
- API-to-Use-Case traceability;
- future endpoint evolution.

Implementation details such as Controller class structure, DTO mapping libraries, exception-handler classes, persistence mappings, and lock mechanisms are deferred to `architecture.md`.

## Base Path

```text
/api/v1
```

Breaking API changes may later introduce `/api/v2`.

Compatible additive changes should remain within the current major version where practical.

## Documents

- [Common Conventions](api-spec/common.md)
- [Customer and Banking API](api-spec/customer-banking.md)
- [Brokerage API](api-spec/brokerage.md)
- [Market and Portfolio API](api-spec/market-portfolio.md)
- [Administration API](api-spec/administration.md)
- [Error Semantics](api-spec/errors.md)
- [Pagination and Idempotency](api-spec/pagination-idempotency.md)
- [Future Scope](api-spec/future-scope.md)

## API Design Principles

1. REST + JSON is the default external protocol.
2. URLs represent resources using lowercase kebab-case and plural nouns.
3. JSON fields use camelCase.
4. Domain Commands are not forced into CRUD semantics when CRUD would distort the business meaning.
5. Closing, freezing, restricting, enabling, disabling, and cancelling are state transitions or commands, not physical deletion.
6. Request/Response DTOs are separate from persistence entities.
7. Use Cases and endpoints remain traceable, but are not required to map 1:1.
8. Derived values may appear in responses even when they are not persisted.
9. Historical facts are returned as the values fixed when the event occurred.
10. Phase-out-of-scope endpoints are not implemented prematurely.

## Phase Rule

An endpoint becomes implementation scope only when its related Use Case enters the current project phase.

The API documentation may reserve a future contract, but Controllers should not be added merely because a later feature is known.

## Traceability Direction

```text
Requirement
↓
Use Case
↓
Business Rule
↓
API
↓
Test
```

Endpoint sections include Related Use Cases where applicable.
