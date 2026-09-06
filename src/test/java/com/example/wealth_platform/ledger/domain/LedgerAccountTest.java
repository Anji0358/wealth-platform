package com.example.wealth_platform.ledger.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class LedgerAccountTest {
	UUID LEDGER_ACCOUNT_ID=UUID.fromString("00000000-0000-0000-0000-000000000000");

	@Test
	void retains_specified_id_and_kind() {

		LedgerAccount ledgerAccount=new LedgerAccount(
				LEDGER_ACCOUNT_ID,
				LedgerAccountKind.BANK_ACCOUNT
				);

		assertEquals(ledgerAccount.getAccountId(),LEDGER_ACCOUNT_ID);
		assertEquals(ledgerAccount.getAccountKind(),LedgerAccountKind.BANK_ACCOUNT);

	}

}
