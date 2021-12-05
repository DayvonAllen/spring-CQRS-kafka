package com.example.bankqueryapi.infrastructure.consumers;

import com.example.bankqueryapi.infrastructure.handlers.EventHandler;
import com.example.common.events.AccountClosedEvent;
import com.example.common.events.AccountOpenedEvent;
import com.example.common.events.FundsDepositedEvent;
import com.example.common.events.FundsWithdrawnEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

public class AccountEventConsumer implements EventConsumer{

    private final EventHandler eventHandler;

    public AccountEventConsumer(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @KafkaListener(topics = "AccountOpenedEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(AccountOpenedEvent event, Acknowledgment ack) {
        eventHandler.on(event);
        ack.acknowledge();
    }

    @KafkaListener(topics = "FundsDepositedEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(FundsDepositedEvent event, Acknowledgment ack) {
        eventHandler.on(event);
        ack.acknowledge();
    }

    @KafkaListener(topics = "FundsWithdrawnEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(FundsWithdrawnEvent event, Acknowledgment ack) {
        eventHandler.on(event);
        ack.acknowledge();
    }

    @KafkaListener(topics = "AccountClosedEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(AccountClosedEvent event, Acknowledgment ack) {
        eventHandler.on(event);
        ack.acknowledge();
    }
}
