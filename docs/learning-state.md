# Learning State

> This file is the current learning checkpoint.
> Keep it concise.
> Replace outdated state rather than turning this file into permanent history.

## Current Branch

```text
feature/customer-domain-model
```

## Current Learning Task

```text
UC-CUS-001 — Provision Customer Record
```

## Related

### Use Cases

```text
UC-CUS-001
```

### Business Rules

```text
BR-CUS-001, BR-CUS-003
```

### ADR / SQL Experiment

```text
None
```

## Current TDD State

```text
COMPLETE
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

- Customer Domain Model was introduced as a pure Java class.
- Customer retains its internal UUID, name, and creation time.
- Customer unit test verifies the initial model attributes.
- CustomerRepository was introduced as the Domain-side save port.

## Changed Files

```text
src/main/java/com/example/wealth_platform/customer/domain/Customer.java
src/main/java/com/example/wealth_platform/customer/domain/CustomerRepository.java
src/test/java/com/example/wealth_platform/customer/domain/CustomerTest.java
```

## Current Implementation

```text
Customer Domain Model and CustomerRepository port; Customer provisioning Use Case is not implemented.
```

## Decisions / Reasons

- Customer is a pure Java Domain Model without Spring or persistence dependencies.
- Customer identity uses UUID, with name and createdAt as the minimal conceptual attributes from DM-003.
- CustomerRepository is a Domain-side port with a single save operation; infrastructure will implement it later.

## Alternatives Considered

- Application Use Case, repository, ID generation, and Clock were deferred so that the first behavior remained a small Domain Model exercise.

## Review Findings

```text
No Must Fix or Should Fix findings.
```

## Tests

### Passed

```text
./mvnw -Dtest=CustomerTest test
```

### Not Yet Run

```text
./mvnw test fails because PostgreSQL is not listening on localhost:5432; CustomerTest itself passes.
```

## Open Questions

```text
Customer name validation rules are not yet specified; do not introduce validation until the specification is clarified.
```

## Next Action

```text
Begin UC-CUS-001 Application Use Case with a small Red test for saving a provisioned Customer.
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
