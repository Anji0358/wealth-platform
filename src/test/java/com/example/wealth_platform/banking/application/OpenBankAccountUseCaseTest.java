package com.example.wealth_platform.banking.application;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.example.wealth_platform.banking.domain.AccountType;
import com.example.wealth_platform.banking.domain.BankAccount;
import com.example.wealth_platform.banking.domain.BankAccountRepository;
public class OpenBankAccountUseCaseTest {
	
	private static final UUID BANK_ACCOUNT_ID =UUID.fromString("11111111-1111-1111-1111-111111111111");
	private static final UUID CUSTOMER_ID =UUID.fromString("00000000-0000-0000-0000-000000000000");
	private static final Instant ACCOUNT_CREATED_AT = Instant.parse("2026-09-03T00:00:00Z");
	private static final Clock FIXED_CLOCK = Clock.fixed(ACCOUNT_CREATED_AT, ZoneOffset.UTC);
	
	private static final class FixedBankAccountIdGenerator implements BankAccountIdGenerator {

		private final UUID bankAccountId;
		
		private FixedBankAccountIdGenerator(UUID bankAccountId) {
			this.bankAccountId = bankAccountId;
			
		}

		@Override
		public UUID nextId() {
			return bankAccountId;
		}
	}
	
	private static final class RecordingBankAccountRepository implements BankAccountRepository {

		private BankAccount savedBankAccount;

		@Override
		public void save(BankAccount bankAccount) {
			this.savedBankAccount = bankAccount;
			
		}

		BankAccount savedBankAccount() {
			return savedBankAccount;
			
		}
	}

	@Test
	void opens_bank_account_for_customer(){
		
		RecordingBankAccountRepository repository=new RecordingBankAccountRepository();
		
		OpenBankAccountUseCase useCase=new OpenBankAccountUseCase (
				repository,
				new FixedBankAccountIdGenerator(BANK_ACCOUNT_ID),
				FIXED_CLOCK
				);
		
		useCase.open(CUSTOMER_ID, AccountType.ORDINARY_DEPOSIT);
		
		BankAccount savedAccount = repository.savedBankAccount();
		
		assertEquals(BANK_ACCOUNT_ID, savedAccount.getAccountId());
        assertEquals(CUSTOMER_ID, savedAccount.getCustomerId());
        assertEquals(AccountType.ORDINARY_DEPOSIT,
        savedAccount.getAccountType());
        assertEquals(ACCOUNT_CREATED_AT, savedAccount.getCreatedAt());

	}

}
