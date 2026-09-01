# Market Domain

SecurityのMarket Price生成およびMarket Price参照に関するUse Caseを定義する。

---

## UC-MKT-001 — Generate Daily Market Prices

**Phase:** Phase 2 — Market
**Status:** Needs Review

### Actor

System / Scheduler

### Preconditions

* 対象日がBusiness Dayであること。
* 有効なSecurityが存在すること。
* 価格生成に必要なパラメータが存在すること。

### Main Flow

1. 価格生成対象のSecurityを取得する。
2. 各Securityの前営業日価格を取得する。
3. GBMによって当営業日のClosing Priceを生成する。
4. 生成結果をMarket Priceとして保存する。

### Postconditions

* 各対象Securityについて当営業日のClosing Priceが1件存在する。
* 同一Security・同一Business Dayの価格が重複していない。

### Failure Cases

* 非Business Dayの場合

  * 新しい市場価格を生成しない。
* 同日の価格が既に存在する場合

  * 重複生成しない。
* 必要な価格生成情報が不足している場合

  * 対象Securityの価格生成を失敗として扱う。
  * 不正な価格を保存しない。

### Related Business Rules

* TBD

---

## UC-MKT-002 — View Latest Market Prices

**Phase:** Phase 2 — Market
**Status:** Needs Review

### Actor

Customer

### Preconditions

* 取扱Securityが存在すること。

### Main Flow

1. 対象Securityを取得する。
2. DBに保存された最新Market Priceを取得する。
3. Customerへ返す。

### Postconditions

* 最新の確定済み市場価格を確認できる。
* 新しい価格を生成しない。

### Failure Cases

* 市場価格がまだ存在しない場合

  * 未生成として扱う。
  * 新しい乱数価格をその場で生成しない。

### Related Business Rules

* TBD

---

## UC-MKT-003 — View Market Price History

**Phase:** Phase 2 — Market
**Status:** Needs Review

### Actor

Customer

### Preconditions

* Securityが存在すること。

### Main Flow

1. Securityを指定する。
2. 保存済みMarket Price履歴を取得する。
3. Business Day順に返す。

### Postconditions

* 過去の市場価格履歴を確認できる。
* Market Priceを変更しない。

### Failure Cases

* Securityが存在しない場合

  * 履歴を返さない。

### Related Business Rules

* TBD
