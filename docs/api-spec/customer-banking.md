# Customer and Banking API

All paths below are relative to:

```text
/api/v1
```

## Customer

### API-CUS-001 — Register Customer

```text
POST /customers
```

**Related Use Cases**

- UC-CUS-001 Register Customer

**Request**

```json
{
  "name": "Taro Yamada"
}
```

**Response**

```text
201 Created
```

```json
{
  "customerId": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Taro Yamada",
  "createdAt": "2026-09-01T08:30:00Z"
}
```

The initial API does not require unique email or external identity.

---

### API-CUS-002 — View Customer Profile

```text
GET /customers/{customerId}
```

**Related Use Cases**

- UC-CUS-002 View Customer Profile

**Response**

```text
200 OK
```

The response may include:

- basic Customer information;
- Bank Account summaries or identifiers;
- whether a Securities Account exists;
- Securities Account identifier when one exists.

It should not embed complete account transaction histories.

---

## Bank Account

### API-BNK-001 — Open Bank Account

```text
POST /customers/{customerId}/bank-accounts
```

**Related Use Cases**

- UC-BNK-001 Open Bank Account

**Request**

```json
{
  "accountType": "ORDINARY_DEPOSIT"
}
```

or:

```json
{
  "accountType": "SAVINGS"
}
```

**Response**

```text
201 Created
```

```json
{
  "bankAccountId": "550e8400-e29b-41d4-a716-446655440001",
  "customerId": "550e8400-e29b-41d4-a716-446655440000",
  "accountType": "ORDINARY_DEPOSIT",
  "status": "ACTIVE",
  "currentBalance": 0,
  "reservedAmount": 0,
  "availableBalance": 0,
  "createdAt": "2026-09-01T08:40:00Z"
}
```

---

### API-BNK-002 — View Bank Account

```text
GET /bank-accounts/{bankAccountId}
```

**Related Use Cases**

- UC-BNK-006 View Bank Account

**Response fields should include at least:**

```json
{
  "bankAccountId": "...",
  "customerId": "...",
  "accountType": "ORDINARY_DEPOSIT",
  "status": "ACTIVE",
  "currentBalance": 100000,
  "reservedAmount": 0,
  "availableBalance": 100000
}
```

`availableBalance` is derived.

---

### API-BNK-003 — Deposit Money

```text
POST /bank-accounts/{bankAccountId}/deposits
```

**Related Use Cases**

- UC-BNK-002 Deposit Money

**Request**

```json
{
  "amount": 10000
}
```

**Response**

```text
201 Created
```

A useful response includes:

```json
{
  "bankAccountId": "...",
  "amount": 10000,
  "currentBalance": 110000,
  "availableBalance": 110000,
  "ledgerTransactionId": "..."
}
```

Phase 5 additionally requires the `Idempotency-Key` header.

---

### API-BNK-004 — Withdraw Money

```text
POST /bank-accounts/{bankAccountId}/withdrawals
```

**Related Use Cases**

- UC-BNK-003 Withdraw Money

**Request**

```json
{
  "amount": 5000
}
```

**Response**

```text
201 Created
```

```json
{
  "bankAccountId": "...",
  "amount": 5000,
  "currentBalance": 105000,
  "availableBalance": 105000,
  "ledgerTransactionId": "..."
}
```

---

### API-BNK-005 — Transfer Money

```text
POST /bank-accounts/{sourceBankAccountId}/transfers
```

**Related Use Cases**

- UC-BNK-004 Transfer Between Own Accounts
- UC-BNK-005 Transfer to Another Customer

**Request**

```json
{
  "destinationBankAccountId": "550e8400-e29b-41d4-a716-446655440099",
  "amount": 25000
}
```

**Response**

```text
201 Created
```

```json
{
  "sourceBankAccountId": "...",
  "destinationBankAccountId": "...",
  "amount": 25000,
  "ledgerTransactionId": "...",
  "executedAt": "2026-09-01T09:00:00Z"
}
```

The API does not require the client to label the transfer as "own" or "another customer".

Ownership relationships and Bank Account Type rules determine whether the transfer is allowed.

---

### API-BNK-006 — View Bank Transaction History

```text
GET /bank-accounts/{bankAccountId}/transactions
```

**Related Use Cases**

- UC-BNK-007 View Bank Transaction History

This is a Read Model based on Ledger facts.

The API does not require a separate persisted `BankTransaction` entity.

Pagination rules are defined in `pagination-idempotency.md`.

Possible item fields include:

```json
{
  "ledgerTransactionId": "...",
  "type": "BANK_TRANSFER",
  "amount": -25000,
  "occurredAt": "2026-09-01T09:00:00Z",
  "counterpartyAccountId": "...",
  "description": "Bank transfer"
}
```

The exact presentation fields may be refined during API implementation.

---

### API-BNK-007 — Close Bank Account

```text
POST /bank-accounts/{bankAccountId}/close
```

**Related Use Cases**

- UC-BNK-008 Close Bank Account

**Success**

```text
200 OK
```

Recommended response:

```json
{
  "bankAccountId": "...",
  "status": "CLOSED",
  "closedAt": "2026-09-01T09:30:00Z"
}
```

Closure-condition failures use the common Business Rule error format.

---

## Customer/Banking Notes

- No physical delete endpoint is exposed for Customer or Bank Account.
- Derived balances may be returned.
- Ledger internals are not exposed unnecessarily through ordinary Customer endpoints.
- Authorization is not production-grade in the initial scope, but Customer ownership Business Rules remain mandatory.
