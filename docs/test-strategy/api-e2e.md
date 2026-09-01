# API and End-to-End Tests

## API Contract Tests

### TS-API-001 — HTTP Contract Is Tested

Verify:

- endpoint path;
- method;
- request JSON;
- response JSON;
- status code;
- validation;
- error format.

---

### TS-API-002 — MockMvc Is the Default MVC Test Candidate

MockMvc is appropriate for Controller/API contract tests.

A Controller contract test does not need to start PostgreSQL when persistence is irrelevant.

---

### TS-API-003 — Error Code Is Part of the Contract

Error tests verify both:

```text
HTTP status
+
stable error code
```

Example:

```text
422
INSUFFICIENT_AVAILABLE_BALANCE
```

---

### TS-API-004 — Controller Tests Do Not Prove Transaction Correctness

MockMvc-only tests are insufficient to prove cross-database atomicity.

Transaction correctness requires DB-backed integration tests.

---

### TS-API-005 — Development Acting Identity Contract Is Tested

API contract tests verify that `X-Acting-Customer-Id` is parsed as a UUID and translated to `ActorContext`, and that a path/body `customerId` does not override the Acting Customer.

---

### TS-API-006 — Security Read Status Semantics Are Tested

API tests verify that `GET /securities` defaults to ACTIVE, `status=DISABLED` returns disabled Securities, direct read returns a DISABLED Security, and every Security response includes `status`.

---

## End-to-End Integration

### TS-E2E-001 — Important Financial Flows Have Full-Stack Tests

Selected tests should cover:

```text
HTTP
↓
Application
↓
Domain
↓
PostgreSQL
```

---

### TS-E2E-002 — Transfer Is a Priority E2E Test

Verify one successful Transfer results in:

- Source balance decrease;
- Destination balance increase;
- one Ledger Transaction;
- zero-sum Ledger Entries.

---

### TS-E2E-003 — Buy Order Is a Priority E2E Test

Verify:

- Securities Cash decrease;
- Position create/update;
- Order persisted;
- Execution persisted;
- Ledger created.

---

### TS-E2E-004 — Sell Order Is a Priority E2E Test

Verify:

- Position decrease/removal;
- Securities Cash increase;
- historical Realized P/L;
- Ledger created.

---

### TS-E2E-005 — Historical Values Remain Stable

After later Market Price changes, historical:

- Execution Price;
- Execution Quantity;
- Sell Realized P/L

must remain unchanged.

---

### TS-E2E-006 — Cursor Pagination Avoids Duplication and Gaps

Create records with equal timestamps and verify the tie-breaker prevents:

- duplicate items across pages;
- missing items across pages.
