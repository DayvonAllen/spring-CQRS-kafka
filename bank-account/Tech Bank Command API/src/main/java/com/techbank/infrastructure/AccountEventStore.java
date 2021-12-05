package com.techbank.infrastructure;

import com.example.core.events.BaseEvent;
import com.example.core.events.EventModel;
import com.example.core.exceptions.AggregateNotFoundException;
import com.example.core.exceptions.ConcurrencyException;
import com.example.core.infrastructure.EventStore;
import com.example.core.producers.EventProducer;
import com.techbank.domain.AccountAggregate;
import com.techbank.domain.EventStoreRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountEventStore implements EventStore {

    private final EventStoreRepository eventStoreRepository;

    private final EventProducer eventProducer;

    public AccountEventStore(EventStoreRepository eventStoreRepository, EventProducer eventProducer) {
        this.eventStoreRepository = eventStoreRepository;
        this.eventProducer = eventProducer;
    }

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        List<EventModel> eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if(expectedVersion != -1 && eventStream.get(eventStream.size() - 1).getVersion() != expectedVersion) {
            throw new ConcurrencyException();
        }
        int version = expectedVersion;

        for(BaseEvent event: events) {
            version++;
            event.setVersion(version);
            EventModel eventModel = EventModel.builder()
                    .timestamp(new Date())
                    .aggregateIdentifier(aggregateId)
                    .aggregateType(AccountAggregate.class.getTypeName())
                    .version(version)
                    .eventType(event.getClass().getTypeName())
                    .eventData(event)
                    .build();

            EventModel persistedEvent = eventStoreRepository.save(eventModel);

            if(!persistedEvent.getId().isEmpty()) {
                eventProducer.produce(event.getClass().getSimpleName(), event);
            }
        }

    }

    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        List<EventModel> eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);

        if(eventStream == null || eventStream.isEmpty()) {
            throw new AggregateNotFoundException("Incorrect account ID");
        }

        return eventStream.stream().map(EventModel::getEventData).collect(Collectors.toList());
    }
}
