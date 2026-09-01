# TDD and Test Levels

## TS-001 — Red, Green, Refactor

**Status:** Confirmed

New business behavior should normally follow:

```text
Red
↓
Green
↓
Refactor
```

The failing test should describe the desired behavior before production code is added.

---

## TS-002 — TDD Is Applied Where Behavior Exists

**Status:** Confirmed

Do not force TDD mechanically onto:

- simple configuration files;
- trivial DTO declarations;
- migration files whose correctness is better verified through migration/integration tests.

TDD is primarily for behavior and design feedback.

---

## TS-003 — Business Rules Are the Highest-Priority Test Target

**Status:** Confirmed

Priority order:

```text
Invariant
State Transition
Financial Calculation
Failure Case
Boundary Condition
Concurrency
```

---

## TS-004 — Test Pyramid

**Status:** Confirmed

Use many fast tests and fewer expensive tests:

```text
             E2E / API
               few
              /   \
         Integration
          moderate
           /     \
       Unit Tests
          many
```

---

## TS-005 — Domain Unit Tests Are the Majority

**Status:** Confirmed

Domain behavior should run without Spring or PostgreSQL when persistence is not part of the behavior under test.

---

## TS-006 — E2E Tests Are Selective

**Status:** Confirmed

Do not express every Business Rule through `@SpringBootTest`.

Use E2E tests for high-value cross-layer behavior.

---

## TS-007 — Coverage Is a Supporting Metric

**Status:** Confirmed

100% line coverage is not a project goal.

Coverage should support, not replace, verification of:

- important branches;
- invariants;
- failure paths;
- concurrency behavior.

---

## TS-008 — Branch Coverage Is Useful for Rule-Heavy Logic

**Status:** Confirmed

Branch coverage may help identify untested financial rule paths, but increasing the metric itself is not the objective.

---

## TS-009 — Test Names Describe Behavior

**Status:** Confirmed

Prefer names such as:

```text
withdraw_rejects_when_available_balance_is_insufficient
```

over names tied only to internal method structure.

---

## TS-010 — Given / When / Then Structure

**Status:** Confirmed

Tests should make setup, action, and expected result easy to identify.

Comments are optional when code structure already communicates Given / When / Then clearly.

---

## TS-011 — One Test Focuses on One Business Behavior

**Status:** Confirmed

Do not combine many unrelated rules into one oversized test unless the test is explicitly an E2E business-flow test.
