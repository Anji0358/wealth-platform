# Use Case Decision Register

Use Caseに関連して提起された横断的なQuestionと、その現在の決定状態を記録する。

詳細なルールは `business-rules/open-questions.md` および各DomainのBusiness Rulesを正とする。

---

## OQ-UC-001 — Acquisition Cost and Realized P/L Method

**Status:** Resolved

**Related Use Cases:** UC-BRK-005, UC-PFL-002

**Decision:** Phase 4ではAverage Costを使用する。Average Acquisition Priceは小数を許容し、Remaining Acquisition Costを保持する。FIFOはFuture Considerationとする。

---

## OQ-UC-002 — Reservation of Securities for Sell Orders

**Status:** Resolved / Refined

**Related Use Cases:** UC-BRK-005, UC-BRK-006

**Decision:** Phase 3の即時全量約定ではReserved Quantityを使用しない。Phase 6で長期注文を導入するときにReserved Quantity / Available Quantityを導入する。

---

## OQ-UC-003 — Separation of Execution Processing

**Status:** Deferred to Phase 6

**Related Use Cases:** UC-BRK-004, UC-BRK-005

**Current Decision:** Phase 3ではOrder作成と1回の全量Executionを同じUse Caseで実行する。独立したExecution処理はLimit OrderまたはPartial Executionの導入時に再検討する。

---

## OQ-UC-004 — Bank Account Closure Conditions

**Status:** Resolved

**Related Use Case:** UC-BNK-008

**Decision:** `ACTIVE`かつCurrent BalanceとReserved Amountがともに0で、未完了の資金移動がない場合だけ閉鎖できる。`FROZEN -> CLOSED`は許可しない。

---

## OQ-UC-005 — Securities Account Closure Conditions

**Status:** Resolved

**Related Use Case:** UC-BRK-011

**Decision:** `ACTIVE`かつCurrent BalanceとReserved Amountがともに0で、Positionおよび未完了Orderがない場合だけ閉鎖できる。`RESTRICTED -> CLOSED`は許可しない。

---

## OQ-UC-006 — Effect of Disabling a Security

**Status:** Resolved

**Related Use Cases:** UC-ADM-007, UC-ADM-008, UC-BRK-004, UC-BRK-005, UC-BRK-009

**Decision:** `DISABLED`では新規Buy Orderを禁止し、既存Positionを減らすSell Orderは許可する。価格生成と履歴保持は継続し、Administratorは再度Enableできる。

---

## OQ-UC-007 — Operations Allowed by Account Status

**Status:** Resolved

**Related Use Cases:** Banking、Brokerage Cash TransferおよびAccount Administrationの各Use Case

**Decision:** Bank `FROZEN`は入金・受取・参照を許可するが、送金、出金、証券口座への入金、閉鎖を禁止する。Securities `RESTRICTED`は参照と適格なBank Accountへの資金移動を許可するが、資金受入、新規注文、閉鎖を禁止する。`CLOSED`は履歴参照だけを許可する。

---

## OQ-UC-008 — Reservation Lifecycle for Cash Transfers

**Status:** Resolved / Refined

**Related Use Cases:** UC-BNK-004, UC-BNK-005, UC-BRK-002, UC-BRK-003

**Decision:** Phase 1の同期的資金移動およびPhase 3の即時Market OrderではReserved Amountを使用しない。Reservationは処理が意味のある期間Pendingとなる後続Phaseで導入する。

---

## DR-001 — Customer Security Reads

**Status:** Decided

**Decision:** `GET /securities`はCustomer向けUC-MKT-004とする。デフォルトはACTIVE、明示的status filterでDISABLEDも取得できる。DISABLEDの個別参照を許可し、Securityの存在とBuy可能性を分離する。

---

## DR-002 — Customer Record Provisioning

**Status:** Decided

**Decision:** Customer自己登録は現在スコープ外とし、Customer recordはAdministratorまたはSystemが作成する。CustomerにはOwn Profile参照を提供する。

---

## DR-003 — Development Acting Identity and Ownership

**Status:** Decided

**Decision:** 初期学習版は`X-Acting-Customer-Id`等のtrusted development-only identityをPresentationでActorContextへ変換する。Application層がOwnershipを検証し、path/bodyのCustomer IDを本人性の根拠にしない。本番Authentication / AuthorizationはOut of Scopeとする。詳細理由はADR-003に記録する。

---

## DR-004 — Disabled Security Capabilities

**Status:** Decided

**Decision:** DISABLED Securityで禁止するのは新規BUYだけとする。既存保有SELL、Portfolio、取引履歴、価格履歴、個別参照を継続し、Security responseはstatusを含む。

---

## DR-005 — Phase and Specification Status

**Status:** Decided

**Decision:** PhaseとSpecification Statusを分離する。Needs Reviewにはunresolved OQ IDまたは具体的Review Reasonを必須とし、未決事項がなく実装可能ならPhaseに関係なくConfirmedとする。
