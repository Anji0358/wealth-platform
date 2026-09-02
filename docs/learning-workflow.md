# Learning Workflow

## 1. Purpose

This document defines how the wealth-platform project is used as a learning environment.

The purpose is not simply to complete features. The workflow is designed to build practical skill in:

- TDD;
- Java;
- Spring;
- DDD;
- responsibility separation;
- maintainable design;
- PostgreSQL and SQL;
- debugging;
- transaction/concurrency behavior;
- design patterns.

The central principle is:

```text
Design
→ primarily input from AI

Implementation
→ primarily output from the developer

Review
→ AI

Correction
→ developer
```

---

## 2. Roles

### Developer

The developer normally performs:

- test design and test implementation;
- production implementation;
- refactoring;
- design-pattern refactoring;
- debugging experiments;
- SQL execution and observation;
- explanation of important learning results when useful.

### AI

AI normally acts as:

```text
Design Teacher
TDD Exercise Instructor
Code Reviewer
Debugging Tutor
SQL Experiment Instructor
```

AI is not the default implementation agent.

---

## 3. Standard Learning Cycle

The normal cycle is:

```text
Design Input
↓
Optional Understanding Check
↓
Select One Untested Behavior
↓
Red Task
↓
Compiler Red if a required type or signature is missing
↓
Minimum compile-enabling declaration
↓
Behavioral Red
↓
Minimum Green implementation
↓
Observe the current code
↓
Concrete refactoring pressure?
├─ Yes: perform one Refactor, confirm Green, and observe again
└─ No: return to the specification and select the next behavior
```

The AI must not collapse these stages into one large task.

---

# Part I — Design Learning

## 4. Design Is Primarily Input

The developer is not required to design every class, aggregate, repository, or transaction boundary from zero.

When a design choice is introduced, AI should explain:

```text
Decision
Why
Decision Criterion
Alternatives
Why Not Now
```

Example:

```text
Decision:
Keep the insufficient-balance rule inside BankAccount.

Why:
BankAccount owns the balance state and should protect the invariant that its usable balance cannot become invalid.

Decision Criterion:
Ask "whose state and invariant does this rule protect?"

Alternative:
Check the balance in Application Service.

Why Not:
That would allow a Domain invariant to leak outside the object that owns the state.
```

---

## 5. Design Explanation Should Teach Transferable Criteria

The explanation should not stop at `Use X here.` It should provide reusable criteria such as:

- Who owns the state?
- Who owns the invariant?
- What is the transaction boundary?
- Is a technical framework detail leaking into the Domain?
- Does this class have more than one reason to change?
- Is this abstraction solving a current problem or an imagined future problem?
- Does the dependency point inward or outward?
- Which model should remain stable when infrastructure changes?

The objective is to accumulate design judgment through repeated examples.

---

## 6. Understanding Checks

AI may ask short questions when the design concept is important or new.

Examples:

```text
Why is this rule kept inside BankAccount?

Why does the Domain repository interface not extend Spring Data Repository?

What problem would occur if this validation were placed only in the Controller?
```

Understanding checks should be brief. They are not design examinations and should not create excessive cognitive load.

Avoid repetitive checks for concepts already demonstrated repeatedly.

---

# Part II — TDD Learning

## 7. TDD Is the Default Implementation Workflow

Production behavior is normally developed using:

```text
Red
→ Green
→ Refactor
```

TDD should be experienced as a sequence, not merely described.

---

## 8. One Task at a Time

AI gives exactly one active implementation task at a time.

Each task must be one of the following TDD micro-steps:

- write one test for one behavior;
- add only the type or signature required to make the current test compile;
- make one currently failing test pass with the minimum production change;
- perform one justified refactoring while preserving behavior.

Do not combine:

```text
multiple test cases
test code and production behavior
interface definition and its production implementation
multiple new dependencies
multiple Red / Green / Refactor phases
completion of an entire Use Case
```

Prefer one target file per task. Use multiple files only when the current micro-step cannot be completed otherwise, and keep the set minimal.

### Test-Driven Design

Do not add a production class, interface, method, Test Double, or other abstraction because it may become useful later.

Add it only when its necessity is observable from:

- the current test;
- the current implementation;
- a concrete review finding;
- or the current behavior selected from the specification.

A declaration with no behavior does not need an artificial unit test. If the current test cannot compile without a missing type or signature, add only that declaration as its own micro-task and do not implement business logic yet.

When a boundary or Test Double becomes necessary, introduce only what the current test requires. The detailed policy is defined in `docs/test-strategy.md`.

The interaction remains:

```text
One Micro-Task
↓
Developer completion
↓
Review
↓
One Next Micro-Task
```

---

## 9. Task Format

Every task must be brief and use only the following headings:

