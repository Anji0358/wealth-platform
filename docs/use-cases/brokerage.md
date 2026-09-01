# Brokerage Domain

Securities Account、Order、Execution、Positionおよび証券取引に関するUse Caseを定義する。

---

## UC-BRK-001 — Open Securities Account

**Phase:** Phase 1 — Core Banking / MVP
**Status:** Confirmed

### Actor

Customer

### Preconditions

* Customerが存在すること。

### Main Flow

1. CustomerがSecurities Accountの開設を要求する。
2. 既存Securities Accountの有無を確認する。
3. 新しいSecurities Accountを作成する。

### Postconditions

* Customerが最大1つのSecurities Accountを保有する。
* 初期現金残高は0である。

### Failure Cases

* Customerが既にSecurities Accountを持つ場合

  * 新規開設を拒否する。

### Related Business Rules

* BR-BRK-001 — One Securities Account per Customer Initially
* BR-BRK-005 — Securities Account Initial State

---

## UC-BRK-002 — Transfer Cash from Bank

**Phase:** Phase 1 — Core Banking / MVP
**Status:** Confirmed

### Actor

Customer

### Preconditions

* Bank Accountが存在すること。
* Securities Accountが存在すること。
* 両口座が同一Customerに属すること。

### Main Flow

1. CustomerがBank Account、Securities Account、金額を指定する。
2. 資金移動可能性を検証する。
3. Bank Accountから資金を減少させる。
4. Securities Accountの現金残高を増加させる。
5. Ledgerへ記録する。

### Postconditions

* 両口座の現金残高が整合している。
* Ledger Entryの合計が0である。

### Failure Cases

* Available Balance不足の場合

  * 振替を拒否する。
  * どちらの残高も変更しない。
* 口座状態によって操作できない場合

  * 振替を拒否する。
  * Ledgerを変更しない。
* 途中失敗の場合

  * 全体をRollbackする。

### Related Business Rules

* BR-CUS-002 — Customer Ownership Controls Financial Operations
* BR-BRK-008 — Bank and Securities Accounts Must Share the Same Customer
* BR-BRK-009 — Bank-to-Securities Transfer State Requirements
* BR-LDG-001 — Every Cash Movement Has One Ledger Transaction
* BR-LDG-002 — Ledger Transaction Must Be Zero-Sum
* BR-LDG-007 — Ledger and Balance Changes Succeed or Fail Together

### Related Decisions

* OQ-UC-007
* OQ-UC-008

---

## UC-BRK-003 — Transfer Cash to Bank

**Phase:** Phase 1 — Core Banking / MVP
**Status:** Confirmed

### Actor

Customer

### Preconditions

* Securities Accountが存在すること。
* Bank Accountが存在すること。
* 両口座が同一Customerに属すること。

### Main Flow

1. Customerが振替額を指定する。
2. Securities AccountのAvailable Balanceを検証する。
3. Securities Accountから現金を減少させる。
4. Bank Accountへ現金を増加させる。
5. Ledgerへ記録する。

### Postconditions

* 両口座の残高が整合している。
* Ledger Entryの合計が0である。

### Failure Cases

* Available Balance不足の場合

  * 振替を拒否する。
  * 状態を変更しない。
* 途中失敗の場合

  * 資金移動全体をRollbackする。

### Related Business Rules

* BR-CUS-002 — Customer Ownership Controls Financial Operations
* BR-BRK-008 — Bank and Securities Accounts Must Share the Same Customer
* BR-BRK-010 — Securities-to-Bank Transfer State Requirements
* BR-LDG-001 — Every Cash Movement Has One Ledger Transaction
* BR-LDG-002 — Ledger Transaction Must Be Zero-Sum
* BR-LDG-007 — Ledger and Balance Changes Succeed or Fail Together

---

## UC-BRK-004 — Place Buy Order

**Phase:** Phase 3 — Trading
**Status:** Confirmed

### Actor

Customer

### Preconditions

* Securities Accountが存在すること。
* 対象Securityが存在すること。
* 最新Market Priceが存在すること。

### Main Flow

1. CustomerがSecurityと数量を指定する。
2. 最新Market Priceを取得する。
3. 必要現金額を算出する。
4. Available Balanceを検証する。
5. Buy Orderを作成する。
6. 同一Use Case内で全量Executionを作成する。
7. 現金残高へ約定結果を反映する。
8. Positionへ取得結果を反映する。

