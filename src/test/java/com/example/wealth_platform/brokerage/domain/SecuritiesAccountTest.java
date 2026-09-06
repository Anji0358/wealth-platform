package com.example.wealth_platform.brokerage.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class SecuritiesAccountTest {
	private static final UUID SECURITIES_ACCOUNT_ID=UUID.fromString("00000000-0000-0000-0000-000000000000");

	private static final UUID CUSTOMER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

	private static final Instant CREATED_AT = Instant.parse("2026-09-02T00:00:00Z");

	@Test
	void initializes_with_zero_balances_and_active_status() {
		SecuritiesAccount securitiesAccount=new SecuritiesAccount(
				SECURITIES_ACCOUNT_ID,
				CUSTOMER_ID,
				CREATED_AT
				);

		assertEquals(securitiesAccount.getCurrentBalance(),0);
		assertEquals(securitiesAccount.getReservedAmount(),0);
		assertEquals(securitiesAccount.getAccountStatus(),SecuritiesAccountStatus.ACTIVE);

	}

	@Test
	void initializes_with_zero_available_balance() {
		SecuritiesAccount securitiesAccount=new SecuritiesAccount(
				SECURITIES_ACCOUNT_ID,
				CUSTOMER_ID,
				CREATED_AT
				);

		assertEquals(securitiesAccount.getAvailableBalance(),0L);
	}

	@Test
	void increases_cash_balance_when_receiving_positive_amount() {
		SecuritiesAccount securitiesAccount=new SecuritiesAccount(
				SECURITIES_ACCOUNT_ID,
				CUSTOMER_ID,
				CREATED_AT
				);
		securitiesAccount.receiveCash(100L);
		assertEquals(securitiesAccount.getCurrentBalance(),100);
		assertEquals(securitiesAccount.getAvailableBalance(),100L);
	}

	@Test
	void rejects_zero_amount_when_receiving_cash() {
		SecuritiesAccount securitiesAccount=new SecuritiesAccount(
				SECURITIES_ACCOUNT_ID,
				CUSTOMER_ID,
				CREATED_AT
				);
		assertThrows(IllegalArgumentException.class,()->securitiesAccount.receiveCash(0L));
		assertEquals(securitiesAccount.getCurrentBalance(),0L);
		assertEquals(securitiesAccount.getAvailableBalance(),0L);

	}

	@Test
	void rejects_negative_amount_when_receiving_cash() {
		SecuritiesAccount securitiesAccount=new SecuritiesAccount(
				SECURITIES_ACCOUNT_ID,
				CUSTOMER_ID,
				CREATED_AT
				);
		assertThrows(IllegalArgumentException.class,()->securitiesAccount.receiveCash(-100L));
		assertEquals(securitiesAccount.getCurrentBalance(),0L);
		assertEquals(securitiesAccount.getAvailableBalance(),0L);

	}

	@Test
	void accepts_cash_receipt_that_reaches_long_max_value() {
		SecuritiesAccount securitiesAccount=new SecuritiesAccount(
				SECURITIES_ACCOUNT_ID,
				CUSTOMER_ID,
				CREATED_AT
				);
		assertDoesNotThrow(()->securitiesAccount.receiveCash(Long.MAX_VALUE));
		assertEquals(securitiesAccount.getCurrentBalance(),Long.MAX_VALUE);
		assertEquals(securitiesAccount.getAvailableBalance(),Long.MAX_VALUE);
	}

	@Test
	void decreases_cash_balance_when_sending_amount_within_available_balance() {
		SecuritiesAccount securitiesAccount=new SecuritiesAccount(
				SECURITIES_ACCOUNT_ID,
				CUSTOMER_ID,
				CREATED_AT
				);
		securitiesAccount.receiveCash(100L);
		securitiesAccount.sendCash(40L);
		assertEquals(securitiesAccount.getCurrentBalance(),60L);
		assertEquals(securitiesAccount.getAvailableBalance(),60L);

	}

	@Test
	void rejects_zero_amount_when_sending_cash() {
		SecuritiesAccount securitiesAccount=new SecuritiesAccount(
				SECURITIES_ACCOUNT_ID,
				CUSTOMER_ID,
				CREATED_AT
				);

		securitiesAccount.receiveCash(100L);
		assertThrows(IllegalArgumentException.class,()->securitiesAccount.sendCash(0L));
		assertEquals(securitiesAccount.getCurrentBalance(),100L);
		assertEquals(securitiesAccount.getAvailableBalance(),100L);
	}

	@Test
	void rejects_negative_amount_when_sending_cash() {
		SecuritiesAccount securitiesAccount=new SecuritiesAccount(
				SECURITIES_ACCOUNT_ID,
				CUSTOMER_ID,
				CREATED_AT
				);

		securitiesAccount.receiveCash(100L);
		assertThrows(IllegalArgumentException.class,()->securitiesAccount.sendCash(-50L));
		assertEquals(securitiesAccount.getCurrentBalance(),100L);
		assertEquals(securitiesAccount.getAvailableBalance(),100L);
	}

	@Test
	void rejects_send_amount_exceeding_available_balance() {
		SecuritiesAccount securitiesAccount=new SecuritiesAccount(
				SECURITIES_ACCOUNT_ID,
				CUSTOMER_ID,
				CREATED_AT
				);

		securitiesAccount.receiveCash(100L);
		assertThrows(IllegalArgumentException.class,()->securitiesAccount.sendCash(101L));
		assertEquals(securitiesAccount.getCurrentBalance(),100L);
		assertEquals(securitiesAccount.getAvailableBalance(),100L);
	}

}
