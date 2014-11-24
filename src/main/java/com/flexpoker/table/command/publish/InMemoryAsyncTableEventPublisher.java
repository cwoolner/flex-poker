package com.flexpoker.table.command.publish;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.Event;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.table.command.framework.TableEventType;

@Component
public class InMemoryAsyncTableEventPublisher implements EventPublisher<TableEventType> {

    private final EventHandler<TableCreatedEvent> tableCreatedEventHandler;

    @Inject
    public InMemoryAsyncTableEventPublisher(
            EventHandler<TableCreatedEvent> tableCreatedEventHandler) {
        this.tableCreatedEventHandler = tableCreatedEventHandler;
    }

    @Override
    public void publish(Event<TableEventType> event) {
        switch (event.getType()) {
        case TableCreated:
            tableCreatedEventHandler.handle((TableCreatedEvent) event);
            break;
        default:
            throw new IllegalArgumentException("Event Type cannot be handled: "
                    + event.getType());
        }
    }
}
