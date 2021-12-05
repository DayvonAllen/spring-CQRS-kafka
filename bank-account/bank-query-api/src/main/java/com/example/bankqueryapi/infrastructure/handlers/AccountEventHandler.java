package com.example.bankqueryapi.infrastructure.handlers;

import com.example.bankqueryapi.domain.AccountRepository;
import com.example.bankqueryapi.domain.BankAccount;
import com.example.common.events.AccountClosedEvent;
import com.example.common.events.AccountOpenedEvent;
import com.example.common.events.FundsDepositedEvent;
import com.example.common.events.FundsWithdrawnEvent;
import org.springframework.stereotype.Service;

@Service
public class AccountEventHandler implements EventHandler{

    private final AccountRepository accountRepository;

    public AccountEventHandler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void on(AccountOpenedEvent event) {
        BankAccount bankAccount = BankAccount.builder()
                .id(event.getId())
                .accountHolder(event.getAccountHolder())
                .creationDate(event.getCreatedDate())
                .accountType(event.getAccountType())
                .balance(event.getOpeningBalance())
                .build();

        accountRepository.save(bankAccount);
    }

    @Override
    public void on(FundsDepositedEvent event) {
        BankAccount bankAccount = accountRepository.findById(event.getId()).orElseThrow(RuntimeException::new);
        double currentBalance = bankAccount.getBalance();
        double latestBalance = currentBalance + event.getAmount();
        bankAccount.setBalance(latestBalance);
        accountRepository.save(bankAccount);
    }

    @Override
    public void on(FundsWithdrawnEvent event) {
        BankAccount bankAccount = accountRepository.findById(event.getId()).orElseThrow(RuntimeException::new);
        double currentBalance = bankAccount.getBalance();
        double latestBalance = currentBalance - event.getAmount();
        bankAccount.setBalance(latestBalance);
        accountRepository.save(bankAccount);
    }

    @Override
    public void on(AccountClosedEvent event) {
        accountRepository.deleteById(event.getId());
    }
}
