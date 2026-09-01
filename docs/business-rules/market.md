# Market Business Rules

## Security Rules

### BR-MKT-001 — Security Code Is Unique

**Status:** Confirmed  
**Phase:** Phase 2

#### Rule

Each Security has a Security Code / Ticker that is unique within the system.

The code is immutable after registration.

The concrete database uniqueness mechanism is deferred to `data-model.md` and `sql-strategy.md`.

---

### BR-MKT-002 — Security Parameters Must Be Valid

**Status:** Confirmed  
**Phase:** Phase 2

#### Rule

When registering or updating a Security:

- Initial Price must be greater than `0`;
- Volatility `σ` must be greater than or equal to `0`;
- Expected Return `μ` may be positive, zero, or negative.

---

### BR-MKT-003 — Security Is Never Physically Deleted in Normal Operation

**Status:** Confirmed  
**Phase:** Phase 2

#### Rule

A Security is disabled through status transition rather than physical deletion.

Historical prices, Orders, Executions, and related facts remain traceable.

---

### BR-MKT-004 — Security Status Is Reversible

**Status:** Confirmed  
**Phase:** Phase 2

#### Rule

Valid Security status transitions are:

- `ACTIVE -> DISABLED`
- `DISABLED -> ACTIVE`

`DISABLED` is a reversible administrative state, not a terminal state.

---

## Market Price Rules

### BR-MKT-005 — One Market Price per Security per Business Day

**Status:** Confirmed  
**Phase:** Phase 2

#### Rule

For each Security and Business Day, at most one persisted Market Price may exist.

The same Security and Business Day must not receive a second generated price.

---

### BR-MKT-006 — Daily Price Generation Is Idempotent

**Status:** Confirmed  
**Phase:** Phase 2

#### Rule

If daily Market Price generation is invoked again for a Security and Business Day whose price already exists:

- no second Market Price is created;
- the existing price is not overwritten.

---

### BR-MKT-007 — Market Price History Is Immutable

**Status:** Confirmed  
**Phase:** Phase 2

#### Rule

Persisted historical Market Prices are not overwritten or deleted through normal Market operations.

Changing Security parameters affects only future price generation.

---

### BR-MKT-008 — Disabled Securities Continue Market Price Generation

**Status:** Confirmed  
**Phase:** Phase 2

#### Rule

A `DISABLED` Security continues to receive daily simulated Market Prices.

Disabling a Security means new acquisition is prohibited; it does not stop market data generation.

---

### BR-MKT-009 — Business Day Is Monday Through Friday Initially

**Status:** Confirmed  
**Phase:** Phase 2

#### Rule

The initial Business Day calendar is Monday through Friday.

Public holidays and exchange-specific calendars are outside the initial scope.

---

## Price Precision Rules

### BR-MKT-010 — GBM Calculation and Official Market Price Are Separate Concepts

**Status:** Confirmed  
**Phase:** Phase 2

#### Rule

The internal GBM calculation may produce a fractional real-number value.

Before persistence as the official Closing Price, the generated value is normalized to whole JPY using round-to-nearest-yen semantics.

Trading, Ledger, and ordinary Portfolio monetary calculations use the persisted official Market Price, not the unnormalized simulation value.

#### Example

Generated value:

`1003.728419...`

Persisted Closing Price:

`1004 JPY`

---

### BR-MKT-011 — Market Reads Never Generate New Random Prices

**Status:** Confirmed  
**Phase:** Phase 2

#### Rule

Viewing Market Prices, placing Orders, or valuing a Portfolio never generates a new random Market Price on demand.

Only the dedicated Market Price generation process creates new Market Prices.

## Parameter-Change Timing Reference

Changes to `Expected Return μ` and `Volatility σ` apply to the next Market Price that has not yet been generated, as defined by `market-simulation.md` MS-029.

Already-persisted Market Prices are never recalculated.

---

### BR-MKT-012 — Security Existence and Buy Eligibility Are Separate

**Status:** Confirmed
**Phase:** Phase 2+

#### Rule

A `DISABLED` Security continues to exist and remains directly readable.

`DISABLED` prohibits only new Buy Orders. Existing holdings may be sold, and Portfolio display, Order/Execution history, Market Price history, and direct Security reads remain available.

---

### BR-MKT-013 — Security Listing and Direct Read Include Status

**Status:** Confirmed
**Phase:** Phase 2

#### Rule

The default Customer Security list contains `ACTIVE` Securities.

An explicit status filter may request `DISABLED` Securities. Direct read of a `DISABLED` Security is allowed.

Every Security list item and direct-read response includes Security status so clients do not infer Buy eligibility from existence alone.

#### Related Use Cases

- UC-MKT-004 List Securities
- UC-MKT-002 View Latest Market Prices
- UC-MKT-003 View Market Price History
