# API Pagination and Idempotency

## 1. Cursor Pagination

### API-PAG-001 — History APIs Use Cursor Pagination

**Status:** Confirmed

Append-heavy historical resources use cursor pagination rather than offset pagination by default.

Examples:

- Bank transaction history;
- Orders;
- Executions;
- Market Price history;
- future Ledger audit history.

---

### API-PAG-002 — Pagination Request Shape

**Status:** Confirmed

Recommended request form:

```text
?limit=50&cursor=opaque-value
```

---

### API-PAG-003 — Default and Maximum Limit

**Status:** Confirmed

Recommended values:

```text
default limit = 50
maximum limit = 200
```

Values may later be tuned, but unbounded history reads are not allowed.

---

### API-PAG-004 — Cursor Is Opaque

**Status:** Confirmed

Clients must treat the cursor as an opaque token.

The cursor contract must not require clients to understand internal:

- timestamps;
- database IDs;
- compound sort keys.

---

### API-PAG-005 — Stable Sorting

**Status:** Confirmed

Historical pagination requires deterministic ordering.

Default history ordering is newest first.

An implementation may use a stable ordering conceptually equivalent to:

```text
(occurredAt, id) DESC
```

or the appropriate equivalent for the resource.

The exact SQL is deferred to `sql-strategy.md`.

---

### API-PAG-006 — Limited Filtering

**Status:** Confirmed

History endpoints may expose targeted filters such as:

```text
fromDate
toDate
securityId
side
```

Do not create a universal arbitrary query language initially.

---

### API-PAG-007 — Example Page Response

A generic page shape may be:

```json
{
  "items": [
    {}
  ],
  "nextCursor": "opaque-value-or-null"
}
```

A separate total row count is not required by default because exact counts can be expensive and are not necessary for cursor navigation.

---

## 2. Idempotency

### API-IDEM-001 — Idempotency Starts in Phase 5

**Status:** Planned Future Scope  
**Phase:** Phase 5

Asset-moving Command endpoints require idempotency support in Phase 5.

Covered operations include:

- Deposit;
- Withdrawal;
- Bank transfer;
- Bank-to-Securities cash transfer;
- Securities-to-Bank cash transfer;
- Buy Order;
- Sell Order.

---

### API-IDEM-002 — Request Header

**Status:** Planned Future Scope

The external request uses:

```text
Idempotency-Key: <opaque-client-key>
```

GET requests do not require an Idempotency Key.

---

### API-IDEM-003 — Same Key and Same Request Reuses Original Result

**Status:** Planned Future Scope

If the same Idempotency Key is submitted with the same logical request:

- no second asset movement occurs;
- no duplicate Ledger Transaction is created;
- no duplicate Execution is created;
- the original result is reused.

---

### API-IDEM-004 — Same Key and Different Request Is Conflict

**Status:** Planned Future Scope

If the same Idempotency Key is reused for a different logical request:

```text
409 Conflict
code = IDEMPOTENCY_CONFLICT
```

The exact request fingerprinting strategy is deferred to architecture/data-model design.

---

### API-IDEM-005 — Different Keys Represent Distinct Operations

**Status:** Planned Future Scope

Different Idempotency Keys represent separate requested operations even if their payloads happen to contain the same amount and target.

---

## 3. Concurrency Versioning

### API-CON-001 — Do Not Expose Internal Version Columns Prematurely

**Status:** Confirmed

The API does not initially expose a persistence `version` field merely because optimistic locking may later be used.

The external contract should not unnecessarily commit to an internal concurrency implementation.

If explicit client-side conditional updates become useful later, consider HTTP mechanisms such as:

```text
ETag
If-Match
```

at that time.
