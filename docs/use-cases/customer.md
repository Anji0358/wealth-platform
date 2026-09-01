# Customer Domain

Customerに関するUse Caseを定義する。

---

## UC-CUS-001 — Register Customer

**Phase:** Phase 1 — Core Banking / MVP  
**Status:** Confirmed

### Actor

Customer

### Preconditions

- 同一のCustomerとして既に登録済みではないこと。

### Main Flow

1. Customerが登録に必要な情報を提供する。
2. システムが登録内容を検証する。
3. 新しいCustomerを登録する。
4. Customerを識別するための識別子を確定する。

### Postconditions

- Customerがシステム上に存在する。
- 後続のBank AccountおよびSecurities Accountの所有者として参照できる。

### Failure Cases

- Customerとして登録できない入力の場合
  - 登録を拒否する。
  - Customerを新規作成しない。
- 同一Customerとして扱われる登録が既に存在する場合
  - 重複登録を拒否する。
  - 既存Customerを変更しない。

### Related Business Rules

- TBD — `business-rules.md` 作成後に紐付ける。

---

## UC-CUS-002 — View Customer Profile

**Phase:** Phase 1 — Core Banking / MVP  
**Status:** Confirmed

### Actor

Customer

### Preconditions

- Customerが存在すること。

### Main Flow

1. Customerの基本情報を取得する。
2. Customerが保有するBank Accountの識別情報を取得する。
3. Securities Accountの有無を取得する。
4. Customer Profileとして返す。

### Postconditions

- Customer自身と保有口座の概要を確認できる。
- Customer、Bank Account、Securities Accountの状態を変更しない。

### Failure Cases

- Customerが存在しない場合
  - Profileを返さない。
  - システム状態を変更しない。

### Related Business Rules

- TBD — `business-rules.md` 作成後に紐付ける。