### Postconditions

* OrderとExecutionが別のドメイン概念として記録されている。
* 注文数量の全量が約定している。
* Positionが更新されている。
* 現金残高およびReserved Amountが整合している。

### Failure Cases

* Available Balance不足の場合

  * Orderを成立させない。
  * Executionを作成しない。
  * Positionを変更しない。
* 対象Securityが取引不可能な場合

  * 注文を拒否する。
  * 資産状態を変更しない。
* 処理途中で失敗した場合

  * 現金、Order、Execution、Positionについて不完全な状態を残さない。

### Related Business Rules

* BR-CUS-002 — Customer Ownership Controls Financial Operations
* BR-BRK-011 — Phase 3 Uses Immediate Full-Fill Market Orders
* BR-BRK-012 — Trading Requires Current Business-Day Market Price
* BR-BRK-013 — Disabled Security Buy/Sell Behavior
* BR-BRK-015 — Buy Order Requires Sufficient Securities Cash
* BR-BRK-017 — Phase 3 Buy Is Atomic
* BR-XDM-010 — Reservation Exists Only for Meaningfully Pending Operations

### Related Decisions

* OQ-UC-003

---

## UC-BRK-005 — Place Sell Order

**Phase:** Phase 3 — Trading
**Status:** Confirmed

### Actor

Customer

### Preconditions

* Securities Accountが存在すること。
* 対象SecurityのPositionが存在すること。
* 最新Market Priceが存在すること。

### Main Flow

1. CustomerがSecurityと売却数量を指定する。
2. 売却可能数量を検証する。
3. Sell Orderを作成する。
4. 初期バージョンでは同一Use Case内で全量Executionを作成する。
5. Positionを更新する。
6. 売却代金をSecurities Accountへ反映する。
7. Realized P/L計算に必要な情報を記録する。

### Postconditions

* 保有数量が売却数量分減少している。
* 売却代金が証券口座現金へ反映されている。
* Executionが記録されている。
* 実現損益を計算可能な状態になっている。

### Failure Cases

* 売却可能数量を超える場合

  * Sell Orderを拒否する。
  * Positionを変更しない。
* 対象Securityが取引不可能な場合

  * 注文を拒否する。
* 処理途中で失敗した場合

  * Order、Execution、Position、現金の一部のみを確定しない。

### Related Business Rules

* BR-CUS-002 — Customer Ownership Controls Financial Operations
* BR-BRK-003 — Position Quantity Must Remain Non-Negative
* BR-BRK-011 — Phase 3 Uses Immediate Full-Fill Market Orders
* BR-BRK-012 — Trading Requires Current Business-Day Market Price
* BR-BRK-013 — Disabled Security Buy/Sell Behavior
* BR-BRK-016 — Sell Order Requires Sufficient Position Quantity
* BR-BRK-018 — Phase 3 Sell Is Atomic

### Related Decisions

* OQ-UC-001
* OQ-UC-002
* OQ-UC-003

---

## UC-BRK-006 — Cancel Order

**Phase:** Phase 6 — Advanced Learning
**Status:** Needs Review

**Review Reason:** OQ-UC-003 — long-lived Order / independent Execution lifecycle must be finalized for Phase 6.

### Actor

Customer

### Preconditions

* Orderが存在すること。
* Limit OrderまたはPartial Executionを含む、未完了Orderのライフサイクルが導入済みであること。

### Main Flow

1. CustomerがOrderのキャンセルを要求する。
2. Orderがキャンセル可能な状態か検証する。
3. Orderをキャンセル状態へ変更する。
4. 拘束中の現金または資産があれば解放する。

### Postconditions

* Orderがキャンセル済みとして記録される。
* 不要になったReserved資産が解放される。

### Failure Cases

* 既に約定済みの場合

  * キャンセルを拒否する。
  * ExecutionおよびPositionを変更しない。
* 既にキャンセル済みの場合

  * 二重キャンセルによる追加状態変化を発生させない。

### Related Business Rules

* BR-XDM-010 — Reservation Exists Only for Meaningfully Pending Operations

---

## UC-BRK-007 — View Orders

