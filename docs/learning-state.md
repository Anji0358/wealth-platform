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
UC-CUS-002 — View Own Customer Profile
```

## Related

### Use Cases

```text
UC-CUS-002
```

### Business Rules

```text
BR-CUS-001, BR-CUS-004
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
- CustomerIdGenerator was introduced as the Application-side ID generation port.
- ProvisionCustomerUseCase creates a Customer and saves it through CustomerRepository.
- ProvisionCustomerUseCaseTest verifies the attributes of the Customer passed to CustomerRepository.

## Changed Files

```text
src/test/java/com/example/wealth_platform/customer/application/ProvisionCustomerUseCaseTest.java
```

## Current Implementation

```text
Customer provisioning behavior is verified. The next use case is viewing the acting Customer's profile.
```

## Decisions / Reasons

- The provisioning test double records the saved Customer as well as its save count, proving both the repository interaction and the saved attributes.

## Alternatives Considered

- None yet for UC-CUS-002.

## Review Findings

```text
No Must Fix or Should Fix findings for the completed UC-CUS-001 test verification.
```

## Tests

### Passed

```text
./mvnw -Dtest=ProvisionCustomerUseCaseTest test
```

### Not Yet Run

```text
./mvnw test has not been rerun; it previously required PostgreSQL on localhost:5432.
```

## Open Questions

```text
Profile representation and the missing-Customer result type must be decided while starting UC-CUS-002.
```

## Next Action

```text
Introduce an Application-side ActorContext port for the acting Customer identity, then start the first UC-CUS-002 Red test.
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
