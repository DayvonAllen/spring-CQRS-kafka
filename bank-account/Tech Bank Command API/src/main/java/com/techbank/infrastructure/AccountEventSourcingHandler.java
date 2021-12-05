package com.techbank.infrastructure;

import com.example.core.domain.AggregateRoot;
import com.example.core.events.BaseEvent;
import com.example.core.events.EventModel;
import com.example.core.handlers.EventSourcingHandler;
import com.example.core.infrastructure.EventStore;
import com.techbank.domain.AccountAggregate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {

    private final EventStore eventStore;

    public AccountEventSourcingHandler(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void save(AggregateRoot aggregate) {
        eventStore.saveEvents(aggregate.getId(), aggregate.getUncommittedChanges(), aggregate.getVersion());
        aggregate.markChangeAsCommitted();
    }

    @Override
    public AccountAggregate getById(String id) {
        AccountAggregate accountAggregate = new AccountAggregate();
        List<BaseEvent> events = eventStore.getEvents(id);

        if(events != null && !events.isEmpty()) {
            accountAggregate.replayEvents(events);
            Optional<Integer> latestVersion = events.stream().map(BaseEvent::getVersion).max(Comparator.naturalOrder());
            accountAggregate.setVersion(latestVersion.get());
        }
        return accountAggregate;
    }
}
