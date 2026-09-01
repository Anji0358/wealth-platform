# Glossary

## Purpose

本ドキュメントは、本システムで使用するドメイン用語の意味を統一し、仕様書・設計・テスト・Javaコードで共通のUbiquitous Language（ユビキタス言語）を使用することを目的とする。

英語表記を正式なドメイン用語とし、日本語を併記する。

実装上のクラス名・メソッド名などについても、可能な限り本ドキュメントで定義した英語表記と一致させる。

---

# 1. Customer Domain

## Customer（顧客）

### Definition

本金融プラットフォームを利用する個人利用者。

Customerは銀行口座および証券口座を保有し、入出金、送金、証券取引、資産確認などを行う。

### Related Terms

* Bank Account
* Securities Account
* Portfolio

---

## Administrator（管理者）

### Definition

Customer自身には許可されない管理操作を行うシステム運用上のActor。

銀行口座の凍結・凍結解除、証券口座の取引制限・制限解除などを行う。

### Distinction

Administratorは金融資産の所有者ではなく、口座状態などを管理する主体である。

---

## Acting Customer（操作主体Customer）

### Definition

現在のCustomer向けUse Caseを開始したCustomer。初期学習版ではtrusted development-only identityから特定し、Application層ではActorContextを通じて参照する。

### Distinction

path、query、bodyに含まれるCustomer IDは対象を指定する値であり、Acting Customerであることの証明ではない。

---

## ActorContext（操作主体コンテキスト）

### Definition

Application Use CaseへActing Customerなどの操作主体を渡す、transport-agnosticな抽象。

HTTP header名やSpring MVC型をDomain/Applicationへ持ち込まないために使用する。本番Authentication / Authorizationを意味しない。

---

# 2. Banking Domain

## Bank Account（銀行口座）

### Definition

Customerが銀行預金として現金を保有・管理するための口座。

1人のCustomerは複数のBank Accountを保有できる。

Bank AccountはAccount TypeおよびAccount Statusを持つ。

### Related Terms

* Customer
* Account Type
* Account Status
* Current Balance
* Reserved Amount
* Available Balance

---

## Account Type（口座種別）

### Definition

Bank Accountの用途または商品種別を表す分類。

初期バージョンでは、普通預金口座や貯蓄用口座などの区別に使用する。

### Distinction

Account Typeは口座の利用可否を表すAccount Statusとは異なる。

---

## Current Balance（現在残高）

### Definition

口座に現在記録されている現金残高。

未完了の取引によって拘束されている金額も含む。

### Distinction

Current Balanceは、直ちに新しい取引へ使用可能なAvailable Balanceとは異なる。

### Related Terms

* Reserved Amount
* Available Balance
* Ledger

---

## Reserved Amount（拘束額）

### Definition

未完了の送金や証券買い注文などのために確保され、他の新しい取引では利用できない金額。

### Distinction

Reserved Amountは資金が口座から確定的に減少したことを意味しない。

取引が完了した場合にはCurrent Balanceへ反映され、取引がキャンセルまたは失敗した場合には拘束が解除される。

### Related Terms

* Current Balance
* Available Balance

---

## Available Balance（利用可能残高）

### Definition

新しい出金、送金、証券買い注文などに利用できる金額。

初期仕様では次の関係で定義する。

`Available Balance = Current Balance - Reserved Amount`

### Distinction

Current Balance全額が常に利用可能であるとは限らない。

### Related Terms

* Current Balance
* Reserved Amount

---

## Deposit（入金）

### Definition

システム外からBank Accountへ現金を流入させる取引。

Depositによる資金移動はLedgerへ記録する。

### Related Terms

* Bank Account
* Ledger Transaction
* External Account

---

## Withdrawal（出金）

### Definition

Bank Accountからシステム外へ現金を流出させる取引。

Withdrawalによる資金移動はLedgerへ記録する。

### Related Terms

* Bank Account
* Available Balance
* Ledger Transaction
* External Account

---

## Transfer（送金・振替）

### Definition

あるBank Accountから別のBank Accountへ現金を移動する取引。

初期バージョンでは以下を扱う。

* 同一Customerが所有するBank Account間の資金移動
* あるCustomerから別CustomerのBank Accountへの資金移動

### Distinction

DepositやWithdrawalとは異なり、Transferではシステム内部の口座間で資金が移動する。

Bank AccountとSecurities Account間の現金移動はBrokerage側のCash Transferとして扱う。

### Related Terms

* Bank Account
* Ledger Transaction

---

## Account Status（口座状態）

### Definition

口座が現在どのような操作を許可されているかを表す状態。

`Account Status`は口座状態を説明する一般用語であり、Bank AccountとSecurities Accountが同じ状態型または共通Accountモデルを持つことを意味しない。

Bank Account Statusでは初期バージョンとして以下を扱う。