**Phase:** Phase 3 — Trading
**Status:** Confirmed

### Actor

Customer

### Preconditions

* Securities Accountが存在すること。

### Main Flow

1. CustomerのOrdersを取得する。
2. 各Orderの状態を取得する。
3. 注文履歴を返す。

### Postconditions

* Orderと現在のOrder Statusを確認できる。
* システム状態を変更しない。

### Failure Cases

* Securities Accountが存在しない場合

  * Order一覧を返さない。

### Related Business Rules

* BR-BRK-011 — Phase 3 Uses Immediate Full-Fill Market Orders
* BR-XDM-009 — Historical Facts Are Append-Oriented

---

## UC-BRK-008 — View Executions

**Phase:** Phase 3 — Trading
**Status:** Confirmed

### Actor

Customer

### Preconditions

* Securities Accountが存在すること。

### Main Flow

1. Customerに属するExecutionを取得する。
2. 約定価格、数量、関連Order等を返す。

### Postconditions

* 過去の成立済み証券取引を確認できる。
* システム状態を変更しない。

### Failure Cases

* Securities Accountが存在しない場合

  * Execution履歴を返さない。

### Related Business Rules

* BR-BRK-011 — Phase 3 Uses Immediate Full-Fill Market Orders
* BR-XDM-009 — Historical Facts Are Append-Oriented

---

## UC-BRK-009 — View Positions

**Phase:** Phase 4 — Position / Portfolio
**Status:** Confirmed

### Actor

Customer

### Preconditions

* Securities Accountが存在すること。

### Main Flow

1. Securities Accountに属するPositionsを取得する。
2. 各Securityの保有数量および取得情報を取得する。
3. Position一覧を返す。

### Postconditions

* 現在保有しているSecurityを確認できる。
* Positionを変更しない。

### Failure Cases

* Securities Accountが存在しない場合

  * Position一覧を返さない。

### Related Business Rules

* BR-BRK-003 — Position Quantity Must Remain Non-Negative
* BR-BRK-004 — Position Represents Current Holdings Only
* BR-BRK-019 — Average Cost Is the Initial Cost Basis Method
* BR-BRK-021 — Remaining Acquisition Cost Preserves Cost-Basis Integrity

---

## UC-BRK-010 — View Securities Account

**Phase:** Phase 1 — Core Banking / MVP
**Status:** Confirmed

### Actor

Customer

### Preconditions

* Securities Accountが存在すること。

### Main Flow

1. Securities Accountを取得する。
2. 以下を確認する。

   * Current Balance
   * Reserved Amount
   * Available Balance
   * Account Status
3. 情報を返す。

### Postconditions

* Securities Accountの現在状態を確認できる。
* システム状態を変更しない。

### Failure Cases

* Securities Accountが存在しない場合

  * 情報を返さない。

### Related Business Rules

* BR-CUS-002 — Customer Ownership Controls Financial Operations
* BR-BRK-002 — Securities Cash Must Remain Non-Negative
* BR-BRK-006 — Restricted Securities Account Behavior

---

## UC-BRK-011 — Close Securities Account

**Phase:** Phase 1 — Core Banking / MVP
**Status:** Confirmed

### Actor

Customer

### Preconditions

* Securities Accountが存在すること。
* Securities AccountのAccount StatusがACTIVEであること。

### Main Flow

1. Customerが口座閉鎖を要求する。
2. システムが閉鎖可能性を検証する。
3. Securities AccountをCLOSEDへ変更する。

### Postconditions

* Securities AccountがCLOSEDとなる。
* 過去の取引・Ledger・Execution履歴は保持される。

### Failure Cases

* Current BalanceまたはReserved Amountが0ではない場合

  * 閉鎖を拒否する。
  * Account Statusを変更しない。
* Positionが存在する場合

  * 閉鎖を拒否する。
  * Account Statusを変更しない。
* 未完了Orderが存在する場合

  * 閉鎖を拒否する。
  * Account Statusを変更しない。
* Account StatusがACTIVEではない場合

  * 閉鎖を拒否する。
  * Account Statusを変更しない。

### Related Business Rules

* BR-BRK-006 — Restricted Securities Account Behavior
* BR-BRK-007 — Securities Account Closure Conditions
