# Market Domain

SecurityのMarket Price生成およびMarket Price参照に関するUse Caseを定義する。

---

## UC-MKT-001 — Generate Daily Market Prices

**Phase:** Phase 2 — Market
**Status:** Confirmed

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

* BR-MKT-005 — One Market Price per Security per Business Day
* BR-MKT-006 — Daily Price Generation Is Idempotent
* BR-MKT-008 — Disabled Securities Continue Market Price Generation
* BR-MKT-009 — Business Day Is Monday Through Friday Initially
* BR-MKT-010 — GBM Calculation and Official Market Price Are Separate Concepts

---

## UC-MKT-002 — View Latest Market Prices

**Phase:** Phase 2 — Market
**Status:** Confirmed

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

* BR-MKT-007 — Market Price History Is Immutable
* BR-MKT-011 — Market Reads Never Generate New Random Prices

---

## UC-MKT-003 — View Market Price History

**Phase:** Phase 2 — Market
**Status:** Confirmed

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

* BR-MKT-005 — One Market Price per Security per Business Day
* BR-MKT-007 — Market Price History Is Immutable
* BR-MKT-011 — Market Reads Never Generate New Random Prices

---

## UC-MKT-004 — List Securities

**Phase:** Phase 2 — Market
**Status:** Confirmed

### Actor

Customer

### Preconditions

* なし。

### Main Flow

1. Customerが任意でSecurity Status filterを指定する。
2. filter未指定の場合はACTIVE Securityを取得する。
3. `ACTIVE`または`DISABLED`が指定された場合は、そのStatusのSecurityを取得する。
4. Security ID、Security Code、name、statusを含む一覧を返す。

### Alternative Flow — View One Security

1. CustomerがSecurity IDを指定する。
2. Statusにかかわらず、存在するSecurityを取得する。
3. Security ID、Security Code、name、statusを返す。

### Postconditions

* Securityの存在とStatusを確認できる。
* 一覧への掲載自体を新規BUY可能性の保証として扱わない。
* DISABLED Securityも個別参照できる。
* システム状態を変更しない。

### Failure Cases

* 未知のStatus filterが指定された場合

  * 入力エラーとして拒否する。

### Related Business Rules

* BR-MKT-003 — Security Is Never Physically Deleted in Normal Operation
* BR-MKT-012 — Security Existence and Buy Eligibility Are Separate
* BR-MKT-013 — Security Listing and Direct Read Include Status
