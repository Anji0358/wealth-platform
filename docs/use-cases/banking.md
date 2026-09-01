# Banking Domain

Bank AccountおよびBankingに関するUse Caseを定義する。

---

## UC-BNK-001 — Open Bank Account

**Phase:** Phase 1 — Core Banking / MVP
**Status:** Confirmed

### Actor

Customer

### Preconditions

* Customerが存在すること。

### Main Flow

1. CustomerがAccount Typeを指定する。
2. システムが口座開設条件を検証する。
3. Bank Accountを作成する。
4. Bank AccountをCustomerに関連付ける。
5. 初期Account Statusを設定する。

### Postconditions

* Customerが新しいBank Accountを保有する。
* 新しいBank Accountの初期残高は0である。
* Current Balance、Reserved Amount、Available Balanceの整合性が成立する。

### Failure Cases

* 指定されたAccount Typeが不正な場合

  * 開設を拒否する。
  * Bank Accountを作成しない。

### Related Business Rules

* TBD

---

## UC-BNK-002 — Deposit Money

**Phase:** Phase 1 — Core Banking / MVP
**Status:** Confirmed

### Actor

Customer

### Preconditions

* Customerが存在すること。
* 対象Bank Accountが存在すること。

### Main Flow

1. CustomerがBank Accountと入金額を指定する。
2. システムが入金可能か検証する。
3. External AccountからBank Accountへの資金移動を成立させる。
4. Current Balanceを更新する。
5. Ledger TransactionおよびLedger Entriesを記録する。

### Postconditions

* Current Balanceが入金額分増加している。
* Ledgerに資金移動が記録されている。
* Ledger Entryの金額合計が0である。

### Failure Cases

* 入金額が不正な場合

  * 入金を拒否する。
  * Current Balanceを変更しない。
  * Ledgerを変更しない。
* Account Statusによって入金が許可されない場合

  * 入金を拒否する。
  * 残高およびLedgerを変更しない。

### Related Business Rules

* TBD

### Open Questions

* OQ-UC-007

---

## UC-BNK-003 — Withdraw Money

**Phase:** Phase 1 — Core Banking / MVP
**Status:** Confirmed

### Actor

Customer

### Preconditions

* Customerが存在すること。
* 対象Bank Accountが存在すること。

### Main Flow

1. Customerが出金額を指定する。
2. システムが出金可能性を検証する。
3. Bank AccountからExternal Accountへの資金移動を成立させる。
4. Current Balanceを更新する。
5. Ledgerへ記録する。

### Postconditions

* Current Balanceが出金額分減少している。
* Available Balanceが整合している。
* Ledger Transactionが記録されている。
* Ledger Entryの金額合計が0である。

### Failure Cases

* 出金額が不正な場合

  * 出金を拒否する。
  * 残高およびLedgerを変更しない。
* Available Balanceが不足している場合

  * 出金を拒否する。
  * 残高およびLedgerを変更しない。
* Account Statusによって出金が許可されない場合

  * 出金を拒否する。
  * 残高およびLedgerを変更しない。

### Related Business Rules

* TBD

### Open Questions

* OQ-UC-007

---

## UC-BNK-004 — Transfer Between Own Accounts

**Phase:** Phase 1 — Core Banking / MVP
**Status:** Confirmed

### Actor

Customer

### Preconditions

* Customerが存在すること。
* 送金元と送金先のBank Accountが存在すること。
* 両Bank Accountが同一Customerに属すること。

### Main Flow

1. Customerが送金元口座、送金先口座、金額を指定する。
2. システムが振替可能性を検証する。
3. 必要に応じて資金をReserved Amountとして拘束する。
4. 送金元Current Balanceを減少させる。
5. 送金先Current Balanceを増加させる。
6. 資金拘束を解放する。
7. Ledger Transactionを記録する。

### Postconditions

* 送金元と送金先の残高が正しく更新されている。
* Reserved Amountが整合している。
* Ledger Entryの金額合計が0である。
* 資金が途中で消失または重複していない。

### Failure Cases

* 不正な金額の場合

  * 振替を拒否する。
  * 両口座およびLedgerを変更しない。
