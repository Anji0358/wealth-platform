package com.example.wealth_platform.customer.application;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

import com.example.wealth_platform.customer.domain.Customer;
import com.example.wealth_platform.customer.domain.CustomerRepository;

public class ProvisionCustomerUseCase {

	private final CustomerRepository repository;
	private final CustomerIdGenerator idGenerator;
	private final Clock clock;

	public ProvisionCustomerUseCase(
			CustomerRepository repository,
			CustomerIdGenerator idGenerator,
			Clock clock)
	{
		this.repository=repository;
		this.idGenerator=idGenerator;
		this.clock=clock;
	}

	public Customer provision(String customerName) {
		UUID customerId=idGenerator.nextId();
		Instant createdAt=Instant.now(clock);

		Customer customer=new Customer(customerId,customerName,createdAt);
		repository.save(customer);

		return customer;
	}
}
