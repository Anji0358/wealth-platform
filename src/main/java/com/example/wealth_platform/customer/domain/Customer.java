package com.example.wealth_platform.customer.domain;

import java.time.Instant;
import java.util.UUID;

public class Customer {

	private final UUID customerId;
	private final String customerName;
	private final Instant createdAt;

	public Customer(
			UUID customerId,
			String customerName,
			Instant createdAt)
	{
		this.customerId=customerId;
		this.customerName=customerName;
		this.createdAt=createdAt;
	}

	public UUID getId() {
		return customerId;
	}

	public String getName() {
		return customerName;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}
}