* Available Balanceが不足している場合

  * 振替を拒否する。
  * 両口座およびLedgerを変更しない。
* Account Statusによって振替できない場合

  * 振替を拒否する。
  * 両口座およびLedgerを変更しない。
* 処理途中で失敗した場合

  * 資金移動全体をRollbackする。
  * 一方の口座だけが変更された状態を残さない。

### Related Business Rules

* TBD

### Open Questions

* OQ-UC-007
* OQ-UC-008

---

## UC-BNK-005 — Transfer to Another Customer

**Phase:** Phase 1 — Core Banking / MVP
**Status:** Confirmed

### Actor

Customer

### Preconditions

* 送金元Customerが存在すること。
* 送金元Bank Accountが存在すること。
* 送金先Bank Accountが存在すること。

### Main Flow

1. Customerが送金元、送金先、金額を指定する。
2. システムが送金元口座の所有者を確認する。
3. 送金可能性を検証する。
4. 必要に応じて資金を拘束する。
5. 送金元Current Balanceを減少させる。
6. 送金先Current Balanceを増加させる。
7. Ledgerへ資金移動を記録する。

### Postconditions

* 送金元と送金先の残高が正しく更新されている。
* Ledger Entryの金額合計が0である。
* 同一取引が重複実行されていない。

### Failure Cases

* Customerが所有していない口座を送金元に指定した場合

  * 送金を拒否する。
  * 状態を変更しない。
* Available Balanceが不足している場合

  * 送金を拒否する。
  * 状態を変更しない。
* Account Statusによって送金できない場合

  * 送金を拒否する。
  * 状態を変更しない。
* 同一取引要求が重複した場合

  * 資金移動を重複実行しない。
* 処理途中で失敗した場合

  * 全体をRollbackする。

### Related Business Rules

* TBD

### Open Questions

* OQ-UC-007
* OQ-UC-008

---

## UC-BNK-006 — View Bank Account

**Phase:** Phase 1 — Core Banking / MVP
**Status:** Confirmed

### Actor

Customer

### Preconditions

* Customerが存在すること。
* Bank Accountが存在すること。

### Main Flow

1. CustomerがBank Accountを指定する。
2. システムがCustomerの所有口座であることを確認する。
3. 以下を取得する。

   * Account Type
   * Account Status
   * Current Balance
   * Reserved Amount
   * Available Balance
4. Bank Account情報を返す。

### Postconditions

* Bank Accountの現在状態を確認できる。
* システム状態を変更しない。

### Failure Cases

* 他CustomerのBank Accountの場合

  * 情報を返さない。
  * 状態を変更しない。

### Related Business Rules

* TBD

---

## UC-BNK-007 — View Bank Transaction History

**Phase:** Phase 1 — Core Banking / MVP
**Status:** Confirmed

### Actor

Customer

### Preconditions

* 対象Bank Accountが存在すること。

### Main Flow

1. CustomerがBank Accountを指定する。
2. 所有権を確認する。
3. 対象口座に関係する取引履歴を取得する。
4. 履歴を時系列で返す。

### Postconditions

* Customerが口座に関係する過去の資金移動を確認できる。
* Ledgerおよび残高を変更しない。

### Failure Cases

* 対象口座をCustomerが所有していない場合

  * 履歴を返さない。

### Related Business Rules

* TBD

---

## UC-BNK-008 — Close Bank Account

**Phase:** Phase 1 — Core Banking / MVP
**Status:** Needs Review

### Actor

Customer

### Preconditions

* Customerが存在すること。
* Bank Accountが存在すること。

### Main Flow

1. CustomerがBank Accountの閉鎖を要求する。
2. システムが閉鎖可能性を検証する。
3. Account StatusをCLOSEDへ変更する。

### Postconditions

* Bank AccountがCLOSEDとなる。
* 過去の取引履歴およびLedgerは保持される。

### Failure Cases

* 閉鎖条件を満たさない場合

  * 閉鎖を拒否する。
  * Account Statusを変更しない。

### Related Business Rules

* TBD

### Open Questions

* OQ-UC-004
