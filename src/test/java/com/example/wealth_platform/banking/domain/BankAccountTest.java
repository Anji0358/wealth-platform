package com.example.wealth_platform.banking.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BankAccountTest {

	private BankAccount ordinaryDepositAccount() {
		return new BankAccount(AccountType.ORDINARY_DEPOSIT);
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
		BankAccount account = new BankAccount(AccountType.SAVINGS);

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

}
