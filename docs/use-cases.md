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

個別Use Caseの詳細は `use-cases/` 配下のDomain別ドキュメントで管理する。

---

## 2. Status

### Confirmed

Use Caseの目的および主要な振る舞いが確定しており、実装対象として扱える状態。

### Needs Review

Use Caseを実装すること自体は確定しているが、実装前に詳細仕様の追加検討が必要な状態。

PhaseとSpecification Statusは独立している。後期Phaseであることだけを理由にNeeds Reviewとしてはならない。

Needs Reviewには、未解決のOQ IDまたは具体的なReview Reasonを必ず記載する。未決事項がなく実装可能なUse Caseは、Phaseに関係なくConfirmedとする。

---

## 3. Use Case Index

### Customer Domain

詳細：

* [Customer Use Cases](./use-cases/customer.md)

| ID         | Use Case                  | Phase                        | Status    |
| ---------- | ------------------------- | ---------------------------- | --------- |
| UC-CUS-001 | Provision Customer Record | Phase 1 — Core Banking / MVP | Confirmed |
| UC-CUS-002 | View Own Customer Profile | Phase 1 — Core Banking / MVP | Confirmed |

---

### Banking Domain

詳細：

* [Banking Use Cases](./use-cases/banking.md)

| ID         | Use Case                      | Phase                        | Status       |
| ---------- | ----------------------------- | ---------------------------- | ------------ |
| UC-BNK-001 | Open Bank Account             | Phase 1 — Core Banking / MVP | Confirmed    |
| UC-BNK-002 | Deposit Money                 | Phase 1 — Core Banking / MVP | Confirmed    |
| UC-BNK-003 | Withdraw Money                | Phase 1 — Core Banking / MVP | Confirmed    |
| UC-BNK-004 | Transfer Between Own Accounts | Phase 1 — Core Banking / MVP | Confirmed    |
| UC-BNK-005 | Transfer to Another Customer  | Phase 1 — Core Banking / MVP | Confirmed    |
| UC-BNK-006 | View Bank Account             | Phase 1 — Core Banking / MVP | Confirmed    |
| UC-BNK-007 | View Bank Transaction History | Phase 1 — Core Banking / MVP | Confirmed    |
| UC-BNK-008 | Close Bank Account            | Phase 1 — Core Banking / MVP | Confirmed    |

---

### Brokerage Domain

詳細：

* [Brokerage Use Cases](./use-cases/brokerage.md)

| ID         | Use Case                 | Phase                          | Status       |
| ---------- | ------------------------ | ------------------------------ | ------------ |
| UC-BRK-001 | Open Securities Account  | Phase 1 — Core Banking / MVP   | Confirmed    |
| UC-BRK-002 | Transfer Cash from Bank  | Phase 1 — Core Banking / MVP   | Confirmed    |
| UC-BRK-003 | Transfer Cash to Bank    | Phase 1 — Core Banking / MVP   | Confirmed    |
| UC-BRK-004 | Place Buy Order          | Phase 3 — Trading              | Confirmed    |
| UC-BRK-005 | Place Sell Order         | Phase 3 — Trading              | Confirmed    |
| UC-BRK-006 | Cancel Order             | Phase 6 — Advanced Learning    | Needs Review |
| UC-BRK-007 | View Orders              | Phase 3 — Trading              | Confirmed    |
| UC-BRK-008 | View Executions          | Phase 3 — Trading              | Confirmed    |
| UC-BRK-009 | View Positions           | Phase 4 — Position / Portfolio | Confirmed    |
| UC-BRK-010 | View Securities Account  | Phase 1 — Core Banking / MVP   | Confirmed    |
| UC-BRK-011 | Close Securities Account | Phase 1 — Core Banking / MVP   | Confirmed    |

---

### Market Domain

詳細：

* [Market Use Cases](./use-cases/market.md)

| ID         | Use Case                     | Phase            | Status    |
| ---------- | ---------------------------- | ---------------- | --------- |
| UC-MKT-001 | Generate Daily Market Prices | Phase 2 — Market | Confirmed |
| UC-MKT-002 | View Latest Market Prices    | Phase 2 — Market | Confirmed |
| UC-MKT-003 | View Market Price History    | Phase 2 — Market | Confirmed |
| UC-MKT-004 | List Securities              | Phase 2 — Market | Confirmed |

---

### Portfolio Domain

詳細：

* [Portfolio Use Cases](./use-cases/portfolio.md)

| ID         | Use Case                | Phase                          | Status       |
| ---------- | ----------------------- | ------------------------------ | ------------ |
| UC-PFL-001 | View Portfolio Overview | Phase 4 — Position / Portfolio | Confirmed    |
| UC-PFL-002 | View Profit and Loss    | Phase 4 — Position / Portfolio | Confirmed    |

---

### Administration Domain

詳細：

* [Administration Use Cases](./use-cases/administration.md)

| ID         | Use Case                      | Phase                        | Status       |
| ---------- | ----------------------------- | ---------------------------- | ------------ |
| UC-ADM-001 | Freeze Bank Account           | Phase 1 — Core Banking / MVP | Confirmed    |
| UC-ADM-002 | Unfreeze Bank Account         | Phase 1 — Core Banking / MVP | Confirmed    |
| UC-ADM-003 | Restrict Securities Account   | Phase 1 — Core Banking / MVP | Confirmed    |
| UC-ADM-004 | Unrestrict Securities Account | Phase 1 — Core Banking / MVP | Confirmed    |
| UC-ADM-005 | Register Security             | Phase 2 — Market             | Confirmed    |
| UC-ADM-006 | Update Security Parameters    | Phase 2 — Market             | Confirmed    |
| UC-ADM-007 | Disable Security              | Phase 2 — Market             | Confirmed    |
| UC-ADM-008 | Enable Security               | Phase 2 — Market             | Confirmed    |

---

### Ledger Domain

初期バージョンでは、Actorが直接開始するLedger固有のUse Caseは定義しない。

LedgerのUse Case上の位置付けは以下を参照する。

* [Ledger Domain](./use-cases/ledger.md)

---

## 4. Cross-Domain Decision Register

Use Caseに関連して提起された横断的なQuestionと、その現在の決定状態は以下で管理する。

* [Use Case Decision Register](./use-cases/open-questions.md)

Questionは `OQ-UC-XXX` の形式で識別し、解決後も決定の追跡に同じIDを使用する。

---

## 5. Future Scope

将来詳細化する予定のUse Case、および採用自体が未確定の候補は以下で管理する。

* [Future Use Case Scope](./use-cases/future-scope.md)

Future Scopeでは以下を区別する。

* Planned Future Scope：実装方針は決まっているが、現在はUse Caseとして詳細化しないもの
* Future Considerations：候補として保持しているが、採用自体が未確定のもの

---

## 6. Document Structure

Use Case関連ドキュメントは以下の構成で管理する。

```text
docs/
├── use-cases.md
└── use-cases/
    ├── customer.md
    ├── banking.md
    ├── brokerage.md
    ├── market.md
    ├── portfolio.md
    ├── administration.md
    ├── ledger.md
    ├── open-questions.md
    └── future-scope.md
```

`use-cases.md` はUse Case全体の入口およびIndexとして扱う。

個別の業務仕様を確認する場合は、対象Domainのドキュメントのみを参照する。

複数Domainにまたがる未決事項については `open-questions.md` を参照する。

---

## 7. Pending References

各Use Caseの `Related Business Rules` は、`business-rules.md` の作成後に正式なBusiness Rule IDへ置き換える。

この作業が完了するまで、Use CaseとBusiness Rule間のTraceabilityは未完成として扱う。
