package com.flexpoker.table.command.eventpublishers;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.framework.event.EventSubscriber;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.events.HandCompletedEvent;
import com.flexpoker.table.command.framework.TableEvent;

@Component
public class InMemoryAsyncTableEventPublisher implements EventPublisher<TableEvent> {

    private final EventSubscriber<TableEvent> tableEventSubscriber;

    private final ProcessManager<ActionOnChangedEvent> actionOnCountdownProcessManager;

    private final ProcessManager<HandCompletedEvent> attemptToStartNewHandForExistingTableProcessManager;

    @Inject
    public InMemoryAsyncTableEventPublisher(
            EventSubscriber<TableEvent> tableEventSubscriber,
            ProcessManager<ActionOnChangedEvent> actionOnCountdownProcessManager,
            ProcessManager<HandCompletedEvent> attemptToStartNewHandForExistingTableProcessManager) {
        this.tableEventSubscriber = tableEventSubscriber;
        this.actionOnCountdownProcessManager = actionOnCountdownProcessManager;
        this.attemptToStartNewHandForExistingTableProcessManager = attemptToStartNewHandForExistingTableProcessManager;
    }

    @Override
    public void publish(TableEvent event) {
        tableEventSubscriber.receive(event);

        if (event.getClass() == ActionOnChangedEvent.class) {
            actionOnCountdownProcessManager.handle((ActionOnChangedEvent) event);
        } else if (event.getClass() == HandCompletedEvent.class) {
            attemptToStartNewHandForExistingTableProcessManager
                    .handle((HandCompletedEvent) event);
        }
    }
}
