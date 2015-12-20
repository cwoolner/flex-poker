package com.flexpoker.game.command.eventpublishers;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.framework.event.EventSubscriber;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.events.GameStartedEvent;
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent;
import com.flexpoker.game.command.events.NewHandIsClearedToStartEvent;
import com.flexpoker.game.command.framework.GameEvent;

@Component
public class InMemoryAsyncGameEventPublisher
        implements EventPublisher<GameEvent> {

    private final EventSubscriber<GameEvent> gameEventSubscriber;

    private final ProcessManager<GameTablesCreatedAndPlayersAssociatedEvent> createInitialTablesForGameProcessManager;

    private final ProcessManager<GameStartedEvent> startFirstHandProcessManager;

    private final ProcessManager<NewHandIsClearedToStartEvent> startNewHandForExistingTableProcessManager;

    @Inject
    public InMemoryAsyncGameEventPublisher(
            EventSubscriber<GameEvent> gameEventSubscriber,
            ProcessManager<GameTablesCreatedAndPlayersAssociatedEvent> createInitialTablesForGameProcessManager,
            ProcessManager<GameStartedEvent> startFirstHandProcessManager,
            ProcessManager<NewHandIsClearedToStartEvent> startNewHandForExistingTableProcessManager) {
        this.gameEventSubscriber = gameEventSubscriber;
        this.createInitialTablesForGameProcessManager = createInitialTablesForGameProcessManager;
        this.startFirstHandProcessManager = startFirstHandProcessManager;
        this.startNewHandForExistingTableProcessManager = startNewHandForExistingTableProcessManager;
    }

    @Override
    public void publish(GameEvent event) {
        gameEventSubscriber.receive(event);

        if (event
                .getClass() == GameTablesCreatedAndPlayersAssociatedEvent.class) {
            createInitialTablesForGameProcessManager
                    .handle((GameTablesCreatedAndPlayersAssociatedEvent) event);
        } else if (event.getClass() == GameStartedEvent.class) {
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
        } else if (event.getClass() == NewHandIsClearedToStartEvent.class) {
            startNewHandForExistingTableProcessManager
                    .handle((NewHandIsClearedToStartEvent) event);
        }
    }
}
