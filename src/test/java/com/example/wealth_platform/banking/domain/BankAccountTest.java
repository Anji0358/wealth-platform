package com.example.wealth_platform.banking.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class BankAccountTest {

	private static final UUID CUSTOMER_ID =
			UUID.fromString("11111111-1111-1111-1111-111111111111");

	private static final UUID ACCOUNT_ID =
			UUID.fromString("00000000-0000-0000-0000-000000000000");

	private static final Instant ACCOUNT_CREATED_AT =
			Instant.parse("2026-09-02T00:00:00Z");

	private static final Instant ACCOUNT_CLOSED_AT =
			Instant.parse("2027-09-02T00:00:00Z");


	private BankAccount ordinaryDepositAccount() {
		return new BankAccount(CUSTOMER_ID,ACCOUNT_ID,AccountType.ORDINARY_DEPOSIT,ACCOUNT_CREATED_AT);
	}

	@Test
	void starts_with_zero_current_balance() {
		BankAccount account = ordinaryDepositAccount();

		assertEquals(0L, account.getCurrentBalance());
	}

	@Test
	void starts_with_zero_reserved_amount() {
		BankAccount account = ordinaryDepositAccount();

		assertEquals(0L, account.getReservedAmount());
	}

	@Test
	void starts_with_active_status() {
		BankAccount account = ordinaryDepositAccount();

		assertEquals(AccountStatus.ACTIVE, account.getStatus());
	}

	@Test
	void starts_with_zero_available_balance() {
		BankAccount account = ordinaryDepositAccount();

		assertEquals(0L, account.getAvailableBalance());
	}

	@Test
	void deposit_updates_current_and_available_balance() {
		BankAccount account = ordinaryDepositAccount();

		account.deposit(100L);

		assertEquals(100L, account.getCurrentBalance());
		assertEquals(100L, account.getAvailableBalance());
	}

	@Test
	void rejects_zero_deposit() {
		BankAccount account = ordinaryDepositAccount();

		assertThrows(IllegalArgumentException.class, () -> account.deposit(0L));
		assertEquals(0L, account.getCurrentBalance());
	}

	@Test
	void rejects_negative_deposit() {
		BankAccount account = ordinaryDepositAccount();

		assertThrows(IllegalArgumentException.class, () -> account.deposit(-1L));
		assertEquals(0L, account.getCurrentBalance());
	}

	@Test
	void retains_specified_account_type() {
		BankAccount account = ordinaryDepositAccount();

		assertEquals(AccountType.ORDINARY_DEPOSIT, account.getAccountType());
	}

	@Test
	void retains_savings_account_type() {
		BankAccount account = new BankAccount(CUSTOMER_ID,ACCOUNT_ID,AccountType.SAVINGS,ACCOUNT_CREATED_AT);

		assertEquals(AccountType.SAVINGS, account.getAccountType());
	}

	@Test
	void withdraw_decreases_current_and_available_balance() {
		BankAccount account = ordinaryDepositAccount();
		account.deposit(100L);

		account.withdraw(40L);

		assertEquals(60L, account.getCurrentBalance());
		assertEquals(60L, account.getAvailableBalance());
	}

	@Test
	void rejects_zero_withdrawal() {
		BankAccount account = ordinaryDepositAccount();
		account.deposit(100L);

		assertThrows(IllegalArgumentException.class, () -> account.withdraw(0L));
		assertEquals(100L, account.getCurrentBalance());
	}

	@Test
	void rejects_negative_withdrawal() {
		BankAccount account = ordinaryDepositAccount();
		account.deposit(100L);

		assertThrows(IllegalArgumentException.class, () -> account.withdraw(-1L));
		assertEquals(100L, account.getCurrentBalance());
	}

	@Test
	void withdraw_equal_to_available_balance_sets_balances_to_zero() {
		BankAccount account = ordinaryDepositAccount();
		account.deposit(100L);

		account.withdraw(100L);

		assertEquals(0L, account.getCurrentBalance());
		assertEquals(0L, account.getAvailableBalance());
	}

	@Test
	void rejects_withdrawal_exceeding_available_balance() {
		BankAccount account = ordinaryDepositAccount();
		account.deposit(100L);

		assertThrows(IllegalArgumentException.class, () -> account.withdraw(101L));
		assertEquals(100L, account.getCurrentBalance());
		assertEquals(100L, account.getAvailableBalance());
	}

	@Test
	void freezing_sets_status_to_frozen() {
		BankAccount account = ordinaryDepositAccount();

		account.freeze();

		assertEquals(AccountStatus.FROZEN, account.getStatus());
	}

	@Test
	void frozen_account_can_receive_deposit() {
		BankAccount account = ordinaryDepositAccount();

		account.freeze();

		account.deposit(100L);

		assertEquals(100L, account.getCurrentBalance());
		assertEquals(100L, account.getAvailableBalance());

	}

	@Test
	void frozen_account_cannot_withdraw() {
		BankAccount account = ordinaryDepositAccount();

		account.freeze();

		account.deposit(100L);
		assertThrows(IllegalArgumentException.class, () -> account.withdraw(50L));
		assertEquals(100L, account.getCurrentBalance());
		assertEquals(100L, account.getAvailableBalance());
	}

	@Test
	void frozen_account_can_be_unfrozen() {
		BankAccount account = ordinaryDepositAccount();

		account.freeze();
		account.unfreeze();

		assertEquals(AccountStatus.ACTIVE, account.getStatus());

	}

	@Test
	void frozen_account_cannot_be_frozen_again() {
		BankAccount account = ordinaryDepositAccount();

		account.freeze();
		assertThrows(IllegalArgumentException.class, () -> account.freeze());
		assertEquals(AccountStatus.FROZEN, account.getStatus());
	}

	@Test
	void active_account_cannot_be_unfrozen() {
		BankAccount account = ordinaryDepositAccount();

		account.freeze();
		account.unfreeze();

		assertThrows(IllegalArgumentException.class, () -> account.unfreeze());
		assertEquals(AccountStatus.ACTIVE, account.getStatus());
	}

	@Test
	void active_zero_balance_account_can_close() {
		BankAccount account = ordinaryDepositAccount();
		account.close(ACCOUNT_CLOSED_AT);

		assertEquals(AccountStatus.CLOSED, account.getStatus());

	}

	@Test
	void frozen_account_cannot_close() {
		BankAccount account = ordinaryDepositAccount();

		account.freeze();
		assertThrows(IllegalArgumentException.class, () -> account.close(ACCOUNT_CLOSED_AT));
		assertEquals(AccountStatus.FROZEN, account.getStatus());
	}

	@Test
	void active_account_with_positive_balance_cannot_close() {
		BankAccount account = ordinaryDepositAccount();
		account.deposit(100L);

		assertThrows(IllegalArgumentException.class, () -> account.close(ACCOUNT_CLOSED_AT));
		assertEquals(AccountStatus.ACTIVE, account.getStatus());
		assertEquals(100L, account.getCurrentBalance());
		assertEquals(100L, account.getAvailableBalance());
	}

	@Test
	void closed_account_cannot_receive_deposit() {
		BankAccount account = ordinaryDepositAccount();
		account.close(ACCOUNT_CLOSED_AT);

		assertThrows(IllegalArgumentException.class, () -> account.deposit(100L));
		assertEquals(AccountStatus.CLOSED, account.getStatus());
		assertEquals(0L, account.getCurrentBalance());
		assertEquals(0L, account.getAvailableBalance());
	}

	@Test
	void closed_account_cannot_be_unfrozen() {
		BankAccount account = ordinaryDepositAccount();
		account.close(ACCOUNT_CLOSED_AT);

		assertThrows(IllegalArgumentException.class, () -> account.unfreeze());
		assertEquals(AccountStatus.CLOSED, account.getStatus());
	}

	@Test
	void closed_account_cannot_be_frozen() {
		BankAccount account = ordinaryDepositAccount();
		account.close(ACCOUNT_CLOSED_AT);

		assertThrows(IllegalArgumentException.class, () -> account.freeze());
		assertEquals(AccountStatus.CLOSED, account.getStatus());
	}

	@Test
	void rejects_null_account_type() {
		assertThrows(IllegalArgumentException.class, () -> new BankAccount(CUSTOMER_ID,ACCOUNT_ID,null,ACCOUNT_CREATED_AT));

	}

	@Test
	void rejects_deposit_that_would_overflow_current_balance() {
		BankAccount account = ordinaryDepositAccount();
		account.deposit(Long.MAX_VALUE);

		assertThrows(IllegalArgumentException.class, () -> account.deposit(1L));
		assertEquals(Long.MAX_VALUE, account.getCurrentBalance());
		assertEquals(Long.MAX_VALUE, account.getAvailableBalance());
	}

	@Test
	void retains_specified_customer_id() {
		BankAccount account = ordinaryDepositAccount();
		assertEquals(CUSTOMER_ID, account.getCustomerId());

	}

	@Test
	void rejects_null_customer_id() {
		assertThrows(IllegalArgumentException.class, () -> new BankAccount(null,ACCOUNT_ID,AccountType.ORDINARY_DEPOSIT,ACCOUNT_CREATED_AT));
	}

	@Test
	void retains_specified_account_id() {
		BankAccount account = ordinaryDepositAccount();
		assertEquals(ACCOUNT_ID, account.getAccountId());

	}

	@Test
	void rejects_null_account_id() {
		assertThrows(IllegalArgumentException.class, () -> new BankAccount(CUSTOMER_ID,null,AccountType.ORDINARY_DEPOSIT,ACCOUNT_CREATED_AT));

	}

	@Test
	void retains_specified_creation_time() {
		BankAccount account = ordinaryDepositAccount();
		assertEquals(ACCOUNT_CREATED_AT, account.getCreatedAt());

	}

	@Test
	void rejects_null_creation_time() {
		assertThrows(IllegalArgumentException.class, () -> new BankAccount(CUSTOMER_ID,ACCOUNT_ID,AccountType.ORDINARY_DEPOSIT,null));

	}

	@Test
	void starts_without_closure_time() {
		BankAccount account = ordinaryDepositAccount();
		assertEquals(null,account.getClosedAt());
	}

	@Test
	void records_specified_closure_time() {
		BankAccount account = ordinaryDepositAccount();
		account.close(ACCOUNT_CLOSED_AT);
		assertEquals(ACCOUNT_CLOSED_AT,account.getClosedAt());
	}

	@Test
	void rejects_null_closure_time() {
		BankAccount account = ordinaryDepositAccount();

		assertThrows(IllegalArgumentException.class, () -> account.close(null));
		assertEquals(AccountStatus.ACTIVE, account.getStatus());
	}



}
