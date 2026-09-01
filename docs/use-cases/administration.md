# Administration Domain

AdministratorによるAccount管理およびSecurity管理に関するUse Caseを定義する。

---

## UC-ADM-001 — Freeze Bank Account

**Phase:** Phase 1 — Core Banking / MVP
**Status:** Confirmed

### Actor

Administrator

### Preconditions

* Bank Accountが存在すること。

### Main Flow

1. Administratorが凍結対象口座を指定する。
2. 状態遷移可能性を検証する。
3. Account StatusをFROZENへ変更する。

### Postconditions

* Bank AccountがFROZENとなる。

### Failure Cases

* 状態遷移が許可されない場合

  * Account Statusを変更しない。

### Related Business Rules

* TBD

### Open Questions

* OQ-UC-007

---

## UC-ADM-002 — Unfreeze Bank Account

**Phase:** Phase 1 — Core Banking / MVP
**Status:** Confirmed

### Actor

Administrator

### Preconditions

* Bank Accountが存在すること。

### Main Flow

1. FROZEN口座を指定する。
2. 状態遷移可能性を検証する。
3. ACTIVEへ変更する。

### Postconditions

* Bank AccountがACTIVEとなる。

### Failure Cases

* FROZENではない口座の場合

  * 不正な状態遷移を発生させない。

### Related Business Rules

* TBD

---

## UC-ADM-003 — Restrict Securities Account

**Phase:** Phase 1 — Core Banking / MVP
**Status:** Confirmed

### Actor

Administrator

### Preconditions

* Securities Accountが存在すること。

### Main Flow

1. 対象口座を指定する。
2. 状態遷移を検証する。
3. RESTRICTEDへ変更する。

### Postconditions

* Securities AccountがRESTRICTEDとなる。

### Failure Cases

* 状態遷移できない場合

  * Account Statusを変更しない。

### Related Business Rules

* TBD

### Open Questions

* OQ-UC-007

---

## UC-ADM-004 — Unrestrict Securities Account

**Phase:** Phase 1 — Core Banking / MVP
**Status:** Confirmed

### Actor

Administrator

### Preconditions

* Securities Accountが存在すること。

### Main Flow

1. RESTRICTED口座を指定する。
2. ACTIVEへの遷移を検証する。
3. Account StatusをACTIVEへ変更する。

### Postconditions

* Securities AccountがACTIVEとなる。

### Failure Cases

* RESTRICTEDではない場合

  * 不正な状態変更を行わない。

### Related Business Rules

* TBD

---

## UC-ADM-005 — Register Security

**Phase:** Phase 2 — Market
**Status:** Needs Review

### Actor

Administrator

### Preconditions

* 登録対象Securityがまだ存在しないこと。

### Main Flow

1. Security情報を指定する。
2. Initial Price、Expected Return、Volatilityを設定する。
3. Securityを取扱対象として登録する。

### Postconditions

* Market Price生成対象として利用できるSecurityが存在する。

### Failure Cases

* 必要パラメータが不正な場合

  * Securityを登録しない。

### Related Business Rules

* TBD

---

## UC-ADM-006 — Update Security Parameters

**Phase:** Phase 2 — Market
**Status:** Needs Review

### Actor

Administrator

### Preconditions

* Securityが存在すること。

### Main Flow

1. Securityを指定する。
2. 更新対象の市場モデルパラメータを指定する。
3. パラメータを更新する。

### Postconditions

* 後続の市場価格生成で使用可能なパラメータが更新される。
* 過去のMarket Price履歴は変更されない。

### Failure Cases

* 不正なパラメータの場合

  * 更新を拒否する。
  * 既存設定を保持する。

### Related Business Rules

* TBD

### Pending Decision

`μ`・`σ`等の変更がいつから価格生成へ適用されるかは、`market-simulation.md` のOpen Questionとして決定する。

---

## UC-ADM-007 — Disable Security

**Phase:** Phase 2 — Market
**Status:** Needs Review

### Actor

Administrator

### Preconditions

* Securityが存在すること。

### Main Flow

1. Securityを指定する。
2. 無効化可能性を検証する。
3. Securityを新規取引対象から除外する。

### Postconditions

* Securityの過去のMarket Price等は保持される。
* Securityを物理削除しない。

### Failure Cases

* 無効化が許可されない状態の場合

  * Securityの状態を変更しない。

### Related Business Rules

* TBD

### Open Questions

* OQ-UC-006
