# Requirements

## 1. System Purpose

本システムは、個人顧客が銀行口座と証券口座を一元的に利用し、入出金、送金、証券取引および資産管理を行える銀行・証券統合型金融プラットフォームを構築することを目的とする。

金融取引における資金の整合性、取引履歴の追跡可能性、および適切な状態管理を重視する。

また、本プロジェクトを通じて、金融ドメインを題材に以下を実践的に学習する。

* TDD（Test-Driven Development）
* オブジェクト指向設計
* DDD（Domain-Driven Design）
* デザインパターン
* 中級以上のSQL
* データベース設計
* トランザクション
* 同時実行制御

---

## 2. Actors

### 2.1 Customer

個人顧客。

主に以下の操作を行う。

* 銀行口座の利用
* 入出金
* 送金
* 証券口座の利用
* 証券取引
* 保有資産・損益・総資産の確認

### 2.2 Administrator

システム管理者。

主に以下の管理操作を行う。

* 銀行口座の状態管理
* 証券口座の状態管理
* 取扱銘柄の管理
* その他、顧客自身には許可されない管理操作

### 2.3 System / Scheduler

ユーザー操作とは独立して自動処理を実行する主体。

主に以下を担当する。

* 営業日ごとの市場価格生成
* 市場価格履歴の登録

---

## 3. System Scope

本システムでは、以下の6つの業務領域を扱う。

### Customer

顧客に関する情報を扱う。

### Banking

銀行口座、入出金、口座間振替、他顧客への送金などの銀行業務を扱う。

### Brokerage

証券口座、注文、約定、保有証券などの証券取引を扱う。

### Ledger

銀行および証券に関係する現金移動を共通の台帳として記録する。

### Market

取扱証券および市場価格を扱い、数理モデルに基づいて仮想市場価格を生成する。

### Portfolio

銀行預金、証券口座現金、保有証券、市場価格などを基に、資産評価および損益を扱う。

これらは現時点では業務領域を表すものであり、DDDにおけるBounded ContextやJavaパッケージなどの実装上の境界は、後続の設計工程で決定する。

---

## 4. Banking Requirements

### 4.1 Bank Accounts

Customerは複数の銀行口座を保有できる。

銀行口座には口座種別を持たせる。

初期バージョンでは、普通預金や貯蓄用口座などの区別を扱うが、金利計算や定期預金などは扱わない。

銀行口座は以下の状態を持つ。

* ACTIVE
* FROZEN
* CLOSED

### 4.2 Banking Operations

Customerは以下の操作を行える。

* 銀行口座を開設する
* 銀行口座へ入金する
* 銀行口座から出金する
* 自身が所有する銀行口座間で資金を振り替える
* 他のCustomerが所有する銀行口座へ送金する
* Current Balanceを確認する
* Reserved Amountを確認する
* Available Balanceを確認する
* 取引履歴を確認する
* 口座状態を確認する
* 銀行口座を閉鎖する

Administratorは以下を行える。

* ACTIVE口座をFROZENに変更する
* FROZEN口座をACTIVEに戻す

### 4.3 Balance

銀行口座では以下を区別する。

* Current Balance
* Reserved Amount
* Available Balance

利用可能残高は以下として扱う。

`Available Balance = Current Balance - Reserved Amount`

未完了の取引で使用予定の資金はReserved Amountとして拘束する。

### 4.4 Future Considerations

初期バージョンでは実装せず、将来的な追加候補とする。

* 1回あたりの送金上限
* 1日あたりの送金上限
* 送金手数料
* 予約送金
* 外部銀行への送金

---

## 5. Brokerage Requirements

### 5.1 Securities Account

Customerは初期バージョンでは最大1つの証券口座を保有できる。

証券口座は以下の状態を持つ。

* ACTIVE
* RESTRICTED
* CLOSED

### 5.2 Cash Transfer

Customerは以下の資金移動を行える。

* 自身の銀行口座から証券口座へ資金を移動する
* 証券口座から自身の銀行口座へ資金を戻す

