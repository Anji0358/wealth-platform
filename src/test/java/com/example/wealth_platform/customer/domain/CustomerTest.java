package com.example.wealth_platform.customer.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class CustomerTest {

	@Test
	void test_provisioned_customer_has_given_internal_fields() {

		UUID fixedId = UUID.fromString("11111111-1111-1111-1111-111111111111");
		String customerName ="Taro Yamada";
		Instant createdAt = Instant.parse("2023-10-01T10:15:30.00Z");

		Customer customer= new Customer(
				fixedId,
				customerName,
				createdAt);

		assertEquals(fixedId,customer.getId());
		assertEquals(customerName,customer.getName());
		assertEquals(createdAt,customer.getCreatedAt());

	}


}
