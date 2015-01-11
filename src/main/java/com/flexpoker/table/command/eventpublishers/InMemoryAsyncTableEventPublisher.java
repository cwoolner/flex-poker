package com.flexpoker.table.command.eventpublishers;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.Event;
import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.framework.event.EventSubscriber;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.events.HandCompletedEvent;
import com.flexpoker.table.command.framework.TableEventType;

@Component
public class InMemoryAsyncTableEventPublisher implements EventPublisher<TableEventType> {

    private final EventSubscriber<TableEventType> tableEventSubscriber;

    private final ProcessManager<ActionOnChangedEvent> actionOnCountdownProcessManager;

    private final ProcessManager<HandCompletedEvent> attemptToStartNewHandForExistingTableProcessManager;

    @Inject
    public InMemoryAsyncTableEventPublisher(
            EventSubscriber<TableEventType> tableEventSubscriber,
            ProcessManager<ActionOnChangedEvent> actionOnCountdownProcessManager,
            ProcessManager<HandCompletedEvent> attemptToStartNewHandForExistingTableProcessManager) {
        this.tableEventSubscriber = tableEventSubscriber;
        this.actionOnCountdownProcessManager = actionOnCountdownProcessManager;
        this.attemptToStartNewHandForExistingTableProcessManager = attemptToStartNewHandForExistingTableProcessManager;
    }

    @Override
    public void publish(Event<TableEventType> event) {
        tableEventSubscriber.receive(event);

        if (event.getType() == TableEventType.ActionOnChanged) {
            actionOnCountdownProcessManager.handle((ActionOnChangedEvent) event);
        } else if (event.getType() == TableEventType.HandCompleted) {
            attemptToStartNewHandForExistingTableProcessManager
                    .handle((HandCompletedEvent) event);
        }
    }
}
