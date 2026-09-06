package com.example.wealth_platform.ledger.domain;

import java.util.UUID;

public class LedgerAccount {
	private final UUID ledgerAccountId;
	private final LedgerAccountKind ledgerAccountKind;

	LedgerAccount(UUID ledgerAccountId,LedgerAccountKind ledgerAccountKind){
		this.ledgerAccountId=ledgerAccountId;
		this.ledgerAccountKind=ledgerAccountKind;
	}

	public UUID getAccountId() {
		return ledgerAccountId;
	}

	public LedgerAccountKind getAccountKind() {
		return ledgerAccountKind;
	}


}
