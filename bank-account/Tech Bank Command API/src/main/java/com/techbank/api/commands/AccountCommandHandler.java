package com.techbank.api.commands;

import com.example.core.handlers.EventSourcingHandler;
import com.techbank.domain.AccountAggregate;
import org.springframework.stereotype.Service;

@Service
public class AccountCommandHandler implements CommandHandler {

    private final EventSourcingHandler<AccountAggregate> eventSourcingHandler;

    public AccountCommandHandler(EventSourcingHandler<AccountAggregate> eventSourcingHandler) {
        this.eventSourcingHandler = eventSourcingHandler;
    }

    @Override
    public void handle(OpenAccountCommand command) {
        AccountAggregate accountAggregate = new AccountAggregate(command);
        eventSourcingHandler.save(accountAggregate);
    }

    @Override
    public void handle(DepositFundsCommand command) {
        AccountAggregate accountAggregate = eventSourcingHandler.getById(command.getId());
        accountAggregate.depositFunds(command.getAmount());
        eventSourcingHandler.save(accountAggregate);
    }

    @Override
    public void handle(WithdrawFundsCommand command) {
        AccountAggregate accountAggregate = eventSourcingHandler.getById(command.getId());
        if(command.getAmount() > accountAggregate.getBalance()) {
            throw new IllegalStateException("Withdraw declined, insufficient funds");
        }
        accountAggregate.withdrawFunds(command.getAmount());
        eventSourcingHandler.save(accountAggregate);
    }

    @Override
    public void handle(CloseAccountCommand command) {
        AccountAggregate accountAggregate = eventSourcingHandler.getById(command.getId());
        accountAggregate.closeAccount();
        eventSourcingHandler.save(accountAggregate);
    }
}
