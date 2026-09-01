# Future Use Case Scope

将来のUse Case候補および今後詳細化する予定の機能を管理する。

現在のUse Caseとして定義済みの機能とは区別し、実装方針が決まっているものと、採用自体が未確定なものを分けて管理する。

---

## Planned Future Scope

以下は実装方針が既に決まっているが、現在のUse Caseとしては詳細化しない。

### Phase 6

* Limit Order
* Cancel Order（UC-BRK-006として概要定義済み、OQ-UC-003の解決後に確定する）
* Partial Execution
* 1 Order : N Executions
* 追加のMarket Price生成モデル

これらを実装するPhaseへ移行する際に、未定義のUse CaseとBusiness Rulesを追加し、UC-BRK-006を含むNeeds Review仕様を確定する。

---

## Future Considerations

以下は候補として保持するが、採用自体は未確定であるため現在はUse Case化しない。

### Banking

* 送金上限
* 送金手数料
* 予約送金
* 外部銀行送金
* Ledger監査閲覧

### Securities / Investment Products

* ETF
* 投資信託
* 債券
* デリバティブ
* 配当
* 税務処理

### Portfolio / Analytics

* 日次資産推移
* 累積リターン
* Sharpe Ratio
* Maximum Drawdown

採用が決定した時点でRequirements、Glossary、Use Cases、Business Rules等へ昇格させる。
