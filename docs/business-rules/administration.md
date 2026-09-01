# Administration Business Rules

## Bank Account Administration

### BR-ADM-001 — Administrator May Freeze Active Bank Account

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

An Administrator may transition:

`Bank Account ACTIVE -> FROZEN`

The financial-operation effects of `FROZEN` are defined in Banking business rules.

#### Related Use Cases

- UC-ADM-001 Freeze Bank Account

---

### BR-ADM-002 — Administrator May Unfreeze Frozen Bank Account

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

An Administrator may transition:

`Bank Account FROZEN -> ACTIVE`

Undefined or idempotent state-transition requests are rejected.

#### Related Use Cases

- UC-ADM-002 Unfreeze Bank Account

---

## Securities Account Administration

### BR-ADM-003 — Administrator May Restrict Active Securities Account

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

An Administrator may transition:

`Securities Account ACTIVE -> RESTRICTED`

#### Related Use Cases

- UC-ADM-003 Restrict Securities Account

---

### BR-ADM-004 — Administrator May Unrestrict Restricted Securities Account

**Status:** Confirmed  
**Phase:** Phase 1

#### Rule

An Administrator may transition:

`Securities Account RESTRICTED -> ACTIVE`

#### Related Use Cases

- UC-ADM-004 Unrestrict Securities Account

---

## Security Administration

### BR-ADM-005 — Administrator Registers Security

**Status:** Confirmed  
**Phase:** Phase 2

#### Rule

Security registration must satisfy all Market Security validation rules, including:

- unique Security Code;
- immutable Security Code after registration;
- Initial Price > 0;
- Volatility >= 0.

#### Related Use Cases

- UC-ADM-005 Register Security

---

### BR-ADM-006 — Administrator Updates Security Parameters

**Status:** Confirmed  
**Phase:** Phase 2

#### Rule

Administrator may update mutable simulation parameters such as Expected Return and Volatility, provided their validation rules remain satisfied.

Historical Market Prices are not recalculated or overwritten.

#### Related Use Cases

- UC-ADM-006 Update Security Parameters

---

### BR-ADM-007 — Administrator Disables Security

**Status:** Confirmed  
**Phase:** Phase 2

#### Rule

Administrator may transition:

`Security ACTIVE -> DISABLED`

Disabling a Security:

- prohibits new Buy Orders;
- does not stop Market Price generation;
- does not delete history;
- still allows Sell Orders that reduce existing Positions.

#### Related Use Cases

- UC-ADM-007 Disable Security

---

### BR-ADM-008 — Administrator Enables Security

**Status:** Confirmed  
**Phase:** Phase 2

#### Rule

Administrator may transition:

`Security DISABLED -> ACTIVE`

After re-enabling, new Buy Orders may again be accepted if all other Trading rules are satisfied.

#### Related Use Cases

- UC-ADM-008 Enable Security

---

## State Transition Rule

### BR-ADM-009 — Undefined Administrative State Transitions Are Rejected

**Status:** Confirmed  
**Phase:** Phase 1+

#### Rule

Administrative requests that do not correspond to a defined state transition are rejected.

Examples:

- `ACTIVE -> ACTIVE`
- `FROZEN -> FROZEN`
- `CLOSED -> ACTIVE`
- `DISABLED -> DISABLED`

No related business state changes on rejection.
