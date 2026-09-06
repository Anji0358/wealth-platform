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
}
