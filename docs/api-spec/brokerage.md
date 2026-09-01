# Brokerage API

All paths below are relative to:

```text
/api/v1
```

## Securities Account

### API-BRK-001 — Open Securities Account

```text
POST /customers/{customerId}/securities-accounts
```

**Related Use Cases**

- UC-BRK-001 Open Securities Account

**Request**

An empty JSON object may be accepted initially:

```json
{}
```

**Response**

```text
201 Created
```

```json
{
  "securitiesAccountId": "...",
  "customerId": "...",
  "status": "ACTIVE",
  "currentBalance": 0,
  "reservedAmount": 0,
  "availableBalance": 0,
  "createdAt": "2026-09-01T10:00:00Z"
}
```

Opening a second Securities Account for the same Customer is rejected.

---

### API-BRK-002 — View Securities Account

```text
GET /securities-accounts/{securitiesAccountId}
```

**Related Use Cases**

- UC-BRK-010 View Securities Account

**Response includes at least:**

```json
{
  "securitiesAccountId": "...",
  "customerId": "...",
  "status": "ACTIVE",
  "currentBalance": 100000,
  "reservedAmount": 0,
  "availableBalance": 100000
}
```

---

### API-BRK-003 — Transfer Cash from Bank

```text
POST /securities-accounts/{securitiesAccountId}/cash-transfers/from-bank
```

**Related Use Cases**

- UC-BRK-002 Transfer Cash from Bank

**Request**

```json
{
  "bankAccountId": "...",
  "amount": 50000
}
```

**Response**

```text
201 Created
```

```json
{
  "bankAccountId": "...",
  "securitiesAccountId": "...",
  "amount": 50000,
  "ledgerTransactionId": "...",
  "executedAt": "2026-09-01T10:10:00Z"
}
```

Both accounts must belong to the same Customer.

---

### API-BRK-004 — Transfer Cash to Bank

```text
POST /securities-accounts/{securitiesAccountId}/cash-transfers/to-bank
```

**Related Use Cases**

- UC-BRK-003 Transfer Cash to Bank

**Request**

```json
{
  "bankAccountId": "...",
  "amount": 25000
}
```

**Response**

```text
201 Created
```

A `RESTRICTED` Securities Account may still use this operation when all other rules are satisfied.

---

### API-BRK-005 — Close Securities Account

```text
POST /securities-accounts/{securitiesAccountId}/close
```

**Related Use Cases**

- UC-BRK-011 Close Securities Account

**Success**

```text
200 OK
```

```json
{
  "securitiesAccountId": "...",
  "status": "CLOSED",
  "closedAt": "2026-09-01T10:30:00Z"
}
```

---

## Order

### API-BRK-006 — Place Market Order

```text
POST /securities-accounts/{securitiesAccountId}/orders
```

**Related Use Cases**

- UC-BRK-004 Place Buy Order
- UC-BRK-005 Place Sell Order

**Request**

Buy:

```json
{
  "securityId": "...",
  "side": "BUY",
  "quantity": 10
}
```

Sell:

```json
{
  "securityId": "...",
  "side": "SELL",
  "quantity": 10
}
```

Phase 3 does not accept:

```text
limitPrice
requestedPrice
```

because only immediate Market Orders are supported.

**Success**

```text
201 Created
```

Recommended response:

```json
{
  "orderId": "...",
  "executionId": "...",
  "securitiesAccountId": "...",
  "securityId": "...",
  "side": "BUY",
  "quantity": 10,
  "status": "FILLED",
  "executionPrice": 1200,
  "executionAmount": 12000,
  "executedAt": "2026-09-01T10:40:00Z"
}
```

For a Sell Order, the response may additionally include:

```json
{
  "allocatedAcquisitionCost": 10000,
  "realizedProfitLoss": 2000
}
```

These are historical values fixed at execution time.

---

### API-BRK-007 — View Orders

```text
GET /securities-accounts/{securitiesAccountId}/orders
```

**Related Use Cases**

- UC-BRK-007 View Orders

Results use cursor pagination.

Possible filters may include:

```text
securityId
side
fromDate
toDate
```

Do not design a universal arbitrary-query language initially.

---

### API-BRK-008 — View Executions

```text
GET /securities-accounts/{securitiesAccountId}/executions
```

**Related Use Cases**

- UC-BRK-008 View Executions

Results use cursor pagination and expose historical Execution facts.

---

### API-BRK-009 — View Positions

```text
GET /securities-accounts/{securitiesAccountId}/positions
```

**Related Use Cases**

- UC-BRK-009 View Positions

Possible Position response:

```json
{
  "securityId": "...",
  "securityCode": "ABC",
  "quantity": 100,
  "remainingAcquisitionCost": 100050,
  "averageAcquisitionPrice": 1000.5
}
```

`averageAcquisitionPrice` is derived and may contain fractional JPY.

Position rows with Quantity zero are not returned because they do not exist as current Positions.

---

## Future Order Cancellation

**Related Use Cases**

- UC-BRK-006 Cancel Order

Phase 6 may introduce:

```text
POST /orders/{orderId}/cancel
```

This is intentionally not:

```text
DELETE /orders/{orderId}
```

because cancellation preserves Order history rather than deleting the Order.

The endpoint is not implemented in Phase 3.

---

## Brokerage Notes

- Buy and Sell share the same Order resource.
- The API does not create separate `/buy` and `/sell` resources.
- Trading uses the persisted same-Business-Day Market Price defined by Business Rules.
- An Order request rejected before Order creation returns an error and does not create a `REJECTED` Order row in the initial model.
