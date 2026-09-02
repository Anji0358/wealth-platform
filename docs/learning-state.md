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
BankAccount Domain Model — initial state, deposit, and withdrawal
```

## Related

### Use Cases

```text
UC-BNK-001, UC-BNK-002, UC-BNK-003
```

### Business Rules

```text
BR-BNK-001, BR-BNK-002, BR-BNK-004, BR-BNK-005, BR-BNK-009
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
COMPILER_RED
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
- BankAccount initializes Current Balance and Reserved Amount to zero, and Status to ACTIVE.
- BankAccount supports valid deposit and withdrawal, and rejects non-positive or insufficient withdrawals/deposits.
- AccountType supports ORDINARY_DEPOSIT and SAVINGS.
- FROZEN is an Account Status; freezing changes Status without changing Account Type.

## Changed Files

```text
src/main/java/com/example/wealth_platform/banking/domain/AccountStatus.java
src/main/java/com/example/wealth_platform/banking/domain/AccountType.java
src/main/java/com/example/wealth_platform/banking/domain/BankAccount.java
src/test/java/com/example/wealth_platform/banking/domain/BankAccountTest.java
```

## Current Implementation

```text
The BankAccount Domain Model has initial balance, deposit, withdrawal, and the ACTIVE-to-FROZEN Status transition.
```

## Decisions / Reasons

- Whole-JPY balances use long in the current Domain Model, aligned with the data model's BIGINT semantics.
- Available Balance is derived from Current Balance minus Reserved Amount rather than stored independently.
- Account Type is required when creating BankAccount; it is distinct from Account Status.

## Alternatives Considered

- A dedicated Money type is deferred until a concrete calculation or currency-complexity pressure appears.

## Review Findings

```text
No outstanding Must Fix or Should Fix findings for the completed BankAccount behavior.
```

## Tests

### Passed

```text
./mvnw -Dtest=ProvisionCustomerUseCaseTest test
./mvnw -Dtest=ViewOwnCustomerProfileUseCaseTest test
./mvnw -Dtest=BankAccountTest test
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
Verify the first permitted operation for a FROZEN Bank Account: receiving a Deposit.
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
