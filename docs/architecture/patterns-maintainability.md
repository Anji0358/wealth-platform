# Design Patterns and Maintainability

## 1. Pattern Strategy

### ARCH-065 — Patterns Solve Concrete Problems

**Status:** Confirmed

Design patterns are not project requirements by themselves.

A pattern should be introduced when it solves a concrete pressure such as:

- multiple algorithms;
- complex object creation;
- module coupling;
- repeated branching;
- persistence boundary complexity.

This project intentionally learns patterns through refactoring rather than pattern-first implementation.

---

## 2. Patterns Already Present in the Architecture

### ARCH-066 — Repository / Port-Adapter Pattern

**Status:** Confirmed architectural pattern

Repository abstractions face inward and Infrastructure implements them.

This provides:

- dependency inversion;
- persistence isolation;
- test doubles for Application tests;
- domain independence from Spring Data.

---

### ARCH-067 — Application Service Pattern

**Status:** Confirmed architectural pattern

Application Services coordinate Use Cases and transaction boundaries.

They should not absorb the detailed Business Rules that belong in Domain objects.

---

### ARCH-068 — Adapter Pattern at External Boundaries

**Status:** Confirmed architectural pattern

Examples:

```text
HTTP Controller
Scheduler
JPA Repository Adapter
Random Source Adapter
```

These adapt external mechanisms to the Application/Domain model.

---

### ARCH-069 — Query Service / Read Model Pattern

**Status:** Confirmed architectural pattern

Query responsibilities may use purpose-built projections instead of reusing write-side Aggregates.

This improves:

- query clarity;
- performance;
- SQL learning;
- separation between mutation consistency and reporting.

---

## 3. Patterns Deferred Until Needed

### ARCH-070 — Strategy Pattern

**Status:** Deferred until variation exists

Recommended trigger:

```text
Average Cost
+
FIFO
+
possibly another cost-basis method
```

When more than one algorithm is genuinely supported, introduce a Cost Basis Strategy.

Do not introduce Strategy while only one algorithm exists.

---

### ARCH-071 — Factory Pattern

**Status:** Deferred until creation becomes complex

Use a Factory when Aggregate creation needs substantial coordination, validation, or variants that no longer fit a clear constructor/static factory.

Do not create generic factories for simple constructors.

---

### ARCH-072 — Domain Event Pattern

**Status:** Deferred until coupling pressure exists

Consider Domain Events when:

- one completed domain action needs multiple decoupled reactions;
- direct Application orchestration becomes difficult to maintain;
- asynchronous behavior becomes an actual requirement.

Do not introduce an Event Bus for simple local transactional coordination.

---

### ARCH-073 — Specification Pattern

**Status:** Future consideration

Consider only if complex reusable business predicates emerge and become difficult to express cleanly in existing Domain behavior.

Do not introduce a Specification framework for simple `if` rules.

---

## 4. Maintainability Rules

### ARCH-074 — Shared Package Must Stay Small

**Status:** Confirmed

`shared` is reserved for genuinely cross-cutting concepts.

Possible examples:

```text
Money/value concept
common time abstraction
shared identifier primitives
```

Avoid:

```text
CommonService
CommonUtil
BaseRepository
Misc
```

---

### ARCH-075 — Generic Repository Is Not Used

**Status:** Confirmed

Avoid:

```text
GenericRepository<T, ID>
```

as the default domain abstraction.

Repositories should expose operations meaningful for each Aggregate.

---

### ARCH-076 — Duplication Is Sometimes Cheaper Than Wrong Abstraction

**Status:** Confirmed

A small amount of duplication is preferable to a shared abstraction that creates coupling between unrelated domains.

Refactor only when the shared concept is semantically real.

---

### ARCH-077 — Architectural Boundaries May Be Tested

**Status:** Future enhancement

ArchUnit or equivalent tooling may later verify rules such as:

```text
domain must not depend on presentation
domain must not depend on infrastructure
```

Architecture tests are useful once package structure stabilizes, but are not required before the first implementation.

---

### ARCH-078 — Architecture Decisions with Long-Term Consequences Use ADRs

**Status:** Confirmed

Meaningful choices should be recorded in:

```text
docs/adr/
```

Examples include:

- Modular Monolith;
- Domain/JPA model separation;
- final concurrency strategy;
- major query/persistence approach changes.
