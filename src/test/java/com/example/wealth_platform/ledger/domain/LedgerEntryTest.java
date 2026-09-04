package com.example.wealth_platform.ledger.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class LedgerEntryTest {
	UUID LEDGER_ACCOUNT_ID=UUID.fromString("00000000-0000-0000-0000-000000000000");

	@Test
	void rejects_zero_amount() {
		assertThrows(IllegalArgumentException.class,()->new LedgerEntry(LEDGER_ACCOUNT_ID,0L));
	}

	@Test
	void retains_specified_ledger_account_id() {
		LedgerEntry ledgerEntry=new LedgerEntry(LEDGER_ACCOUNT_ID,100L);
		assertEquals(ledgerEntry.getLedgerAccountId(),LEDGER_ACCOUNT_ID);
	}

	@Test
	void rejects_null_ledger_account_id() {
		assertThrows(IllegalArgumentException.class,()->new LedgerEntry(null,100L));
	}

}
