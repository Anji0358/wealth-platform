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

The test should describe the desired behavior before that production behavior is implemented.

In Java, Red may contain two stages:

```text
Compiler Red
↓
Minimum cohesive compile-enabling slice
↓
Behavioral Red, unless the minimal slice is already Green
↓
Minimum behavior implementation
↓
Green
```

`Compiler Red` is valid when the current test does not compile because a required production class, constructor, method, or interface signature does not exist yet.

At this stage, add the smallest cohesive slice required for compilation: the missing declaration and, when necessary, directly required fields and a trivial method body. Do not add behavior beyond the current test. If this makes the test Green, it is a valid Green outcome.

An unrelated compilation, configuration, or environment failure is not an acceptable Red.

---

## TS-002 — TDD Is Applied Where Behavior Exists

**Status:** Confirmed

Do not force TDD mechanically onto:

- simple configuration files;
- trivial DTO declarations;
- type or interface declarations added only to make the current behavior test compile;
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

---

## TS-012 — One TDD Task Advances One Micro-Step

**Status:** Confirmed

One task must perform only one of the following:

- write one test for one behavior;
- add the cohesive compile-enabling slice required by the current test, including a type, required fields, and a trivial method body when necessary;
- make one currently failing test Green with the minimum implementation;
- perform one justified refactoring while preserving behavior.

Do not combine test creation, production behavior, and refactoring in one task.

---

## TS-013 — Interfaces Emerge from Current Evidence

**Status:** Confirmed

Defining a production interface and creating its production implementation are separate decisions.

Prefer to introduce an interface during Refactor when the current code reveals a concrete role, boundary, dependency-direction problem, or testability problem.

An interface may also be introduced during Red when the current behavior cannot be tested without a seam to an external dependency. In that case, define only the signature required by the current test.

Do not introduce an interface only because it may be useful later, because a design principle is being studied, or because the type is conventionally represented by an interface.

---

## TS-014 — Test Doubles Are Allowed but Must Remain Minimal

**Status:** Confirmed

Dummy, Stub, Fake, Spy, and Mock are valid tools in TDD when the current test needs dependency isolation or an observable collaboration.

Introduce only the Test Double behavior or observation required by the current test.

Do not create stored values, counters, verification methods, or additional branches in anticipation of later tests.

Prefer the simplest suitable Test Double. Use a mocking framework only when it solves a current problem, not automatically.
