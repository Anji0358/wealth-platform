# Portfolio Domain

Customerが保有する金融資産全体の評価および損益確認に関するUse Caseを定義する。

---

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