これらの現金移動はLedgerへ記録する。

### 5.3 Orders

Customerは以下の注文を行える。

* 買い注文
* 売り注文
* 未約定注文のキャンセル
* 注文状態の確認
* 注文履歴の確認

初期バージョンでは成行注文を扱う。

### 5.4 Order and Execution

OrderとExecutionは別の概念として扱う。

Orderは証券を購入または売却する意思を表す。

ExecutionはOrderが実際に成立した結果を表す。

初期バージョンでは、

`1 Order : 1 Execution`

とし、注文数量の全量を1回で約定する。

設計上は将来的に、

`1 Order : N Executions`

へ拡張できることを考慮する。

### 5.5 Position

証券の保有数量はPositionとして管理する。

株式数量そのものはCash Ledgerでは管理しない。

### 5.6 Account Administration

Administratorは以下を行える。

* ACTIVE証券口座をRESTRICTEDに変更する
* RESTRICTED証券口座をACTIVEへ戻す

### 5.7 Future Considerations

* 指値注文
* 部分約定
* 複数Execution
* 注文有効期限
* 取引手数料

---

## 6. Portfolio Requirements

Customerは以下を確認できる。

* 銀行預金残高
* 証券口座現金残高
* 保有銘柄
* 銘柄ごとの保有数量
* 平均取得単価
* 現在市場価格
* 銘柄別評価額
* 銘柄別含み損益
* 実現損益
* 総資産

Portfolioでは、Brokerageが管理するPosition、Marketが管理する市場価格、Bankingが管理する銀行資産などを利用して資産評価を行う。

### Future Considerations

* 日次総資産推移
* 日次リターン
* 累積リターン
* 資産構成比
* ボラティリティ
* Sharpe Ratio
* 最大ドローダウン

---

## 7. Market Requirements

### 7.1 Securities

取扱証券ごとに市場価格生成に必要なパラメータを設定する。

初期バージョンでは以下を使用する。

* 初期価格
* 期待収益率 μ
* ボラティリティ σ

### 7.2 Price Generation

市場価格は外部株価APIから取得せず、本システム内で生成する。

初期バージョンではGBM（Geometric Brownian Motion / 幾何ブラウン運動）を使用する。

System / Schedulerは営業日ごとに各銘柄の終値を1回生成する。

生成された価格は市場価格履歴としてDBへ保存する。

### 7.3 Business Day

初期バージョンでは、

* 月曜日〜金曜日：営業日
* 土曜日・日曜日：非営業日

とする。

土日には新しい市場価格を生成しない。

### 7.4 Market Price Integrity

同一銘柄・同一営業日について、複数の終値を保持してはならない。

証券取引およびPortfolioの資産評価には、DBへ保存された最新市場価格を利用する。

価格参照のたびに新しい乱数によって価格を生成してはならない。

### 7.5 Testability

市場価格生成で使用する乱数は、テスト時に制御できること。

同一条件から再現可能なテストを実行できる設計とする。

### 7.6 Future Considerations

* 日本の祝日を考慮した市場カレンダー
* Open / High / Low / Close
* 出来高
* 銘柄間相関
* 市場全体に影響するショック
* 平均回帰モデル
* 確率的ボラティリティモデル

---

## 8. Ledger Requirements

### 8.1 Shared Cash Ledger

銀行および証券に関するすべての現金移動を共通Ledgerへ記録する。

対象には以下を含む。

* 入金
* 出金
* 銀行口座間振替
* 他顧客への送金
* 銀行口座から証券口座への資金移動
* 証券口座から銀行口座への資金移動

### 8.2 Ledger Transaction

1回の資金移動を1つのLedger Transactionとして扱う。

1つのLedger Transactionは複数のLedger Entryを持つ。

### 8.3 Ledger Invariant

1つのLedger Transactionに含まれるLedger Entryの金額合計は必ず0でなければならない。

`Σ LedgerEntry.amount = 0`

