package com.flexpoker.game.command.eventpublishers;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.Event;
import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.framework.event.EventSubscriber;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.events.GameStartedEvent;
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent;
import com.flexpoker.game.command.events.NewHandIsClearedToStartEvent;
import com.flexpoker.game.command.framework.GameEventType;

@Component
public class InMemoryAsyncGameEventPublisher
        implements EventPublisher<GameEventType> {

    private final EventSubscriber<GameEventType> gameEventSubscriber;

    private final ProcessManager<GameTablesCreatedAndPlayersAssociatedEvent> createInitialTablesForGameProcessManager;

    private final ProcessManager<GameStartedEvent> startFirstHandProcessManager;

    private final ProcessManager<NewHandIsClearedToStartEvent> startNewHandForExistingTableProcessManager;

    @Inject
    public InMemoryAsyncGameEventPublisher(
            EventSubscriber<GameEventType> gameEventSubscriber,
            ProcessManager<GameTablesCreatedAndPlayersAssociatedEvent> createInitialTablesForGameProcessManager,
            ProcessManager<GameStartedEvent> startFirstHandProcessManager,
            ProcessManager<NewHandIsClearedToStartEvent> startNewHandForExistingTableProcessManager) {
        this.gameEventSubscriber = gameEventSubscriber;
        this.createInitialTablesForGameProcessManager = createInitialTablesForGameProcessManager;
        this.startFirstHandProcessManager = startFirstHandProcessManager;
        this.startNewHandForExistingTableProcessManager = startNewHandForExistingTableProcessManager;
    }

    @Override
    public void publish(Event<GameEventType> event) {
        gameEventSubscriber.receive(event);

        if (event
                .getType() == GameEventType.GameTablesCreatedAndPlayersAssociated) {
            createInitialTablesForGameProcessManager
                    .handle((GameTablesCreatedAndPlayersAssociatedEvent) event);
        } else if (event.getType() == GameEventType.GameStarted) {
            final ProcessManager<GameStartedEvent> localStartFirstHandProcessManager = this.startFirstHandProcessManager;
            final Timer timer = new Timer();
            final TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    localStartFirstHandProcessManager
                            .handle((GameStartedEvent) event);
                }
            };
            timer.schedule(timerTask, 10000);
        } else if (event.getType() == GameEventType.NewHandIsClearedToStart) {
            startNewHandForExistingTableProcessManager
                    .handle((NewHandIsClearedToStartEvent) event);
        }
    }
}
