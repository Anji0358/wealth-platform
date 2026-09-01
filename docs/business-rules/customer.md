# Customer Business Rules

## Invariants

### BR-CUS-001 — Customer Identity Is Internal

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

A Customer is identified by a system-generated internal identifier.

The initial system does not determine whether two Customer records represent the same real-world person.

Identical names are allowed.

#### Rationale

Authentication, KYC, and identity verification are outside the initial scope. Introducing real-world duplicate detection without an authoritative identity source would create an artificial rule.

#### Related Use Cases

- UC-CUS-001 Provision Customer Record
- UC-CUS-002 View Own Customer Profile

---

### BR-CUS-002 — Customer Ownership Controls Financial Operations

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

A Customer may directly perform Customer operations only on Bank Accounts and Securities Accounts owned by that Customer.

Cross-customer movement of money is permitted only through an explicitly defined business operation such as a Banking transfer to another Customer.

#### Violation Result

If ownership validation fails:

- no Account balance changes;
- no Position changes;
- no Order or Execution is created;
- no Ledger Transaction is created.

#### Related Use Cases

- UC-BNK-003 Withdraw Money
- UC-BNK-004 Transfer Between Own Accounts
- UC-BNK-005 Transfer to Another Customer
- UC-BRK-002 Transfer Cash from Bank
- UC-BRK-003 Transfer Cash to Bank
- UC-BRK-004 Place Buy Order
- UC-BRK-005 Place Sell Order

---

### BR-CUS-003 — Customer Record Is Provisioned by Administrator or System

**Status:** Confirmed
**Phase:** Phase 1

#### Rule

Customer self-registration is not part of the current scope.

A Customer record is created only through the Administrator or System provisioning use case. Provisioning creates an internal Customer identity; it does not perform production-grade identity verification, KYC, Authentication, or Authorization.

#### Related Use Cases

- UC-CUS-001 Provision Customer Record

---

### BR-CUS-004 — Acting Customer Identity Is Trusted but Transport-Agnostic

**Status:** Confirmed
**Phase:** Phase 1 learning environment

#### Rule

Customer-initiated use cases receive the Acting Customer through an `ActorContext` abstraction.

The learning environment may obtain this identity from a trusted development-only input such as `X-Acting-Customer-Id`, but Application and Domain code must not depend on an HTTP header.

A `customerId` supplied in a path, query, or request body identifies a target resource only. It is not evidence that the caller is that Customer.

The Application layer must validate ownership before returning Customer-private information or changing Customer-owned state.

#### Related Use Cases

- UC-CUS-002 View Own Customer Profile
- all Customer-initiated Banking, Brokerage, and Portfolio use cases

## Future Considerations

### Duplicate Customer Detection

Future identity models may introduce one or more of:

- unique email;
- external customer number;
- authentication identity;
- KYC / identity verification.

This is a **Future Consideration**, not an unresolved requirement for the initial version.
