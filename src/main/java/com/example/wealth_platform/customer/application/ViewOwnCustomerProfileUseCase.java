package com.example.wealth_platform.customer.application;

import java.util.Optional;

import com.example.wealth_platform.customer.domain.Customer;
import com.example.wealth_platform.customer.domain.CustomerRepository;

public class ViewOwnCustomerProfileUseCase {

	private final ActorContext actorContext;
	private final CustomerRepository repository;

	ViewOwnCustomerProfileUseCase(
			ActorContext actorContext,
			CustomerRepository repository){

		this.actorContext=actorContext;
		this.repository=repository;
	}

	public Customer view() {

		Optional<Customer> customer=repository.findById(actorContext.actingCustomerId());
		return customer.orElseThrow();
	}

}
