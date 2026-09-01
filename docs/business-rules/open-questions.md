# Business Rules — Open Questions and Future Scope

## Resolved Use-Case Questions

These questions were previously open and are now resolved.

### OQ-UC-001 — Acquisition Cost and Realized P/L Method

**Status:** Resolved

**Decision:**

- Phase 4 uses Average Cost.
- FIFO is deferred to future scope.
- Average Acquisition Price may be fractional.
- Remaining acquisition cost is preserved so rounding residue does not accumulate indefinitely.

---

### OQ-UC-002 — Reservation of Securities for Sell Orders

**Status:** Resolved / Refined

**Decision:**

- Phase 3 immediate full-fill Sell Orders do not use Reserved Quantity.
- Phase 3 validates `Sell Quantity <= Current Position Quantity`.
- Reserved Quantity / Available Quantity are introduced in Phase 6 when Orders may remain open or partially filled.

---

### OQ-UC-003 — Separation of Execution Processing

**Status:** Deferred to Phase 6

**Current Decision:**

- Phase 3 creates Order and one full Execution in the same business operation.
- When Limit Orders or Partial Execution are introduced, revisit whether execution processing becomes an independent System use case.

---

### OQ-UC-004 — Bank Account Closure Conditions

**Status:** Resolved

**Decision:**

A Bank Account may close only when:

- Status = `ACTIVE`;
- Current Balance = `0`;
- Reserved Amount = `0`;
- no unfinished cash movement exists.

`FROZEN -> CLOSED` is not allowed directly.

---

### OQ-UC-005 — Securities Account Closure Conditions

**Status:** Resolved

**Decision:**

A Securities Account may close only when:

- Status = `ACTIVE`;
- Current Balance = `0`;
- Reserved Amount = `0`;
- no Position exists;
- no unfinished Order exists.

`RESTRICTED -> CLOSED` is not allowed directly.

---

### OQ-UC-006 — Effect of Disabling a Security

**Status:** Resolved

**Decision:**

When a Security is `DISABLED`:

- new Buy Orders are prohibited;
- Sell Orders reducing existing Positions are allowed;
- Market Price generation continues;
- history is retained;
- Security is not physically deleted;
- Administrator may later re-enable it.

Open-Order behavior should be revisited when long-lived Orders are introduced.

---

### OQ-UC-007 — Operations Allowed by Account Status

**Status:** Resolved

**Decision:**

Bank `FROZEN`:

- may receive money and be viewed;
- may not send money, withdraw, fund Securities Account, or close.

Securities `RESTRICTED`:

- may be viewed;
- may transfer cash out to an eligible Bank Account;
- may not receive Bank funding;
- may not place new Buy/Sell Orders;
- may not close.

`CLOSED` accounts allow historical viewing only.

---

### OQ-UC-008 — Reservation Lifecycle for Cash Transfers

**Status:** Resolved / Refined

**Decision:**

Phase 1 synchronous cash movements do not use Reserved Amount.

Phase 3 immediate Market Orders also do not use Reserved Amount.

Reservation begins in later phases only for operations that remain pending meaningfully over time.

---

## Open Questions

### OQ-BR-001 — Effective Timing of Security Parameter Changes

**Status:** Open Question  
**Decision Required Before:** finalizing `market-simulation.md`

When Expected Return `μ` or Volatility `σ` changes, determine whether the new value applies:

- immediately to the current Business Day if price generation has not yet occurred; or
- starting from the next Business Day.

Historical Market Prices are never recalculated.

---

## Planned Future Scope

The following are intended later-phase extensions:

### Trading

- Limit Order.
- Cancel Order.
- Partial Execution.
- `1 Order : N Executions`.
- long-lived Order states such as OPEN / PARTIALLY_FILLED / FILLED / CANCELLED.
- Reserved Amount for pending Buy Orders.
- Reserved Quantity / Available Quantity for pending Sell Orders.

### Banking

- Per-transfer limit.
- Daily transfer limit.
- Scheduled transfer.
- External-bank transfer.

### Robustness

- Idempotency for asset-moving requests.
- Concurrency control implementation.
- transaction-failure testing.
- performance verification.

### Portfolio / Cost Basis

- FIFO as an additional cost-basis method.

---

## Future Considerations

These are candidates, not committed implementation plans.

### Customer

- Duplicate Customer detection.
- unique email.
- external customer number.
- authentication identity.
- KYC / identity verification.

### Financial Features

- transfer fees;
- trading fees;
- dividends;
- taxes;
- interest;
- term deposits;
- richer double-entry accounting;
- ETFs / funds / bonds / derivatives;
- margin trading;
- short selling;
- FX.

### Portfolio Analytics

- asset history;
- cumulative return;
- allocation analysis;
- volatility;
- Sharpe ratio;
- maximum drawdown.

### Ledger / Audit

- richer audit views;
- more granular external/system accounts;
- fuller correction workflows.

## Scope Classification Rule

When a future item becomes concrete:

1. **Future Consideration** — candidate only.
2. **Planned Future Scope** — adoption committed for a later phase.
3. **Use Case** — behavior enters implementation planning.
4. **Business Rules** — invariants and valid behavior become explicit.
5. **Model / API / Architecture** — implementation design follows.
6. **TDD** — implementation proceeds through failing tests first.
