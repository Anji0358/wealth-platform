package com.example.wealth_platform.banking.domain;

import java.time.Instant;
import java.util.UUID;

public class BankAccount {

	private UUID customerId;
	private UUID accountId;
	private Instant createdAt;
	private Instant closedAt;
	private long currentBalance;
	private long reservedAmount;
	private AccountStatus status = AccountStatus.ACTIVE;
	private AccountType accountType;

	public BankAccount(UUID accountId,UUID customerId,AccountType accountType,Instant createdAt) {
		if(customerId==null||accountId==null||accountType==null||createdAt==null) {
			throw new IllegalArgumentException();
		}

		this.customerId=customerId;
		this.accountId=accountId;
		this.accountType = accountType;
		this.createdAt=createdAt;
	}

	public UUID getCustomerId() {
		return customerId;
	}

	public UUID getAccountId() {
		return accountId;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getClosedAt() {
		if(status!=AccountStatus.CLOSED) {
			return null;
		}

		return closedAt;
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

	private void ensureAccountIsNotClosed() {
		if (status == AccountStatus.CLOSED) {
			throw new IllegalArgumentException();
		}
	}

	private void ensureAccountIsNotFrozen() {
		if (status == AccountStatus.FROZEN) {
			throw new IllegalArgumentException();
		}
	}

	public void deposit(long amount) {
		ensureAccountIsNotClosed();

		if (amount <= 0L) {
			throw new IllegalArgumentException();
		}

		if(currentBalance>Long.MAX_VALUE-amount) {
			throw new IllegalArgumentException();
		}

		currentBalance += amount;
	}

	public void withdraw(long amount) {
		ensureAccountIsNotFrozen();

		if (amount <= 0L) {
			throw new IllegalArgumentException();
		}

		if (getAvailableBalance() < amount) {
			throw new IllegalArgumentException();
		}

		currentBalance -= amount;
	}

	public void freeze() {
		ensureAccountIsNotFrozen();
		ensureAccountIsNotClosed();

		status = AccountStatus.FROZEN;
	}

	public void unfreeze() {
		if (status == AccountStatus.ACTIVE) {
			throw new IllegalArgumentException();
		}

		ensureAccountIsNotClosed();

		status = AccountStatus.ACTIVE;
	}

	public void close(Instant now) {
		ensureAccountIsNotClosed();
		ensureAccountIsNotFrozen();

		if(now==null) {
			 throw new IllegalArgumentException();
		}

		if (currentBalance != 0L || reservedAmount != 0L) {
	        throw new IllegalArgumentException();
	    }

		status = AccountStatus.CLOSED;
		this.closedAt=now;
	}

}