* ACTIVE
* FROZEN
* CLOSED

Securities Account Statusでは以下を扱う。

* ACTIVE
* RESTRICTED
* CLOSED

### Distinction

Account StatusはAccount Typeとは異なり、口座の現在の利用可否や操作制限を表す。

Bank Account StatusとSecurities Account Statusは、それぞれ異なる状態遷移と許可操作を持つ別のDomain概念として扱う。

---

# 3. Brokerage Domain

## Securities Account（証券口座）

### Definition

Customerが証券取引を行うための口座。

証券取引用の現金およびPositionに関連する取引を扱う。

初期バージョンでは1人のCustomerにつき最大1つのSecurities Accountを保有できる。

### Related Terms

* Customer
* Order
* Execution
* Position

---

## Order（注文）

### Definition

Customerが特定のSecurityを購入または売却したいという意思を表す取引要求。

Orderが作成された時点では、必ずしも売買が成立しているとは限らない。

### Distinction

Orderは売買の意思を表し、Executionは実際に売買が成立した結果を表す。

### Related Terms

* Buy Order
* Sell Order
* Execution
* Order Status

---

## Buy Order（買い注文）

### Definition

Securityを購入するためのOrder。

買い注文に必要な現金は、取引完了までReserved Amountとして拘束される場合がある。

### Related Terms

* Order
* Available Balance
* Execution

---

## Sell Order（売り注文）

### Definition

保有しているSecurityを売却するためのOrder。

Customerは利用可能な保有数量を超えてSell Orderを作成できない。

### Related Terms

* Order
* Position
* Execution

---

## Execution（約定）

### Definition

Orderに基づく証券売買が実際に成立した結果。

Executionには成立した数量および価格が記録される。

初期バージョンでは1つのOrderを1回のExecutionによって全量約定する。

### Distinction

Orderは売買の意思であり、Executionは成立した取引事実である。

将来的には1つのOrderから複数のExecutionが発生する部分約定への拡張を想定する。

### Related Terms

* Order
* Position
* Market Price

---

## Position（保有ポジション）

### Definition

Securities Accountが現在保有しているSecurityの数量および取得情報を表す。

証券の保有数量はPositionとして管理し、Cash Ledgerでは管理しない。

### Distinction

Positionは個別Securityの保有状態を表す。

複数のPositionや銀行預金などを統合して評価する概念がPortfolioである。

### Related Terms

* Security
* Execution
* Portfolio
* Average Acquisition Price

---

## Order Status（注文状態）

### Definition

Orderが現在どの処理段階にあるかを表す状態。

初期バージョンでは、注文の受付、約定、キャンセルなどを区別するために使用する。

### Distinction

Order StatusはSecurities Account自体の利用可否を表すAccount Statusとは異なる。

---

# 4. Ledger Domain

## Ledger（台帳）

### Definition

本システム内で発生した現金移動の事実を共通形式で記録する仕組み。

BankingとBrokerageの双方で発生する現金移動を記録する。

### Distinction

Ledgerは現在残高そのものではなく、資金がどのように移動したかを追跡するための記録である。

証券の保有数量はLedgerではなくPositionで管理する。

### Related Terms

* Ledger Transaction
* Ledger Entry
* Current Balance

---

## Ledger Transaction（台帳取引）

### Definition

1回の資金移動全体を表すLedger上の取引単位。

1つのLedger Transactionは複数のLedger Entryから構成される。

### Distinction

Ledger Transactionは取引全体を表し、Ledger Entryはその取引に含まれる個別の資金増減を表す。

### Related Terms

* Ledger
* Ledger Entry

---

## Ledger Entry（台帳明細）

### Definition

Ledger Transactionに含まれる、特定の口座または勘定に対する個別の資金増減記録。

1つのLedger Transactionに含まれるLedger Entryの金額合計は0でなければならない。

`Σ LedgerEntry.amount = 0`

### Distinction

Ledger Entry単体では資金移動全体を表さない。

複数のLedger Entryの集合によって1つのLedger Transactionを構成する。

### Related Terms

* Ledger Transaction
* External Account

---

## Reversal Transaction（取消取引・逆取引）

### Definition

過去の確定済みLedger Transactionを直接変更または削除せず、その効果を打ち消すために追加する新しいLedger Transaction。

### Distinction

Reversal Transactionは過去の記録を書き換えるものではない。

元のLedger Transactionを保持したまま、逆方向のLedger Entryを新たに記録する。

### Related Terms

* Ledger Transaction
* Ledger Entry

---

## External Account（外部勘定）

### Definition

システム外からの入金、またはシステム外への出金をLedger上で表現するための論理的な相手勘定。

### Distinction

External Accountは実際の外部銀行口座や外部金融システムとの接続を意味しない。

初期バージョンでは、システム境界外との資金流入・流出をLedger上で整合的に表現するために使用する。