```text
### Current TDD State

<Compiler Red / Red / Green / Refactor>

### Task

<One action only. Include the behavior and a short overview of any class or method that must be used or created. State the minimum boundary that prevents work from advancing into the next phase.>

### Target File

<One file in principle; the minimum set only when unavoidable>
```

Do not add separate `Context`, `Why`, `Learning Focus`, `Constraints`, `Out of Scope`, or `Hint` sections to the normal task. The review immediately before the task carries the necessary rationale. Put only an essential constraint in `Task`.

Example:

```text
### Current TDD State

Compiler Red

### Task

To make the current test compile, add `ProvisionCustomerUseCase` and only the constructor currently required by the test. Do not implement customer-provisioning behavior yet.

### Target File

`src/main/java/.../ProvisionCustomerUseCase.java`
```

---

## 10. Red Review

Java TDD may pass through two Red states:

```text
Compiler Red
↓
Minimum missing type or signature
↓
Behavioral Red
```

`Compiler Red` is valid when the test cannot compile because a production type or signature does not exist yet. The next task adds only what is required for compilation. It does not implement the behavior being tested.

`Behavioral Red` means the test compiles and fails because the intended behavior is absent or incorrect.

After the developer reports Red, AI checks:

- whether the state is Compiler Red or Behavioral Red;
- the test expresses the intended behavior;
- failure occurred for the intended reason;
- the assertion is meaningful;
- the test is not tied unnecessarily to implementation details;
- the scope is small enough.

If Red is caused by an unrelated failure, fix the test setup or environment before Green.

---

## 11. Green Task Format

Preferred form:

```text
### Current TDD State

Red

### Task

Implement only the minimum production change required to make the currently failing test Green. Do not implement the next behavior or refactor in this task.

### Target File

<production file>
```

The objective is not code golf. "Minimum" means enough to satisfy the current behavior, with no speculative future feature, while still remaining reasonable Java.

---

## 12. Green Review

After Green, AI does not mechanically move to Refactor or to the next requirement. It first reviews the current evidence.

The review must communicate concise, observable reasoning rather than merely announce the next task.

Use this order:

```text
Current State
Review
Observable Rationale
Decision
Next Task
```

`Observable Rationale` must refer to the current code, test result, or an identified specification statement.

AI reviews:

```text
Correctness
Business Rules
Responsibility
Cohesion
Coupling
Dependency Direction
Test Quality
Maintainability
Readability
Naming
Domain Language Consistency
Duplication
Unnecessary Abstraction
Persistence Leakage
Transaction / SQL Impact
```

Findings use:

```text
Must Fix
Should Fix
Consider
```

The developer performs the correction. AI does not immediately replace the implementation with a finished solution.

Examples of valid rationale:

```text
The same Business Rule is implemented in two methods, so it currently has two responsibility owners. The next step is one refactoring that gives the rule a single owner.
```

```text
No meaningful duplication, mixed responsibility, unclear naming, or inappropriate dependency is visible. The requirement "each customer has a unique customer ID" is still untested, so the next step is a Red task for that behavior.
```

Do not use a general rule such as `DDD recommends it`, `SOLID recommends it`, or `repositories are usually interfaces` as the sole rationale.

---

# Part III — Refactoring

## 13. Refactor Only When There Is a Reason

TDD includes Refactor, but Refactor does not mean "change code every cycle."

If the implementation is already simple, readable, appropriately named, cohesive, and free of meaningful duplication, AI should explicitly state:

```text
No refactor is necessary for this cycle.
```

Then continue to the next Red.

Valid evidence may include:

- meaningful duplication;
- mixed responsibilities;
- a name that obscures intent or Domain meaning;
- an inappropriate dependency direction;
- repeated dependence on the same external role;
- coupling that makes the current behavior difficult to test;
- a Domain concept scattered as primitive values;
- excessive method or test complexity.

An interface may be a reasonable result of refactoring when the current code reveals a stable role or boundary and abstraction improves the present design. It is not required merely because a Repository or Service is commonly represented by an interface.

---

## 14. Green-to-Next-Step Decision

After every Green:

```text
Observe current code and tests
↓
Is there concrete refactoring pressure?
├─ Yes: select one smallest justified refactoring
└─ No: return to the relevant specification
          ↓
        select one smallest untested behavior
          ↓
        create the next Red
```

If Refactor is selected, the review must identify the concrete code evidence and the task must preserve all existing behavior.

If the next Red is selected, cite the relevant requirement, Use Case, or Business Rule in the review and name the one behavior that remains untested.

The task itself still uses only:

```text
Current TDD State
Task
Target File
```

---

## 15. Naming Refactoring Is Based on Readability

Naming review includes:

- class names;
- method names;
- field names;
- local variable names;
- parameter names;
- test method names;
- package names.

