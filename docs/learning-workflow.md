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
Red Task
↓
Red Review
↓
Green Task
↓
Green Review
↓
Normal Refactor if justified
↓
TDD Cycle Complete
↓
Pattern Refactor if justified
↓
Before / After Review
↓
Next Red
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

Each task must have one production implementation target: one class, interface, or method with one clear responsibility. A task must not require the developer to introduce multiple production types merely to make it possible to complete the task.

When a later behavior needs a missing port or boundary type, introduce that type in its own small task first. A trivial declaration without behavior does not require artificial TDD; apply Red → Green → Refactor when behavior is being added.

Do not issue:

```text
Write the test,
implement it,
and refactor it.
```

in one instruction.

Instead:

```text
Red Task
↓
Developer completion
↓
Review
↓
Green Task
↓
Developer completion
↓
Review
↓
Refactor Task if required
```

---

## 9. Red Task Format

Preferred form:

```text
Create a test that verifies <specific behavior>.

Do not change production code yet.

Run the test and confirm that it is Red for the expected reason.
```

Example:

```text
Create a test that verifies that depositing a positive amount increases the BankAccount current balance.

Do not change production code yet.

Run the test and confirm that it fails for the expected reason.
```

---

## 10. Red Review

After the developer reports Red, AI checks:

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
Implement the minimum production change required to make the previous test Green.

Do not implement <future behavior> yet.
```

The objective is not code golf. "Minimum" means enough to satisfy the current behavior, with no speculative future feature, while still remaining reasonable Java.

---

## 12. Green Review

After Green, AI reviews:

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

---

# Part III — Refactoring

## 13. Refactor Only When There Is a Reason

TDD includes Refactor, but Refactor does not mean "change code every cycle."

If the implementation is already simple, readable, appropriately named, cohesive, and free of meaningful duplication, AI should explicitly state:

```text
No refactor is necessary for this cycle.
```

Then continue to the next Red.

---

## 14. Normal Refactor Task Format

When a refactor is justified, explain:

```text
Current Problem
Reason
Target Improvement
Behavior Constraint
```

Example:

```text
The validation is duplicated in two methods.

This makes the same Business Rule changeable from multiple places.

Refactor so the rule has one responsibility owner.

Keep all existing tests Green.
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
RED
GREEN
REFACTOR
PATTERN_REFACTOR
REVIEW
COMPLETE
NOT_STARTED
```

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
