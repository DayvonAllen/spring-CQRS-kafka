package com.example.core.producers;

import com.example.core.events.BaseEvent;

public interface EventProducer {
    void produce(String topic, BaseEvent event);
}
