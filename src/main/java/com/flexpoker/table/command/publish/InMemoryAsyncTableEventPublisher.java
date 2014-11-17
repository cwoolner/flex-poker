package com.flexpoker.table.command.publish;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.Event;
import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.table.command.framework.TableEventType;

@Component
public class InMemoryAsyncTableEventPublisher implements EventPublisher<TableEventType> {

    // @Inject
    public InMemoryAsyncTableEventPublisher() {
    }

    @Override
    public void publish(Event<TableEventType> event) {
        switch (event.getType()) {
        default:
            throw new IllegalArgumentException("Event Type cannot be handled: "
                    + event.getType());
        }
    }
}
