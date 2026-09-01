# Fixtures, Determinism, and Test Quality

## TS-FIX-001 — Test Data Is Deterministic

Randomly generated test data should be reproducible.

Record or fix the seed when randomness is used.

---

## TS-FIX-002 — Fixture Builders Are Introduced When Duplication Appears

Possible future helpers:

```text
BankAccountTestBuilder
SecurityTestBuilder
OrderTestBuilder
```

Do not build a large test-data framework before repeated setup makes it worthwhile.

---

## TS-FIX-003 — Object Mother / Test Data Builder Is Refactoring-Driven

These patterns are acceptable when they remove real test duplication while preserving readability.

---

## TS-FIX-004 — Tests Do Not Depend on Execution Order

Each test must establish its own relevant state.

---

## TS-FIX-005 — Flaky Tests Are Defects

Do not accept intermittent tests caused by:

- real clock dependence;
- uncontrolled randomness;
- uncontrolled race timing;
- leaked database state.

---

## TS-FIX-006 — Integration Tests Are Distinguishable from Unit Tests

The Maven/test configuration should eventually make it possible to run:

```text
fast tests
integration tests
performance tests
```

separately.

Exact plugin/profile configuration is deferred to `development-environment.md` or build configuration.

---

## TS-FIX-007 — Fast Feedback Is Required for TDD

The normal Red/Green loop should not require Docker-heavy or million-row tests unless the behavior specifically depends on the database.

---

## TS-FIX-008 — Mutation Testing Is Future Consideration

PIT or equivalent tooling may later be used to evaluate whether Business Rule tests fail when logic is intentionally mutated.

Not an initial requirement.

---

## TS-FIX-009 — Architecture Tests Are Future Enhancement

ArchUnit may verify dependency rules after package structure stabilizes.

---

## TS-FIX-010 — Static Analysis Is Optional Initially

SpotBugs, Checkstyle, and similar tools may be added later, but should not distract from Business Rule, SQL, and transaction learning.
