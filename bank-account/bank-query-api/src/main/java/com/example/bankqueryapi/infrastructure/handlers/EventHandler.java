package com.example.bankqueryapi.infrastructure.handlers;

import com.example.common.events.AccountClosedEvent;
import com.example.common.events.AccountOpenedEvent;
import com.example.common.events.FundsDepositedEvent;
import com.example.common.events.FundsWithdrawnEvent;

public interface EventHandler {
    void on(AccountOpenedEvent event);
    void on(FundsDepositedEvent event);
    void on(FundsWithdrawnEvent event);
    void on(AccountClosedEvent event);
}