The question is:

```text
Can a reader understand intent and responsibility from the name without reconstructing the implementation?
```

Also check consistency with `docs/glossary.md`.

Examples of names that may deserve review depending on context:

```text
process
handle
data
value
info
manager
result
```

These names are not prohibited.

Rename only when a more specific name materially improves readability or Domain meaning. Do not perform naming changes purely for stylistic preference.

When a naming refactor is justified, the review provides the answer before the task:

```text
Current name → Recommended name
Reason: <how the replacement makes intent or responsibility clearer>
```

Do not ask the developer to devise a replacement name as a separate task. The following Refactor task may ask the developer to apply the reviewed names while preserving behavior.

---

# Part IV — Design Pattern Learning

## 16. Do Not Start with a Pattern

Do not begin a task with:

```text
Implement Strategy Pattern.
```

when there is no existing design pressure.

First create correct behavior using normal TDD. This allows the developer to experience the problem the pattern is intended to solve.

---

## 17. Pattern Refactoring Happens After a Completed TDD Cycle

A pattern becomes a learning task only when the current implementation exposes a meaningful problem such as:

```text
repeated algorithm-selection branches
complex object creation
repeated business-condition composition
growing direct coupling between responsibilities
```

Possible examples:

```text
Strategy
→ multiple interchangeable algorithms

Factory
→ creation logic becomes meaningfully complex

Specification
→ business-condition composition becomes difficult

Domain Event
→ direct post-operation coupling grows
```

---

## 18. Pattern Refactor Workflow

Use:

```text
Before
↓
Current Design Pressure
↓
Pattern Explanation
↓
Trade-off
↓
Developer Refactor
↓
Existing Tests Green
↓
After
↓
Comparison
```

AI explains why the pattern is relevant. The developer performs the refactor.

---

## 19. Before / After Must Be Compared

After the refactor, examine:

- which responsibility moved;
- which conditional logic disappeared;
- which dependencies changed;
- whether extension became easier;
- whether readability improved;
- what new abstraction was introduced;
- how many classes/interfaces were added;
- whether the cost is justified.

Pattern use is not automatically better design.

---

## 20. Learning When Not to Use a Pattern

If the variation is simple or hypothetical, AI should explain why a pattern is not introduced yet.

Examples:

```text
only one algorithm exists
no real variation point exists
the branch is simple
abstraction would add more complexity than it removes
```

Avoid premature pattern usage.

---

# Part V — Hints and Full Solutions

## 21. Progressive Hint Levels

When implementation help is requested, use progressively stronger support.

### Level 0 — No Hint

Task and acceptance criteria only.

### Level 1 — Guiding Question

Example:

```text
Which object owns the state being changed?
```

### Level 2 — Direction

Example:

```text
Look at the Domain object rather than the Controller.
```

### Level 3 — Concept

Example:

```text
This may be modeled as behavior on the Entity itself.
```

### Level 4 — Structure / Pseudocode

Provide shape, not finished implementation.

### Level 5 — Partial Code

Provide only the blocked portion.

### Level 6 — Full Solution

Provide complete code only when appropriate.

---

## 22. When Full Solutions Are Allowed

Full solutions are allowed when:

- the developer explicitly asks for one;
- progressive hints have failed to unblock progress;
- a complete implementation is needed for post-attempt comparison;
- the problem is not itself a learning target.

When showing a full solution after an attempt, explain the important differences.

---

# Part VI — Debugging

## 23. Debugging Is Hypothesis-Driven

Default flow:

```text
Observe
↓
Developer Hypothesis
↓
AI Reviews Hypothesis
↓
Choose Evidence
↓
Experiment
↓
Fix
```

Do not treat AI as an automatic error-fixing command.

---

## 24. Developer Hypothesis

When practical, the developer should state:

```text
Observed:
What happened?

Hypothesis:
What do I think caused it?

Evidence:
Why do I think so?
```

AI can then correct or refine the hypothesis.

---

## 25. AI May Teach Missing Knowledge

The developer is not expected to rediscover technical facts.

Questions such as:

```text
What does @Transactional do?
What is an Aggregate Root?
What does EXPLAIN show?
What is MVCC?
```

may be answered directly.

After teaching the concept, AI may reconnect it to the current implementation.

---

# Part VII — SQL and Database Learning

## 26. SQL Performance Workflow

Use:

```text
Predict
↓
Execute
↓
Observe
↓
Explain
↓
Improve
```

Example:

```text
Predict whether the current query will use a sequential scan.

Run EXPLAIN (ANALYZE, BUFFERS).

Observe the plan.

Explain why the planner selected it.

Then evaluate an index or query rewrite.
```

---

## 27. Do Not Skip the Baseline

