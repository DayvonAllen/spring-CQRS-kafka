package com.example.bankqueryapi.infrastructure.consumers;

import com.example.common.events.AccountClosedEvent;
import com.example.common.events.AccountOpenedEvent;
import com.example.common.events.FundsDepositedEvent;
import com.example.common.events.FundsWithdrawnEvent;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {
    void consume(@Payload AccountOpenedEvent event, Acknowledgment ack);
    void consume(@Payload FundsDepositedEvent event, Acknowledgment ack);
    void consume(@Payload FundsWithdrawnEvent event, Acknowledgment ack);
    void consume(@Payload AccountClosedEvent event, Acknowledgment ack);
}
