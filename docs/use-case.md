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

Portfolio DomainのUse Caseは以下を参照する。

* [Portfolio Use Cases](./use-cases/portfolio.md)

---

# 8. Administration Domain

Administration DomainのUse Caseは以下を参照する。

* [Administration Use Cases](./use-cases/administration.md)

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
