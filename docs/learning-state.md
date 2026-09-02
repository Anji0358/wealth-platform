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
GREEN
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
- ViewOwnCustomerProfileUseCase returns the Acting Customer's basic information through ActorContext and CustomerRepository.
- ViewOwnCustomerProfileUseCase throws NoSuchElementException when the Acting Customer does not exist.

## Changed Files

```text
src/main/java/com/example/wealth_platform/customer/application/ActorContext.java
src/main/java/com/example/wealth_platform/customer/application/ViewOwnCustomerProfileUseCase.java
src/main/java/com/example/wealth_platform/customer/domain/CustomerRepository.java
src/test/java/com/example/wealth_platform/customer/application/ProvisionCustomerUseCaseTest.java
src/test/java/com/example/wealth_platform/customer/application/ViewOwnCustomerProfileUseCaseTest.java
```

## Current Implementation

```text
Customer provisioning and the basic Customer portion of profile viewing are verified. Bank Account identifiers and Securities Account presence are not implemented yet.
```

## Decisions / Reasons

- ActorContext is an Application-side port for the acting Customer identity, so Application code remains independent of HTTP and authentication transport.
- CustomerRepository returns Optional<Customer> from findById; ViewOwnCustomerProfileUseCase currently maps an absent Acting Customer to NoSuchElementException.
- Small handwritten test doubles isolate ActorContext and CustomerRepository while keeping the Application behavior visible.

## Alternatives Considered

- A database or mocking framework is not used because the current tests focus solely on Application behavior.

## Review Findings

```text
No outstanding Must Fix or Should Fix findings for the completed basic Customer-profile behavior.
```

## Tests

### Passed

```text
./mvnw -Dtest=ProvisionCustomerUseCaseTest test
./mvnw -Dtest=ViewOwnCustomerProfileUseCaseTest test
```

### Not Yet Run

```text
./mvnw test: Customer-related tests pass; WealthPlatformApplicationTests.contextLoads fails because PostgreSQL is unavailable.
```

## Open Questions

```text
The API-level representation and HTTP mapping of a missing Customer are not decided.
```

## Next Action

```text
Inspect the Account-related specifications and existing model before selecting the smallest untested UC-CUS-002 behavior.
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
