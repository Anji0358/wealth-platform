# Market and Portfolio API

All paths below are relative to:

```text
/api/v1
```

## Security and Market Reads

### API-MKT-001 — List Securities

```text
GET /securities
```

**Related Use Cases**

- UC-MKT-004 List Securities

Without a filter, the endpoint returns `ACTIVE` Securities. Supported filtering is:

```text
GET /securities?status=ACTIVE
GET /securities?status=DISABLED
```

Returns public/read-oriented Security information such as:

- Security ID;
- Security Code;
- name;
- status.

The initial endpoint may use pagination if the dataset grows, though the initial Security count is expected to be modest.

Listing a Security means that the Security exists; it does not by itself guarantee that a new Buy Order is allowed.

---

### API-MKT-005 — View Security

```text
GET /securities/{securityId}
```

**Related Use Cases**

- UC-MKT-004 List Securities

Direct read is available for both `ACTIVE` and `DISABLED` Securities. The response includes at least Security ID, Security Code, name, and status.

---

### API-MKT-002 — View Latest Market Prices

```text
GET /market-prices/latest
```

**Related Use Cases**

- UC-MKT-002 View Latest Market Prices

Returns the latest persisted Market Price for applicable Securities.

Possible item:

```json
{
  "securityId": "...",
  "securityCode": "ABC",
  "businessDate": "2026-09-01",
  "closingPrice": 1200,
  "generatedAt": "2026-09-01T07:00:00Z"
}
```

This endpoint never generates new prices.

---

### API-MKT-003 — View Market Price History

```text
GET /securities/{securityId}/market-prices
```

**Related Use Cases**

- UC-MKT-003 View Market Price History

Results are ordered by Business Date and use pagination.

Possible filters:

```text
fromDate
toDate
```

---

## Market Price Generation

### API-MKT-004 — No Public Scheduler Endpoint Initially

**Status:** Confirmed

`UC-MKT-001 Generate Daily Market Prices` is invoked by the System/Scheduler through an Application Service or equivalent internal entry point.

The initial public HTTP API does not expose a manual endpoint such as:

```text
POST /market-prices/generate
```

A manual administrative trigger may be introduced later only if an actual operational need exists.

---

## Portfolio

### API-PFL-001 — View Portfolio Overview

```text
GET /customers/me/portfolio
```

**Related Use Cases**

- UC-PFL-001 View Portfolio Overview

Portfolio is a Read Model, not a persisted Portfolio entity.

A response may contain:

```json
{
  "customerId": "...",
  "bankCash": 200000,
  "securitiesCash": 50000,
  "positions": [
    {
      "securityId": "...",
      "securityCode": "ABC",
      "quantity": 100,
      "marketPrice": 1200,
      "valuation": 120000,
      "remainingAcquisitionCost": 100000,
      "averageAcquisitionPrice": 1000.0,
      "unrealizedProfitLoss": 20000
    }
  ],
  "totalAssets": 370000,
  "valuedAt": "2026-09-01T11:00:00Z"
}
```

Exact field grouping may evolve during implementation while preserving the underlying semantics.

---

### API-PFL-002 — View Profit and Loss

```text
GET /customers/me/portfolio/profit-loss
```

**Related Use Cases**

- UC-PFL-002 View Profit and Loss

Recommended response concepts:

```json
{
  "customerId": "...",
  "realizedProfitLoss": 12000,
  "unrealizedProfitLoss": 20000,
  "totalProfitLoss": 32000
}
```

Per-Security breakdown may be included when useful.

Realized P/L uses historical Sell Execution facts.

Unrealized P/L uses current Position and latest persisted Market Price.

---

## Market/Portfolio Notes

- Portfolio valuation may use the latest prior Business Day price.
- Trading has stricter same-Business-Day price requirements.
- API reads never trigger GBM calculation.
- Market Price `businessDate` uses Asia/Tokyo market-calendar semantics.