Before optimization, preserve evidence of the original behavior.

For important experiments, record:

```text
Dataset
Baseline SQL
Baseline Plan
Prediction
Observed Plan
Change
New Plan
Interpretation
Trade-off
```

Permanent SQL experiment reports belong under `docs/sql-experiments/`.

---

## 28. Concurrency Learning

Concurrency experiments may combine TDD and controlled experiments.

Typical flow:

```text
Define invariant
↓
Create race/concurrency test
↓
Observe failure or risk
↓
Explain mechanism
↓
Apply one control strategy
↓
Re-run
↓
Compare alternatives
```

Do not choose a locking strategy merely because it is familiar.

---

# Part VIII — Review Quality

## 29. Review Is More Than Functional Correctness

AI review must include whether the code is understandable and maintainable.

A passing test is necessary but not sufficient.

---

## 30. Must Fix / Should Fix / Consider

### Must Fix

Examples:

- Business Rule violation;
- broken invariant;
- transaction inconsistency;
- incorrect behavior;
- dangerous persistence behavior;
- missing critical test;
- dependency-direction violation that materially damages the design.

### Should Fix

Examples:

- unclear naming;
- avoidable duplication;
- poor readability;
- misplaced responsibility;
- confusing test structure;
- maintainability problem.

### Consider

Examples:

- alternative design;
- future pattern candidate;
- optional simplification;
- later performance investigation.

---

# Part IX — Session Continuity

## 31. `learning-state.md` Is the Current Checkpoint

`docs/learning-state.md` exists so a new AI session can resume without requiring the developer to reconstruct the previous session manually.

It records only current and recent state.

---

## 32. Required State Fields

Maintain:

```text
Current Branch
Current Learning Task
Related Use Cases / Business Rules
Current TDD State
Completed
Changed Files
Current Implementation
Decisions / Reasons
Alternatives Considered
Review Findings
Tests
Open Questions
Next Action
```

---

## 33. TDD State Values

Use one of:

```text
COMPILER_RED
RED
GREEN
REFACTOR
PATTERN_REFACTOR
REVIEW
COMPLETE
NOT_STARTED
```

`COMPILER_RED` is the intermediate state in which the current test does not compile because a deliberately missing production type or signature is required. `RED` means the test compiles and fails for the intended behavioral reason.

---

## 34. Update Timing

Update `learning-state.md` at meaningful checkpoints, especially:

- after confirming Red;
- after Green;
- after an important refactor;
- after design-pattern refactoring;
- after discovering a significant issue;
- before ending a work session;
- after completing a learning task.

Do not rewrite it after every minor line change.

---

## 35. New Session Startup

At the start of a new session:

```text
1. Read AGENTS.md
2. Read docs/INDEX.md
3. Read docs/learning-workflow.md
4. Read docs/learning-state.md
5. Inspect Git state if available
6. Read relevant Use Case / Business Rules
7. Resume from Next Action
```

The AI should briefly confirm the recovered current state before issuing the next task.

---

# Part X — Permanent Records

## 36. Information Goes to the Appropriate Record

| Information | Record |
| --- | --- |
| Current task/state | `docs/learning-state.md` |
| Next action | `docs/learning-state.md` |
| Temporary debugging hypothesis | `docs/learning-state.md` |
| Small code change | Git Commit |
| Feature/change explanation | Pull Request |
| Feature-level design reasoning | PR `Design Decisions` |
| Rejected feature-level alternative | PR `Alternatives Considered` |
| Pattern Before / After | Pull Request |
| Long-lived architecture decision | ADR |
| Business Rule change | Business Rule docs |
| API contract change | API docs |
| SQL performance experiment | `docs/sql-experiments/` |

---

## 37. `learning-state.md` Is Not Permanent History

Do not append every previous task forever.

The file represents:

```text
Where are we now?
What just happened?
What happens next?
```

Git and project documentation preserve history.

---

# Part XI — Completion Criteria

## 38. Normal TDD Task Completion

A learning task is complete when appropriate conditions are satisfied:

```text
relevant tests are Green
no Must Fix review findings remain
readability/naming review completed
required refactoring completed or intentionally skipped
design reasoning is understood
learning-state updated
```

---

## 39. Pattern Task Completion

Additionally:

```text
Before problem understood
Pattern reason understood
Existing behavior tests remain Green
After structure reviewed
Trade-off understood
```

---

## 40. Adapt AI Support to Learning Progress

AI support should decrease for concepts that are becoming familiar.

Example progression:

```text
Early:
Detailed design explanation + precise TDD task

Middle:
Short design reminder + TDD task

Later:
Use Case / Business Rule + minimal guidance
```

Increase explanation again when a genuinely new concept appears.

The objective is not permanent dependence on AI scaffolding.
