package com.example.wealth_platform.ledger.domain;

import java.util.UUID;

public class LedgerEntry {

	private final UUID ledgerAccountId;
	private final long amount;

	LedgerEntry(UUID ledgerAccountId,long amount){

		if(ledgerAccountId==null || amount==0) {
			throw new IllegalArgumentException();
		}

		this.ledgerAccountId=ledgerAccountId;
		this.amount=amount;
	}

	public UUID getLedgerAccountId() {
		return ledgerAccountId;
	}

	public long getAmount() {
		return amount;
	}
}