---

# 5. Market Domain

## Security（証券）

### Definition

本システム上で売買対象となる金融商品。

初期バージョンでは株式を対象とする。

各Securityには市場価格生成に必要なパラメータを設定する。

Securityは`ACTIVE`または`DISABLED`のStatusを持つ。`DISABLED`でもSecurityは存在し、個別参照、価格履歴、Portfolio表示、既存保有のSellを継続できる。禁止されるのは新規Buyである。

### Related Terms

* Market Price
* Position
* Order

---

## Market Price（市場価格）

### Definition

特定のSecurityについてMarket Domainが管理する市場上の価格。

証券取引およびPortfolio評価では、DBに保存されている最新のMarket Priceを使用する。

### Distinction

Market Priceを参照するたびに価格を新しく生成することはしない。

価格生成処理によって確定した値を保存し、それを参照する。

### Related Terms

* Security
* Closing Price
* Execution
* Valuation

---

## Business Day（営業日）

### Definition

市場価格生成および証券取引上の営業対象日。

初期バージョンでは月曜日から金曜日をBusiness Dayとし、土曜日と日曜日を非営業日とする。

日本の祝日は初期バージョンでは考慮しない。

### Related Terms

* Closing Price
* Market Price

---

## Closing Price（終値）

### Definition

あるSecurityについて、1営業日の終了時点の価格として確定・保存されるMarket Price。

初期バージョンでは1つのSecurityにつき1営業日1件のみ保持する。

### Related Terms

* Market Price
* Business Day

---

## Expected Return（期待収益率）

### Definition

GBMによる市場価格生成で使用する、Securityの価格変動傾向を表すパラメータ。

記号 `μ` を使用する。

### Distinction

Expected Returnは確定した将来収益を意味するものではなく、仮想市場価格生成モデルのパラメータである。

### Related Terms

* Volatility
* Market Price

---

## Volatility（ボラティリティ）

### Definition

GBMによる市場価格生成で使用する、Securityの価格変動の大きさを表すパラメータ。

記号 `σ` を使用する。

### Distinction

Volatilityが大きいほど、生成される価格の変動幅が大きくなる。

### Related Terms

* Expected Return
* Market Price

---

# 6. Portfolio Domain

## Portfolio（ポートフォリオ）

### Definition

Customerが保有する銀行預金、証券口座現金、およびPositionなどを総合して捉えた資産全体。

Portfolio Domainでは、複数の業務領域の情報を用いて資産評価および損益を計算する。

### Distinction

Positionは個別Securityの保有状態を表すのに対し、PortfolioはCustomerの資産全体を表す。

### Related Terms

* Position
* Valuation
* Total Assets

---

## Average Acquisition Price（平均取得単価）

### Definition

現在保有しているSecurityについて、取得に要した価格を保有数量に基づいて平均した単価。

Positionの評価および損益計算に使用する。

### Related Terms

* Position
* Unrealized P/L
* Realized P/L

---

## Valuation（評価額）

### Definition

保有しているSecurityを現在のMarket Priceで評価した金額。

基本的には、保有数量と現在市場価格を基に算出する。

### Related Terms

* Position
* Market Price
* Unrealized P/L

---

## Unrealized P/L（含み損益）

### Definition

現在保有しているSecurityについて、現在のValuationと取得原価との差から算出される、まだ売却によって確定していない損益。

### Distinction

Securityを売却して確定した損益であるRealized P/Lとは異なる。

### Related Terms

* Average Acquisition Price
* Valuation
* Realized P/L

---

## Realized P/L（実現損益）

### Definition

Securityの売却によって確定した利益または損失。

### Distinction

現在保有しているSecurityの価格変動による未確定損益であるUnrealized P/Lとは異なる。

### Related Terms

* Execution
* Average Acquisition Price
* Unrealized P/L

---

## Total Assets（総資産）

### Definition

Customerが本システム内で保有する資産を合計した評価額。

初期バージョンでは主に以下を含む。

* Bank Accountの現金
* Securities Accountの現金
* Positionの現在評価額

### Related Terms

* Portfolio
* Valuation

---

# 7. Cross-Domain Concepts

## Cash（現金）

### Definition

Banking、Brokerage、Ledger、Portfolioの複数領域で扱われる金銭的資産。

初期バージョンでは日本円のみを扱う。

証券そのものの数量とは区別する。

### Related Terms

* Amount
* Current Balance
* Ledger

---

## Amount（金額）

### Definition

現金取引における金銭量を表す値。

入金、出金、送金、Ledger Entryなどで使用する。

金額の精度、DB型、Java上の表現方法などの実装詳細は後続の設計で決定する。

### Distinction

Amountはドメイン上の金額という概念であり、Javaの具体的な数値型やDBのデータ型そのものを意味しない。
