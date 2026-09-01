# Use Cases

## 1. Purpose

本ドキュメントは、wealth-platformにおいてActorが達成しようとする業務目的と、その際にシステムが満たすべき振る舞いをUse Caseとして定義する。

Use Caseは業務領域ごとに整理し、各Use Caseについて以下を記述する。

* Phase
* Status
* Actor
* Preconditions
* Main Flow
* Postconditions
* Failure Cases
* Related Business Rules
* Open Questions（必要な場合）

Use CaseはAPIや画面単位ではなく、業務目的およびBusiness Ruleの違いを基準として分割する。

---

## 2. Status

### Confirmed

Use Caseの目的および主要な振る舞いが確定しており、実装対象として扱える状態。

### Needs Review

Use Caseを実装すること自体は確定しているが、実装前に詳細仕様の追加検討が必要な状態。

原則として、

* Phase 1 — Core Banking / MVP：Confirmed
* Phase 2 — Market：Needs Review
* Phase 3 — Trading：Needs Review
* Phase 4 — Position / Portfolio：Needs Review

とする。

ただし、Phase 1であっても未決のBusiness Ruleを持つUse CaseはNeeds Reviewとする。

---

# 3. Customer Domain

Customer DomainのUse Caseは以下を参照する。

- [Customer Use Cases](./use-cases/customer.md)

---

# 4. Banking Domain

Banking DomainのUse Caseは以下を参照する。

* [Banking Use Cases](./use-cases/banking.md)

---

# 5. Brokerage Domain

Brokerage DomainのUse Caseは以下を参照する。

* [Brokerage Use Cases](./use-cases/brokerage.md)

---

# 6. Market Domain

Market DomainのUse Caseは以下を参照する。

* [Market Use Cases](./use-cases/market.md)

---

# 7. Portfolio Domain

## UC-PFL-001 — View Portfolio Overview

**Phase:** Phase 4 — Position / Portfolio
**Status:** Needs Review

### Actor

Customer

### Preconditions

* Customerが存在すること。

### Main Flow

1. CustomerのBank Accountsを取得する。
2. Securities Accountの現金残高を取得する。
3. Positionsを取得する。
4. 各Securityの最新Market Priceを取得する。
5. 各PositionのValuationを計算する。
6. Total Assetsを計算する。
7. Portfolio Overviewとして返す。

### Postconditions

* Customerが現在保有する金融資産の全体像を確認できる。
* 資産状態を変更しない。

### Failure Cases

* Positionに対応するMarket Priceが存在しない場合

  * 評価不能なPositionを明示するか、Portfolio計算を失敗させる。
  * 対応方針はBusiness Ruleで確定する。

### Related Business Rules

* TBD

---

## UC-PFL-002 — View Profit and Loss

**Phase:** Phase 4 — Position / Portfolio
**Status:** Needs Review

### Actor

Customer

### Preconditions

* Customerが存在すること。

### Main Flow

1. Positionsと取得情報を取得する。
2. 最新Market Priceを取得する。
3. Average Acquisition Priceを使用してUnrealized P/Lを計算する。
4. 過去の売却ExecutionからRealized P/Lを計算する。
5. 損益情報を返す。

### Postconditions

* Unrealized P/Lを確認できる。
* Realized P/Lを確認できる。
* 計算によって金融取引履歴そのものを変更しない。

### Failure Cases

* 必要な市場価格または取得情報が不足する場合

  * 不正な損益値を生成しない。

### Related Business Rules

* TBD

### Open Questions

* OQ-UC-001

---

# 8. Administration Domain

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

---

# 9. Ledger Domain

初期バージョンでは、Actorが直接開始するLedger固有のUse Caseは定義しない。

LedgerはBankingおよびBrokerageにおける現金移動Use Caseを成立させる内部ドメイン責務として扱う。

例えば以下のUse CaseではLedgerへの記録がPostconditionとなる。

* Deposit Money
* Withdraw Money
* Transfer Between Own Accounts
* Transfer to Another Customer
* Transfer Cash from Bank
* Transfer Cash to Bank

Ledger固有の管理画面や監査履歴閲覧機能についてはFuture Considerationとする。

---

# 10. Planned Future Scope

以下は実装方針が既に決まっているが、現在のUse Caseとしては詳細化しない。

### Phase 6

* Limit Order
* Partial Execution
* 1 Order : N Executions
* 追加のMarket Price生成モデル

これらを実装するPhaseへ移行する際に、新しいUse CaseおよびBusiness Rulesを定義する。

---

# 11. Future Considerations

以下は候補として保持するが、採用自体は未確定であるため現在はUse Case化しない。

* 送金上限
* 送金手数料
* 予約送金
* 外部銀行送金
* Ledger監査閲覧
* ETF
* 投資信託
* 債券
* デリバティブ
* 配当
* 税務処理
* 日次資産推移
* 累積リターン
* Sharpe Ratio
* Maximum Drawdown

採用が決定した時点でRequirements、Glossary、Use Cases、Business Rules等へ昇格させる。

---

# 12. Open Questions

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

---

# 13. Pending References

各Use Caseの `Related Business Rules` は、`business-rules.md` の作成後に正式なBusiness Rule IDへ置き換える。

この作業が完了するまで、Use CaseとBusiness Rule間のTraceabilityは未完成として扱う。
