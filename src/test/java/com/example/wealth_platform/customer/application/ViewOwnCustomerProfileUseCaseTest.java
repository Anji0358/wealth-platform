package com.example.wealth_platform.customer.application;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.example.wealth_platform.customer.domain.Customer;
import com.example.wealth_platform.customer.domain.CustomerRepository;

class ViewOwnCustomerProfileUseCaseTest {

	private static final UUID ACTING_CUSTOMER_ID =
			UUID.fromString("11111111-1111-1111-1111-111111111111");

	private static final String CUSTOMER_NAME="Taro Yamada";

	private static final Instant CUSTOMER_CREATED_AT =
			Instant.parse("2026-09-02T00:00:00Z");

	private static final Customer EXPECTED_CUSTOMER = new Customer(
			ACTING_CUSTOMER_ID,
			CUSTOMER_NAME,
			CUSTOMER_CREATED_AT);

	private static final class FixedActorContext implements ActorContext {

		private final UUID actingCustomerId;

		private FixedActorContext(UUID actingCustomerId) {
			this.actingCustomerId = actingCustomerId;
		}

		@Override
		public UUID actingCustomerId() {
			return actingCustomerId;
		}
	}

	private static final class StubCustomerRepository implements CustomerRepository {

		private final Customer customer;

		private StubCustomerRepository(Customer customer) {
			this.customer = customer;
		}

		@Override
		public Optional<Customer> findById(UUID customerId) {
			if (customer.getId().equals(customerId)) {
				return Optional.of(customer);
			}

			return Optional.empty();
		}

		@Override
		public void save(Customer customer) {
		}
	}

	@Test
	void returns_acting_customers_basic_information() {

		FixedActorContext actorContext=new FixedActorContext(ACTING_CUSTOMER_ID);
		StubCustomerRepository repository=new StubCustomerRepository(EXPECTED_CUSTOMER);

		ViewOwnCustomerProfileUseCase useCase=new ViewOwnCustomerProfileUseCase(
				actorContext,
				repository);

		Customer actualCustomer=useCase.view();

		assertEquals(ACTING_CUSTOMER_ID,actualCustomer.getId());
		assertEquals(CUSTOMER_NAME,actualCustomer.getName());
		assertEquals(CUSTOMER_CREATED_AT,actualCustomer.getCreatedAt());

	}

	@Test
	void throws_when_acting_customer_does_not_exist() {
		UUID missingCustomerId = UUID.fromString("22222222-2222-2222-2222-222222222222");

		FixedActorContext actorContext=new FixedActorContext(missingCustomerId);
		StubCustomerRepository repository=new StubCustomerRepository(EXPECTED_CUSTOMER);

		ViewOwnCustomerProfileUseCase useCase=new ViewOwnCustomerProfileUseCase(
				actorContext,
				repository);

		assertThrows(NoSuchElementException.class,()->useCase.view());

	}
}
