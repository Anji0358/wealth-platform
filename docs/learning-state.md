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
SecuritiesAccount Domain Model — initial state
```

## Related

### Use Cases

```text
UC-BRK-001
```

### Business Rules

```text
BR-BRK-005
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
- A FROZEN BankAccount accepts Deposits and rejects Withdrawals.
- BankAccount supports the implemented ACTIVE, FROZEN, and CLOSED transitions and closure conditions.
- The first application-test Red for OpenBankAccountUseCase, including a BankAccountRepository save port and BankAccountIdGenerator port, has been written.
- OpenBankAccountUseCase creates and saves a BankAccount with a generated ID and fixed Clock time.
- BankAccount constructor order is `(accountId, customerId, accountType, createdAt)` because BankAccount is the constructed entity.
- LedgerTransaction snapshots its source Entries and does not expose mutable Entries.
- LedgerAccount retains its ID and kind as a Ledger-specific identity.
- SecuritiesAccount starts with zero current and reserved balances and ACTIVE status.

## Changed Files

```text
src/main/java/com/example/wealth_platform/banking/domain/AccountStatus.java
src/main/java/com/example/wealth_platform/banking/domain/AccountType.java
src/main/java/com/example/wealth_platform/banking/domain/BankAccount.java
src/main/java/com/example/wealth_platform/banking/domain/BankAccountRepository.java
src/main/java/com/example/wealth_platform/banking/application/BankAccountIdGenerator.java
src/main/java/com/example/wealth_platform/banking/application/OpenBankAccountUseCase.java
src/test/java/com/example/wealth_platform/banking/domain/BankAccountTest.java
src/test/java/com/example/wealth_platform/banking/application/OpenBankAccountUseCaseTest.java
src/main/java/com/example/wealth_platform/ledger/domain/LedgerAccount.java
src/main/java/com/example/wealth_platform/ledger/domain/LedgerAccountKind.java
src/main/java/com/example/wealth_platform/ledger/domain/LedgerTransaction.java
src/test/java/com/example/wealth_platform/ledger/domain/LedgerAccountTest.java
src/test/java/com/example/wealth_platform/ledger/domain/LedgerTransactionTest.java
src/main/java/com/example/wealth_platform/brokerage/domain/SecuritiesAccount.java
src/main/java/com/example/wealth_platform/brokerage/domain/SecuritiesAccountStatus.java
src/test/java/com/example/wealth_platform/brokerage/domain/SecuritiesAccountTest.java
```

## Current Implementation

```text
LedgerTransaction rejects collections with fewer than two Ledger Entries, non-zero sums, and arithmetic overflow; retains an immutable snapshot of valid Entries. LedgerEntry rejects a zero amount and a null Ledger Account ID, and retains its Ledger Account ID and signed amount. LedgerAccount retains its UUID and kind.
SecuritiesAccount initializes its cash balances and Available Balance to zero and its status to ACTIVE; it accepts a positive cash receipt, rejects non-positive amounts, and permits a receipt that reaches Long.MAX_VALUE.
```

## Decisions / Reasons

- Whole-JPY balances use long in the current Domain Model, aligned with the data model's BIGINT semantics.
- Available Balance is derived from Current Balance minus Reserved Amount rather than stored independently.
- Account Type is required when creating BankAccount; it is distinct from Account Status.

## Alternatives Considered

- A dedicated Money type is deferred until a concrete calculation or currency-complexity pressure appears.
- Customer and Bank Account IDs remain UUIDs for now; a type-safe ID value object is deferred until repeated positional-ID errors justify its scope.

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
./mvnw -Dtest=BankAccountTest,OpenBankAccountUseCaseTest test
./mvnw -Dtest=LedgerEntryTest,LedgerTransactionTest,LedgerAccountTest test
./mvnw -Dtest=SecuritiesAccountTest test
```

### Not Yet Run

```text
./mvnw test: Customer-related tests pass; WealthPlatformApplicationTests.contextLoads fails because PostgreSQL is unavailable.
./mvnw clean -Dtest=OpenBankAccountUseCaseTest test: could not run because Maven could not write the missing clean-plugin artifact to the read-only ~/.m2 cache.
```

## Open Questions

```text
The API-level representation and HTTP mapping of a missing Customer are not decided.
```

## Next Action

```text
Write the next Red: SecuritiesAccount decreases cash when it sends a positive amount within Available Balance.
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
