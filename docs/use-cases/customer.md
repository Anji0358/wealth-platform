# Customer Domain

Customerに関するUse Caseを定義する。

---

## UC-CUS-001 — Provision Customer Record

**Phase:** Phase 1 — Core Banking / MVP  
**Status:** Confirmed

### Actor

Administrator / System

### Preconditions

- Customer record作成に必要な情報が提供されていること。

### Main Flow

1. AdministratorまたはSystemがCustomer record作成に必要な情報を提供する。
2. システムが内容を検証する。
3. 新しいCustomerを登録する。
4. Customerを識別するための識別子を確定する。

### Postconditions

- Customerがシステム上に存在する。
- 後続のBank AccountおよびSecurities Accountの所有者として参照できる。

### Failure Cases

- Customerとして登録できない入力の場合
  - 登録を拒否する。
  - Customerを新規作成しない。

### Related Business Rules

- BR-CUS-001 — Customer Identity Is Internal
- BR-CUS-003 — Customer Record Is Provisioned by Administrator or System

---

## UC-CUS-002 — View Own Customer Profile

**Phase:** Phase 1 — Core Banking / MVP  
**Status:** Confirmed

### Actor

Customer

### Preconditions

- Customerが存在すること。

### Main Flow

1. ActorContextからActing Customerを特定する。
2. Acting Customerの基本情報を取得する。
3. Customerが保有するBank Accountの識別情報を取得する。
4. Securities Accountの有無を取得する。
5. Customer Profileとして返す。

### Postconditions

- Customer自身と保有口座の概要を確認できる。
- Customer、Bank Account、Securities Accountの状態を変更しない。

### Failure Cases

- Customerが存在しない場合
  - Profileを返さない。
  - システム状態を変更しない。

### Related Business Rules

- BR-CUS-001 — Customer Identity Is Internal
- BR-CUS-004 — Acting Customer Identity Is Trusted but Transport-Agnostic
