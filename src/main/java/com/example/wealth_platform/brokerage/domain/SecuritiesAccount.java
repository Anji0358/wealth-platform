package com.example.wealth_platform.brokerage.domain;

import java.time.Instant;
import java.util.UUID;

public class SecuritiesAccount {

	private UUID securitiesAccountId;
	private UUID customerId;
	private Instant createdAt;
	private SecuritiesAccountStatus securitiesAccountStatus;
	private long currentBalance;
	private long reservedAmount;

	SecuritiesAccount(UUID securitiesAccountId,UUID customerId,Instant createdAt){
		this.securitiesAccountId=securitiesAccountId;
		this.customerId=customerId;
		this.createdAt=createdAt;
		this.securitiesAccountStatus=SecuritiesAccountStatus.ACTIVE;
		this.currentBalance=0;
		this.reservedAmount=0;
	}

	public UUID getSecuritiesAccountId() {
		return securitiesAccountId;
	}

	public UUID getCustomerId() {
		return customerId;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public SecuritiesAccountStatus getAccountStatus() {
		return securitiesAccountStatus;
	}

	public long getCurrentBalance() {
		return currentBalance;
	}

	public long getReservedAmount() {
		return reservedAmount;
	}

	public long getAvailableBalance() {
		return currentBalance-reservedAmount;
	}

	public void receiveCash(long amount) {
		if(amount <= 0) {
			throw new IllegalArgumentException();
		}

		if(currentBalance > Long.MAX_VALUE - amount) {
			throw new IllegalArgumentException();

		}

		currentBalance+=amount;
	}

	public void sendCash(long amount) {

		if(amount <= 0){
			throw new IllegalArgumentException();
		}
		if(this.getAvailableBalance() < amount) {
			throw new IllegalArgumentException();
		}

		currentBalance-=amount;
	}
}
