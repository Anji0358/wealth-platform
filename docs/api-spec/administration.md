# Administration API

All paths below are relative to:

```text
/api/v1
```

The `/admin` namespace identifies administrative business capabilities.

It does not imply that production-grade Admin authentication is already implemented.

## Bank Account Administration

### API-ADM-001 — Freeze Bank Account

```text
POST /admin/bank-accounts/{bankAccountId}/freeze
```

**Related Use Cases**

- UC-ADM-001 Freeze Bank Account

**Success**

```text
204 No Content
```

---

### API-ADM-002 — Unfreeze Bank Account

```text
POST /admin/bank-accounts/{bankAccountId}/unfreeze
```

**Related Use Cases**

- UC-ADM-002 Unfreeze Bank Account

**Success**

```text
204 No Content
```

---

## Securities Account Administration

### API-ADM-003 — Restrict Securities Account

```text
POST /admin/securities-accounts/{securitiesAccountId}/restrict
```

**Related Use Cases**

- UC-ADM-003 Restrict Securities Account

**Success**

```text
204 No Content
```

---

### API-ADM-004 — Unrestrict Securities Account

```text
POST /admin/securities-accounts/{securitiesAccountId}/unrestrict
```

**Related Use Cases**

- UC-ADM-004 Unrestrict Securities Account

**Success**

```text
204 No Content
```

---

## Security Administration

### API-ADM-005 — Register Security

```text
POST /admin/securities
```

**Related Use Cases**

- UC-ADM-005 Register Security

**Request**

```json
{
  "securityCode": "ABC",
  "name": "ABC Corporation",
  "initialPrice": 1000,
  "expectedReturn": 0.05,
  "volatility": 0.20
}
```

**Success**

```text
201 Created
```

```json
{
  "securityId": "...",
  "securityCode": "ABC",
  "name": "ABC Corporation",
  "status": "ACTIVE",
  "initialPrice": 1000,
  "expectedReturn": 0.05,
  "volatility": 0.20,
  "createdAt": "2026-09-01T12:00:00Z"
}
```

---

### API-ADM-006 — Update Security Parameters

```text
PATCH /admin/securities/{securityId}
```

**Related Use Cases**

- UC-ADM-006 Update Security Parameters

Example request:

```json
{
  "expectedReturn": 0.04,
  "volatility": 0.18
}
```

Only mutable Security parameters are updated.

Security Code is immutable.

Historical Market Prices are not recalculated.

---

### API-ADM-007 — Disable Security

```text
POST /admin/securities/{securityId}/disable
```

**Related Use Cases**

- UC-ADM-007 Disable Security

**Success**

```text
204 No Content
```

The resulting Business Rules prohibit new Buy Orders but allow existing Position reduction and continue Market Price generation.

---

### API-ADM-008 — Enable Security

```text
POST /admin/securities/{securityId}/enable
```

**Related Use Cases**

- UC-ADM-008 Enable Security

**Success**

```text
204 No Content
```

---

## Administration Notes

- State changes are modeled as explicit business commands.
- They are not exposed as generic status-field patches.
- Invalid or redundant state transitions use the common error semantics.
- `/admin` is an API responsibility namespace, not a substitute for authentication.
