# Use Case Open Questions

Use Caseに関連する未決事項を一元管理する。

各Open Questionは、関連するUse Caseと、決定が必要となる時点を明示する。

---

## OQ-UC-001 — Acquisition Cost and Realized P/L Method

**Related Use Cases**

* UC-BRK-005 Place Sell Order
* UC-PFL-002 View Profit and Loss

**Decision Required Before**

Phase 4 — Position / Portfolio

**Question**

証券売却時の取得原価およびRealized P/Lをどの方式で計算するか。

候補例：

* Average Cost
* FIFO

詳細はBusiness Rules策定時に判断する。

---

## OQ-UC-002 — Reservation of Securities for Sell Orders

**Related Use Cases**

* UC-BRK-005 Place Sell Order
* UC-BRK-006 Cancel Order

**Decision Required Before**

Phase 3 — Trading

**Question**

Sell Orderを作成してからExecutionが成立するまで、売却予定数量をどのように拘束するか。

Available QuantityとReserved Quantityの概念をPositionに導入するかを検討する。

---

## OQ-UC-003 — Separation of Execution Processing

**Related Use Cases**

* UC-BRK-004 Place Buy Order
* UC-BRK-005 Place Sell Order

**Decision Required Before**

Phase 6 — Advanced Learning

**Question**

Limit OrderまたはPartial Executionを導入するとき、Execution処理をOrder作成Use Caseから分離し、Systemが開始する独立Use Caseとするか。

初期バージョンではOrder作成と即時全量Executionを同一Use Case内で実行する。

---

## OQ-UC-004 — Bank Account Closure Conditions

**Related Use Cases**

* UC-BNK-008 Close Bank Account

**Decision Required Before**

UC-BNK-008の実装

**Question**

Bank AccountをCLOSEDへ変更するために必要な条件を決定する。

検討例：

* Current Balanceが0である必要があるか
* Reserved Amountが0である必要があるか
* FROZENから直接CLOSEDへ遷移できるか

---

## OQ-UC-005 — Securities Account Closure Conditions

**Related Use Cases**

* UC-BRK-011 Close Securities Account

**Decision Required Before**

UC-BRK-011の実装

**Question**

Securities Accountを閉鎖可能とする条件を決定する。

検討例：

* Current Balanceが0である必要があるか
* Reserved Amountが0である必要があるか
* Positionを1件も保有していない必要があるか
* 未完了Orderが存在してはならないか

---

## OQ-UC-006 — Effect of Disabling a Security

**Related Use Cases**

* UC-ADM-007 Disable Security
* UC-BRK-004 Place Buy Order
* UC-BRK-005 Place Sell Order
* UC-BRK-009 View Positions

**Decision Required Before**

Phase 3 — Trading

**Question**

SecurityをDisableした場合に、既存Positionや既存Orderをどのように扱うか。

少なくとも過去のExecution、Market Price、Position履歴を削除しないことは確定している。

---

## OQ-UC-007 — Operations Allowed by Account Status

**Related Use Cases**

* Deposit Money
* Withdraw Money
* Bank Transfers
* Brokerage Cash Transfers
* Freeze / Restrict operations

**Decision Required Before**

Phase 1 — Core Banking / MVPのBusiness Rules確定

**Question**

Bank AccountがFROZENの場合、およびSecurities AccountがRESTRICTEDの場合に、各操作をどこまで許可するか。

特に以下を決定する必要がある。

* 入金を許可するか
* 他口座からの受取を許可するか
* 出金を禁止するか
* 銀行→証券振替を禁止するか
* 証券→銀行振替を許可するか

この判断は `business-rules.md` で確定する。

---

## OQ-UC-008 — Reservation Lifecycle for Cash Transfers

**Related Use Cases**

* UC-BNK-004
* UC-BNK-005
* UC-BRK-002
* UC-BRK-003

**Decision Required Before**

Phase 1 — Core Banking / MVPのBusiness Rules確定

**Question**

同期的に完了するPhase 1の資金移動において、Reserved Amountをどのタイミングで設定・解放するか。

以下を検討する。

* Transaction内部だけで一時的にReservedとするか
* ユーザーから確認可能な中間状態を持つか
* Phase 1では同期処理のためReservedを実質的に使用しないか

Current Balance / Reserved Amount / Available Balanceの定義自体は確定済みであり、ここでは資金移動Use Caseでのライフサイクルのみを決定する。
