package com.techbank.domain;

import com.example.common.events.AccountClosedEvent;
import com.example.common.events.AccountOpenedEvent;
import com.example.common.events.FundsDepositedEvent;
import com.example.common.events.FundsWithdrawnEvent;
import com.example.core.domain.AggregateRoot;
import com.techbank.api.commands.OpenAccountCommand;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {
    private Boolean active;
    private double balance;

    public double getBalance() {
        return balance;
    }

    public  AccountAggregate(OpenAccountCommand command) {
        raiseEvent(AccountOpenedEvent.builder()
                .id(command.getId())
                .accountHolder(command.getAccountHolder())
                .createdDate(new Date())
                .accountType(command.getAccountType())
                .openingBalance(command.getOpeningBalance())
                .build());
    }

    public void apply(AccountOpenedEvent event) {
        this.id = event.getId();
        this.active = true;
        this.balance = event.getOpeningBalance();
    }

    public void depositFunds(double amount) {
        if(!this.active) {
            throw new IllegalStateException("Funds cannot be deposited into a closed account...");
        }

        if(amount <= 0) {
            throw new IllegalStateException("Amount must be greater or equal to 0");
        }

        raiseEvent(FundsDepositedEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    public void apply(FundsDepositedEvent event) {
        this.id = event.getId();
        this.active = true;
        this.balance += event.getAmount();
    }

    public void withdrawFunds(double amount) {
        if(!this.active) {
            throw new IllegalStateException("Funds cannot be withdrawn into a closed account...");
        }

        if(amount <= 0) {
            throw new IllegalStateException("Amount must be greater or equal to 0");
        }

        raiseEvent(FundsWithdrawnEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    public void apply(FundsWithdrawnEvent event) {
        this.id = event.getId();
        this.active = true;
        this.balance -= event.getAmount();
    }

    public void closeAccount() {
        if(!this.active) {
            throw new IllegalStateException("Account is already closed...");
        }

        raiseEvent(AccountClosedEvent.builder()
                .id(this.id)
                .build());
    }

    public void apply(AccountClosedEvent event) {
        this.id = event.getId();
        this.active = false;
    }
}
