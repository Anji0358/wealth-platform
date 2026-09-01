# Performance Testing

## TS-PERF-001 — Performance Tests Are Separate from Functional Tests

Do not generate 1,000,000 Ledger Entries during ordinary TDD execution.

Performance tests use a dedicated execution path/profile/task.

---

## TS-PERF-002 — Before/After Comparison Is Primary

Performance learning compares controlled alternatives such as:

```text
no index
vs
index

N+1
vs
projection

offset
vs
cursor

read-then-update
vs
atomic SQL
```

---

## TS-PERF-003 — Large Synthetic Dataset

Target learning-scale datasets may include:

```text
Customers             ≈ 10,000
Bank Accounts          tens of thousands
Securities             ≈ 100
Ledger Entries         ≈ 1,000,000
Orders / Executions    hundreds of thousands
Market Prices          multiple years
```

---

## TS-PERF-004 — Performance Is Reproducible

Record dataset generation parameters and seeds.

---

## TS-PERF-005 — Benchmark Environment Is Recorded

Meaningful performance notes should capture at least:

- dataset size;
- indexes;
- query;
- application/database version context where relevant;
- hardware/environment notes if comparing absolute times.

---

## TS-PERF-006 — Production SLA Is Not Claimed

Performance tests are learning benchmarks, not production commitments.

---

## TS-PERF-007 — Performance Regression Checks Are Conservative

Prefer stable plan-level or structural indicators over fragile tight millisecond thresholds.

---

## TS-PERF-008 — EXPLAIN Results Belong in SQL Experiment Reports

Important optimizations should retain enough evidence to explain the before/after result.
