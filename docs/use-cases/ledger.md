# Ledger Domain

Ledgerに関するUse Case上の位置付けを定義する。

## Use Case Policy

初期バージョンでは、Actorが直接開始するLedger固有のUse Caseは定義しない。

LedgerはBankingおよびBrokerageにおける現金移動Use Caseを成立させる内部ドメイン責務として扱う。

例えば以下のUse Caseでは、Ledgerへの記録がPostconditionまたは処理の一部となる。

* UC-BNK-002 — Deposit Money
* UC-BNK-003 — Withdraw Money
* UC-BNK-004 — Transfer Between Own Accounts
* UC-BNK-005 — Transfer to Another Customer
* UC-BRK-002 — Transfer Cash from Bank
* UC-BRK-003 — Transfer Cash to Bank

Ledger固有の管理画面や監査履歴閲覧機能についてはFuture Considerationとする。
