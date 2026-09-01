# API Common Conventions

## 1. Protocol and Representation

### API-001 — REST and JSON

**Status:** Confirmed

The external application API uses HTTP with JSON request and response bodies unless an endpoint explicitly states otherwise.

---

### API-002 — Versioned Base Path

**Status:** Confirmed

All initial endpoints use:

```text
/api/v1
```

A breaking contract redesign may later use another major path such as `/api/v2`.

---

### API-003 — URL Naming

**Status:** Confirmed

Resource URLs use:

- lowercase;
- kebab-case where multiple words are required;
- plural resource nouns.

Examples:

```text
/customers
/bank-accounts
/securities-accounts
/market-prices
```

---

### API-004 — JSON Naming

**Status:** Confirmed

JSON field names use camelCase.

Example:

```json
{
  "currentBalance": 100000,
  "reservedAmount": 0,
  "availableBalance": 100000
}
```

---

## 2. Resource and Command Semantics

### API-005 — Domain Commands Need Not Be CRUD

**Status:** Confirmed

Business operations whose meaning is clearer as explicit commands should use command-oriented subresources or actions.

Examples:

```text
POST /bank-accounts/{id}/deposits
POST /bank-accounts/{id}/withdrawals
POST /bank-accounts/{id}/close
```

The API must not force a domain operation into `PUT`, `PATCH`, or `DELETE` solely to appear CRUD-like.

---

### API-006 — Close Is Not Delete

**Status:** Confirmed

Closing a Bank Account or Securities Account changes domain state and preserves history.

Use explicit commands such as:

```text
POST /bank-accounts/{id}/close
POST /securities-accounts/{id}/close
```

Do not expose close as HTTP `DELETE`.

---

### API-007 — Persistence Entities Are Not API Contracts

**Status:** Confirmed

JPA entities or persistence records must not be serialized directly as the public API contract.

Use dedicated Request/Response DTOs or equivalent API models.

This prevents accidental coupling between:

- database schema;
- persistence framework behavior;
- public API compatibility.

---

## 3. Data Representation

### API-008 — UUID Representation

**Status:** Confirmed

UUID identifiers are represented as JSON strings.

Example:

```json
{
  "customerId": "550e8400-e29b-41d4-a716-446655440000"
}
```

---

### API-009 — Monetary Amounts Use Integer JPY

**Status:** Confirmed

Whole-JPY amounts are represented as JSON integers.

Example:

```json
{
  "amount": 10000
}
```

means 10,000 JPY.

Do not represent initial-scope money as:

```json
10000.00
```

because fractional JPY is not part of the initial monetary model.

---

### API-010 — Timestamps Use ISO 8601

**Status:** Confirmed

Instant-based timestamps use ISO 8601.

Preferred external form is UTC:

```text
2026-09-01T08:30:00Z
```

---

### API-011 — Market Business Date Uses Calendar Date

**Status:** Confirmed

Market Business Date is represented as:

```text
YYYY-MM-DD
```

Example:

```text
2026-09-01
```

This is distinct from a timestamp.

---

### API-012 — Market Business-Day Semantics Use Asia/Tokyo

**Status:** Confirmed

The initial JPY-based simulated market evaluates Business Day and same-day Trading availability using the `Asia/Tokyo` calendar date.

Persistent instant timestamps remain timezone-aware and should be stored as actual instants.

This rule should remain consistent with `market-simulation.md`.

---

## 4. Derived and Historical Values

### API-013 — Derived Values May Be Returned

**Status:** Confirmed

The API may return derived values even when they are not database columns.

Examples include:

- Available Balance;
- Average Acquisition Price;
- Position Valuation;
- Unrealized P/L;
- Total Assets.

---

### API-014 — Historical Values Are Not Recomputed from Current State

**Status:** Confirmed

Historical facts must return the values fixed when the event occurred.

Examples:

- Execution Price;
- Execution Quantity;
- Sell Execution Realized P/L;
- Market Price for a historical Business Day.

The API must not recalculate those facts from today's Market Price or current Position state.

---

## 5. Use Case Mapping

### API-017 — Development Acting Identity Uses a Dedicated Header

**Status:** Confirmed

Customer-initiated requests in the learning environment use:

```text
X-Acting-Customer-Id: <Customer UUID>
```

This header is a trusted development-only identity input, not production Authentication or Authorization.

Presentation converts the header into an `ActorContext`. Application use cases depend on `ActorContext`, not on HTTP header names or Spring MVC types.

A `customerId` in a path, query, or request body identifies a target only and never proves caller identity. Application use cases validate ownership against the Acting Customer.

---

### API-015 — Endpoint and Use Case Mapping Is Not Necessarily One-to-One

**Status:** Confirmed

Multiple related Use Cases may share one endpoint if the same external operation cleanly represents them.

Example:

```text
UC-BNK-004 Transfer Between Own Accounts
UC-BNK-005 Transfer to Another Customer

↓ shared API

POST /bank-accounts/{sourceAccountId}/transfers
```

The Business Rules determine whether source and destination belong to the same Customer.

---

### API-016 — API Sections Include Related Use Cases

**Status:** Confirmed

Each domain endpoint should identify the relevant Use Case IDs where practical.

This preserves documentation traceability without forcing endpoint count to equal Use Case count.
