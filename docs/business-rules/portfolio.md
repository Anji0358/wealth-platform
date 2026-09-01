# Portfolio Business Rules

## Valuation Rules

### BR-PFL-001 — Portfolio Uses Latest Persisted Market Price

**Status:** Confirmed  
**Phase:** Phase 4

#### Rule

Portfolio valuation uses the latest persisted Market Price available for each Security.

Unlike Trading, Portfolio valuation does not require a Market Price from the current calendar date.

On a weekend, for example, the latest Friday Closing Price may be used.

No random price is generated during valuation.

---

### BR-PFL-002 — Security Without Market Price Is Not Artificially Valued

**Status:** Confirmed  
**Phase:** Phase 4

#### Rule

If a Security has no persisted Market Price at all, the system does not invent a price for Portfolio valuation.

The Security's valuation is treated as unavailable until a persisted Market Price exists.

---

### BR-PFL-003 — Position Valuation Uses Total Owned Quantity

**Status:** Confirmed  
**Phase:** Phase 4

#### Rule

Current Position Valuation is:

`Owned Quantity × Latest Persisted Market Price`

When Reserved Quantity exists in a future phase, Reserved Quantity remains part of the Customer's owned assets and is included in valuation.

---

## Profit and Loss Rules

### BR-PFL-004 — Unrealized Profit and Loss

**Status:** Confirmed  
**Phase:** Phase 4

#### Rule

For an existing Position:

`Unrealized P/L = Current Position Valuation - Remaining Acquisition Cost`

---

### BR-PFL-005 — Realized Profit and Loss

**Status:** Confirmed  
**Phase:** Phase 4

#### Rule

For a Sell Execution:

`Realized P/L = Sell Proceeds - Allocated Acquisition Cost`

Customer-level Realized P/L is the cumulative realized profit/loss from completed Sell Executions.

Fees and taxes are excluded in the initial scope.

---

## Total Asset Rules

### BR-PFL-006 — Total Assets Include Owned Reserved Assets

**Status:** Confirmed  
**Phase:** Phase 4 / Phase 6 reservation semantics

#### Rule

Reserved Amount and Reserved Quantity remain Customer-owned assets.

They affect availability, not ownership.

Therefore Total Assets use Current Balance / total owned Quantity rather than Available Balance / Available Quantity.

---

### BR-PFL-007 — Total Assets Definition

**Status:** Confirmed  
**Phase:** Phase 4

#### Rule

Initial Total Assets are conceptually:

`Sum(Bank Account Current Balances)`
`+ Securities Account Current Cash Balance`
`+ Sum(Current Position Valuations)`

Reserved values are not added separately because they are already included in Current Balance or total owned Quantity.

System Accounts such as External Account and Market Settlement Account are not Customer assets.
