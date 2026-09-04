package com.example.wealth_platform.banking.application;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

import com.example.wealth_platform.banking.domain.AccountType;
import com.example.wealth_platform.banking.domain.BankAccount;
import com.example.wealth_platform.banking.domain.BankAccountRepository;

public class OpenBankAccountUseCase {
	
	private final BankAccountRepository repository;
	private final BankAccountIdGenerator bankAccountIdGenerator;
	private final Clock clock;
	
	OpenBankAccountUseCase(BankAccountRepository repository,BankAccountIdGenerator bankAccountIdGenerator,Clock clock){
		this.repository=repository;
		this.bankAccountIdGenerator = bankAccountIdGenerator;
		this.clock=clock;

	}

	public BankAccount open(UUID customerId,AccountType accountType) {

		UUID bankAccountId=bankAccountIdGenerator.nextId();
		Instant createdAt=Instant.now(clock);
		BankAccount newBankAccount=new BankAccount(bankAccountId,customerId,accountType,createdAt);
		repository.save(newBankAccount);
		return newBankAccount;
		
	}

}
