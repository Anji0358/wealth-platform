package com.example.wealth_platform.banking.domain;

public class BankAccount {

	private long currentBalance;
	private long reservedAmount;
	private AccountStatus status = AccountStatus.ACTIVE;
	private AccountType accountType;

	BankAccount(AccountType accountType) {
		this.accountType = accountType;
	}

	public long getCurrentBalance() {
		return currentBalance;
	}

	public long getReservedAmount() {
		return reservedAmount;
	}

	public long getAvailableBalance() {
		return currentBalance - reservedAmount;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void deposit(long amount) {
		if (amount <= 0L) {
			throw new IllegalArgumentException();
		}
		currentBalance += amount;
	}

	public void withdraw(long amount) {
		if (amount <= 0L) {
			throw new IllegalArgumentException();
		}

		if (getAvailableBalance() < amount) {
			throw new IllegalArgumentException();
		}

		currentBalance -= amount;
	}

	public void freeze() {
		status = AccountStatus.FROZEN;
	}

}
