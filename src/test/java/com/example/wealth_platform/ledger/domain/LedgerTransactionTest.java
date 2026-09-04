package com.example.wealth_platform.ledger.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class LedgerTransactionTest {
	UUID LEDGER_ACCOUNT_ID=UUID.fromString("00000000-0000-0000-0000-000000000000");
	UUID LEDGER_ACCOUNT_ID_1=UUID.fromString("00000000-0000-0000-0000-000000000001");

	@Test
	void rejects_transaction_with_fewer_than_two_entries() {
		ArrayList<LedgerEntry> ledgerEntryList=new ArrayList<>();
		LedgerEntry ledgerEntry=new LedgerEntry(LEDGER_ACCOUNT_ID,100L);
		ledgerEntryList.add(ledgerEntry);
		assertThrows(IllegalArgumentException.class,()->new LedgerTransaction(ledgerEntryList));
	}

	@Test
	void rejects_transaction_with_non_zero_sum_entries() {
		LedgerEntry ledgerEntry1=new LedgerEntry(LEDGER_ACCOUNT_ID,100L);
		LedgerEntry ledgerEntry2=new LedgerEntry(LEDGER_ACCOUNT_ID,-99L);

		ArrayList<LedgerEntry> ledgerEntryList=new ArrayList<>();
		ledgerEntryList.add(ledgerEntry1);
		ledgerEntryList.add(ledgerEntry2);

		assertThrows(IllegalArgumentException.class,()->new LedgerTransaction(ledgerEntryList));

	}

	@Test
	void rejects_transaction_whose_amount_sum_overflows_to_zero() {
		LedgerEntry max_ledgerEntry_1=new LedgerEntry(LEDGER_ACCOUNT_ID,Long.MAX_VALUE);
		LedgerEntry max_ledgerEntry_2=new LedgerEntry(LEDGER_ACCOUNT_ID,Long.MAX_VALUE);
		LedgerEntry ledgerEntry=new LedgerEntry(LEDGER_ACCOUNT_ID,2L);

		ArrayList<LedgerEntry> ledgerEntryList=new ArrayList<>();
		ledgerEntryList.add(max_ledgerEntry_1);
		ledgerEntryList.add(max_ledgerEntry_2);
		ledgerEntryList.add(ledgerEntry);

		assertThrows(IllegalArgumentException.class,()->new LedgerTransaction(ledgerEntryList));
	}

	@Test
	void accepts_zero_sum_transaction() {
		LedgerEntry ledgerEntry1=new LedgerEntry(LEDGER_ACCOUNT_ID,100L);
		LedgerEntry ledgerEntry2=new LedgerEntry(LEDGER_ACCOUNT_ID_1,-100L);

		ArrayList<LedgerEntry> ledgerEntryList=new ArrayList<>();
		ledgerEntryList.add(ledgerEntry1);
		ledgerEntryList.add(ledgerEntry2);

		assertDoesNotThrow(()->new LedgerTransaction(ledgerEntryList));

	}

	@Test
	void retains_specified_entries() {

		LedgerEntry ledgerEntry1=new LedgerEntry(LEDGER_ACCOUNT_ID,100L);
		LedgerEntry ledgerEntry2=new LedgerEntry(LEDGER_ACCOUNT_ID_1,-100L);

		ArrayList<LedgerEntry> ledgerEntryList=new ArrayList<>();
		ledgerEntryList.add(ledgerEntry1);
		ledgerEntryList.add(ledgerEntry2);

		LedgerTransaction ledgerTransaction=new LedgerTransaction(ledgerEntryList);
		assertEquals(ledgerTransaction.getEntries(),ledgerEntryList);

	}

}
