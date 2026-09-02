package com.example.wealth_platform.customer.domain;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

	void save(Customer customer);

	Optional<Customer> findById(UUID customerId);

}
