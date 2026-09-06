package com.example.wealth_platform.ledger.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LedgerTransaction {

	private final List<LedgerEntry> ledgerEntries;

    LedgerTransaction(List<LedgerEntry> ledgerEntryList) {

        if (ledgerEntryList.size() <= 1) {
            throw new IllegalArgumentException();
        }

        long sum = 0L;

        for (LedgerEntry ledgerEntry : ledgerEntryList) {

            try {
                sum = Math.addExact(sum, ledgerEntry.getAmount());
            } catch (ArithmeticException e) {
                throw new IllegalArgumentException();
            }

        }


        if (sum != 0L) {
            throw new IllegalArgumentException();
        }

        this.ledgerEntries=new ArrayList<>(ledgerEntryList);
    }

    public List<LedgerEntry> getEntries() {
        return Collections.unmodifiableList(ledgerEntries);
    }
}