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

Ledger DomainのUse Case上の位置付けは以下を参照する。

* [Ledger Domain](./use-cases/ledger.md)

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

Use Caseに関連する未決事項は以下を参照する。

* [Use Case Open Questions](./use-cases/open-questions.md)

---

# 13. Pending References

各Use Caseの `Related Business Rules` は、`business-rules.md` の作成後に正式なBusiness Rule IDへ置き換える。

この作業が完了するまで、Use CaseとBusiness Rule間のTraceabilityは未完成として扱う。
