# Learning State

> This file is the current learning checkpoint.
> Keep it concise.
> Replace outdated state rather than turning this file into permanent history.

## Current Branch

```text
main
```

## Current Learning Task

```text
Cross-document specification consistency review
```

## Related

### Use Cases

```text
UC-CUS-001, UC-CUS-002, UC-MKT-004, UC-BRK-006
```

### Business Rules

```text
BR-CUS-003, BR-CUS-004, BR-MKT-012, BR-MKT-013
```

### ADR / SQL Experiment

```text
ADR-003
```

## Current TDD State

```text
REVIEW
```

Allowed values:

```text
NOT_STARTED
RED
GREEN
REFACTOR
PATTERN_REFACTOR
REVIEW
COMPLETE
```

## Completed

- Project specifications and core development policies have been prepared.
- Git workflow has been defined.
- Learning workflow has been defined.
- Cross-document consistency decisions DR-001 through DR-005 have been propagated from Requirements through ADR.
- ADR-003 records the development ActorContext identity boundary.

## Changed Files

```text
Documentation only; see current git diff.
```

## Current Implementation

```text
No active implementation task.
```

## Decisions / Reasons

- Design learning is primarily input-oriented.
- AI should explain the design choice, reason, decision criterion, and realistic alternatives.
- Implementation learning is primarily output-oriented through TDD.
- AI gives one Red / Green / Refactor task at a time.
- Design patterns are introduced only after a normal implementation exposes a concrete design pressure.
- Naming refactoring is evaluated from the perspective of readability and Domain meaning.

## Alternatives Considered

- Requiring the developer to design every component from zero was rejected because it creates excessive cognitive load while learning Java, Spring, TDD, SQL, and DDD simultaneously.
- Introducing design patterns before experiencing the original design problem was rejected because it hides the motivation for the pattern.

## Review Findings

```text
OQ-UC-003 remains deferred to Phase 6 and is the explicit Review Reason for UC-BRK-006.
```

## Tests

### Passed

```text
Documentation reference and traceability checks passed; application tests were not run because code was not changed.
```

### Not Yet Run

```text
Not applicable.
```

## Open Questions

```text
OQ-UC-003 — finalize the long-lived Order / independent Execution lifecycle before implementing UC-BRK-006.
```

## Next Action

```text
Review the documentation diff. Do not begin UC-BRK-006 until OQ-UC-003 is resolved.
```

## Session Resume Note

A new session should:

```text
1. Read AGENTS.md.
2. Read docs/INDEX.md.
3. Read docs/learning-workflow.md.
4. Read this file.
5. Inspect Git status/diff if repository access is available.
6. Read the relevant Use Case and Business Rules.
7. Resume from "Next Action".
```
