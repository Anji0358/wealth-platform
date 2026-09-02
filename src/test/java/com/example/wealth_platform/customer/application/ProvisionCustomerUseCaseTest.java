package com.example.wealth_platform.customer.application;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.example.wealth_platform.customer.domain.Customer;
import com.example.wealth_platform.customer.domain.CustomerRepository;

public class ProvisionCustomerUseCaseTest {

	private static final class RecordingCustomerRepository
			implements CustomerRepository {

		private int saveCount;
		private Customer savedCustomer;

		@Override
		public void save(Customer customer) {
			this.savedCustomer=customer;
			saveCount++;
		}

		int saveCount() {
			return saveCount;
		}
		public Customer getCustomer() {
			return savedCustomer;
		}
	}

	@Test
	void testProvision() {

		String customerName="Taro Yamada";
		RecordingCustomerRepository customerRepository = new RecordingCustomerRepository();
		UUID fixedId = UUID.fromString("11111111-1111-1111-1111-111111111111");

		CustomerIdGenerator customerIdGenerator = () -> fixedId;
		Instant fixedInstant = Instant.parse("2026-09-02T00:00:00Z");
		Clock clock = Clock.fixed(fixedInstant, ZoneOffset.UTC);

		ProvisionCustomerUseCase useCase =
			    new ProvisionCustomerUseCase(
			        customerRepository,
			        customerIdGenerator,
			        clock
			    );
		useCase.provision(customerName);

		assertEquals(1, customerRepository.saveCount());

		assertEquals(fixedId,customerRepository.getCustomer().getId());
		assertEquals(customerName,customerRepository.getCustomer().getName());
		assertEquals(fixedInstant,customerRepository.getCustomer().getCreatedAt());
	}

}
