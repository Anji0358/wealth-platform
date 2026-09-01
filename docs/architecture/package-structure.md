# Recommended Package Structure

## Root

Recommended root package:

```text
com.example.wealthplatform
```

The exact reverse-domain prefix may be adjusted when the project package is created.

## Suggested Structure

```text
src/main/java/.../wealthplatform/

customer/
├── domain/
├── application/
├── presentation/
└── infrastructure/

banking/
├── domain/
├── application/
├── presentation/
└── infrastructure/

brokerage/
├── domain/
├── application/
├── presentation/
└── infrastructure/

ledger/
├── domain/
├── application/
└── infrastructure/

market/
├── domain/
├── application/
├── presentation/
└── infrastructure/

portfolio/
├── application/
├── presentation/
└── infrastructure/

administration/
├── application/
└── presentation/

shared/
```

## Example: Banking

A later implementation may evolve toward:

```text
banking/
├── domain/
│   ├── BankAccount.java
│   ├── BankAccountId.java
│   ├── BankAccountStatus.java
│   ├── BankAccountType.java
│   └── BankAccountRepository.java
│
├── application/
│   ├── OpenBankAccountUseCase.java
│   ├── DepositMoneyUseCase.java
│   ├── WithdrawMoneyUseCase.java
│   └── TransferMoneyUseCase.java
│
├── presentation/
│   ├── BankAccountController.java
│   └── dto/
│
└── infrastructure/
    └── persistence/
        ├── BankAccountJpaEntity.java
        ├── SpringDataBankAccountRepository.java
        ├── JpaBankAccountRepositoryAdapter.java
        └── BankAccountMapper.java
```

This is illustrative, not a requirement to create every class immediately.

## Package Rules

1. Create packages only when code needs them.
2. Domain code must not import presentation/infrastructure types.
3. Persistence-specific entities remain under infrastructure.
4. HTTP DTOs remain under presentation.
5. Application Use Cases coordinate modules but should not become utility collections.
6. `shared` remains intentionally limited.

Transport-agnostic cross-cutting concepts such as `ActorContext` may live in a narrowly scoped `shared/actor` package. HTTP header parsing remains in Presentation.