これにより、資金がシステム内部で理由なく生成または消失しないことを保証する。

### 8.4 External Cash Flow

システム外からの入金・システム外への出金については、論理的な外部勘定を相手側として扱う。

これは実際の外部銀行システムを再現するものではない。

### 8.5 Immutability and Correction

確定したLedger TransactionおよびLedger Entryは原則として直接変更または削除しない。

誤った取引を訂正する場合は、元の記録を変更するのではなく、取消取引または訂正取引を新たに記録する。

### 8.6 Position Separation

株式等の証券保有数量はLedgerでは管理せず、Brokerage領域のPositionとして管理する。

### 8.7 Future Considerations

* 手数料勘定
* 配当
* 税金
* 利息
* より完全な複式簿記モデル

---

## 9. Error and Consistency Requirements

初期バージョンでは以下の異常系を扱う。

### Validation Error

入力値そのものが不正な場合。

例：

* 0以下の入金額
* 0以下の注文数量

### Business Rule Violation

業務上許可されない操作。

例：

* Available Balanceを超える出金
* Available Balanceを超える買い注文
* 保有数量を超える売却

### Invalid State Transition

現在状態から許可されない状態変更または操作。

例：

* FILLED注文のキャンセル
* CLOSED口座からの送金

### Duplicate Request

同一の金融取引要求が複数回送信された場合でも、資金移動を重複して実行しないこと。

Idempotencyを考慮する。

### Concurrency Conflict

同一口座に対する同時出金や同時注文でも、残高やReserved Amountの不変条件を破壊しないこと。

### Transaction Failure

複数の更新から構成される金融取引では、途中で処理に失敗した場合に一部の更新のみが確定してはならない。

処理全体が成功するか、処理全体がRollbackされること。

---

## 10. Out of Scope

初期バージョンでは以下を対象外とする。

### External Integration

* 実銀行ネットワークとの接続
* 実証券取引所との接続
* 外部株価API
* 外部決済サービス

### Compliance

* KYC
* AML
* 本人確認書類管理
* 実際の金融法令・規制対応

### Banking Extensions

* 外部銀行への送金
* 予約送金
* 送金手数料
* 金利計算
* 定期預金

### Trading Extensions

* 指値注文
* 部分約定
* 信用取引
* 空売り
* ETF
* 投資信託
* 債券
* デリバティブ

### Accounting / Tax

* 税金計算
* 配当課税
* 確定申告機能
* 本格的な会計システム

### Currency

* 外貨
* FX
* 為替変換

### Security

* 本番レベルの認証・認可
* MFA
* 本番レベルの不正アクセス検知

初期アーキテクチャはモノリスとする。

詳細な技術構成についてはArchitectureおよびDevelopment Environmentの設計で定義する。

---

## 11. Initial Release / MVP

最初の完成ラインでは以下を実装する。

### Phase 1 — Core Banking / MVP

* Customer
* BankAccount
* Deposit
* Withdrawal
* 自身の銀行口座間振替
* 他顧客への送金
* Current / Reserved / Available Balance
* Ledger
* SecuritiesAccount
* Bank → Securities Accountの資金移動
* Securities Account → Bankの資金移動

MVPでは株式売買までは実装しない。

---

## 12. Development Phases

### Phase 1 — Core Banking / MVP

銀行・Ledger・証券口座への資金移動を完成させる。

### Phase 2 — Market

* Security
* GBMによる市場価格生成
* MarketPrice履歴

### Phase 3 — Trading

* Buy Order
* Sell Order
* Order State
* Order Cancellation
* Execution

### Phase 4 — Position / Portfolio

* Position
* 平均取得単価
* 含み損益
* 実現損益
* 総資産

### Phase 5 — Robustness

* Idempotency
* Concurrency
* Locking
* Transaction Failure Test
* Performance Verification

### Phase 6 — Advanced Learning

* 高度なSQL
* Index設計・チューニング
* EXPLAIN ANALYZE
* 市場価格生成モデルの追加
* 部分約定
* 指値注文
