# API Error Semantics

## 1. General Principle

The API distinguishes:

- malformed or syntactically invalid input;
- resource absence;
- Business Rule violation;
- invalid state transition;
- idempotency conflict;
- concurrency conflict;
- unexpected infrastructure failure.

Clients should be able to react to stable application error codes without parsing human-readable messages.

---

## 2. Error Representation

### API-ERR-001 — Problem Details Style

**Status:** Confirmed

Error responses use a Problem Details-style JSON structure with an additional stable business `code`.

Example:

```json
{
  "type": "https://example.invalid/problems/business-rule-violation",
  "title": "Business rule violation",
  "status": 422,
  "code": "INSUFFICIENT_AVAILABLE_BALANCE",
  "detail": "Available balance is insufficient.",
  "instance": "/api/v1/bank-accounts/...",
  "traceId": "..."
}
```

The final production URI strategy for `type` may be simplified during implementation.

---

### API-ERR-002 — Do Not Expose Internal Exception Details

**Status:** Confirmed

Responses must not expose:

- Java stack traces;
- SQL statements;
- database exception internals;
- internal class names;
- secrets.

Logging may retain internal diagnostic context separately.

---

## 3. HTTP Status Mapping

### API-ERR-003 — 400 Bad Request

Use for request syntax or boundary-validation failures such as:

- malformed JSON;
- missing required fields;
- invalid primitive formats;
- invalid enum representation.

---

### API-ERR-004 — 404 Not Found

Use when the requested resource does not exist.

Examples:

- unknown Customer ID;
- unknown Bank Account ID;
- unknown Security ID.

---

### API-ERR-005 — 409 Conflict

Use for state/conflict conditions such as:

- invalid state transition;
- duplicate unique business resource where conflict semantics apply;
- second Securities Account for the same Customer;
- same Idempotency Key with different request;
- concurrency conflict.

Some exact mappings may be refined during implementation, but conflict semantics should remain distinguishable from ordinary validation.

---

### API-ERR-006 — 422 Unprocessable Content

Use for a syntactically valid request that violates an ordinary Business Rule.

Examples:

- insufficient Available Balance;
- Savings Account cannot transfer to another Customer;
- insufficient Position Quantity;
- Security disabled for Buy;
- current day is not a Business Day;
- current Business Day Market Price is unavailable;
- account closure conditions are not satisfied.

---

### API-ERR-007 — 500 Internal Server Error

Use for unexpected internal or infrastructure failure that cannot be represented as a known application conflict.

The response must remain sanitized.

---

## 4. Stable Error Codes

### API-ERR-008 — Business Error Code Catalog

Initial stable error-code candidates include:

```text
VALIDATION_FAILED
RESOURCE_NOT_FOUND

INSUFFICIENT_AVAILABLE_BALANCE
ACCOUNT_FROZEN
ACCOUNT_CLOSED
SECURITIES_ACCOUNT_RESTRICTED
SAVINGS_TRANSFER_NOT_ALLOWED
ACCOUNT_OWNERSHIP_MISMATCH
SAME_SOURCE_AND_DESTINATION_ACCOUNT

POSITION_QUANTITY_INSUFFICIENT
NOT_BUSINESS_DAY
MARKET_PRICE_NOT_AVAILABLE
SECURITY_DISABLED_FOR_BUY

ACCOUNT_CLOSURE_CONDITIONS_NOT_MET
SECURITIES_ACCOUNT_ALREADY_EXISTS

INVALID_STATE_TRANSITION
IDEMPOTENCY_CONFLICT
CONCURRENCY_CONFLICT
```

The catalog may be extended as Business Rules become implementation scope.

Existing code meanings should remain stable within API v1.

---

## 5. Validation vs Business Rule

### API-ERR-009 — Boundary Validation Is Distinct from Domain Validation

**Status:** Confirmed

Examples of boundary validation:

```text
quantity = null
unknown enum string
malformed UUID
invalid JSON
```

Examples of Business Rule violations:

```text
quantity > Position Quantity
Bank Account is FROZEN
Security is DISABLED for Buy
```

The implementation should not collapse both categories into one generic validation exception.

---

## 6. Ownership and Authorization

### API-ERR-010 — Initial Ownership Violation Is a Domain Error

**Status:** Confirmed

Because production authentication/authorization is outside initial scope, ownership mismatch is initially represented as a domain/application error.

When authentication is introduced later, some ownership failures may become HTTP authorization semantics such as `403 Forbidden`.

This future change should be handled deliberately rather than simulated prematurely.

---

## 7. Concurrency

### API-ERR-011 — Concurrency Conflict Is Stable at the API Boundary

**Status:** Confirmed

Whatever internal mechanism detects the race:

- optimistic locking;
- pessimistic locking;
- serialization failure;
- another database conflict;

the public contract should translate relevant retryable business conflicts into a stable application error such as:

```text
409 Conflict
code = CONCURRENCY_CONFLICT
```

The API must not expose the internal lock mechanism.

---

## 8. Error Stability

### API-ERR-012 — Human Message May Change, Error Code Meaning Should Not

**Status:** Confirmed

Clients should depend on:

```text
HTTP status
+
code
```

rather than exact `detail` message wording.

This allows user-facing messages to improve without breaking clients.
