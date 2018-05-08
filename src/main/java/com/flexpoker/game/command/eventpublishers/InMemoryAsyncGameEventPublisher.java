package com.flexpoker.game.command.eventpublishers;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.framework.event.subscriber.EventSubscriber;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.events.GameStartedEvent;
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent;
import com.flexpoker.game.command.events.NewHandIsClearedToStartEvent;
import com.flexpoker.game.command.events.PlayerMovedToNewTableEvent;
import com.flexpoker.game.command.events.TablePausedForBalancingEvent;
import com.flexpoker.game.command.events.TableResumedAfterBalancingEvent;
import com.flexpoker.game.command.framework.GameEvent;

@Component
public class InMemoryAsyncGameEventPublisher implements EventPublisher<GameEvent> {

    private final EventSubscriber<GameEvent> gameEventSubscriber;

    private final ProcessManager<GameTablesCreatedAndPlayersAssociatedEvent> createInitialTablesForGameProcessManager;

    private final ProcessManager<GameStartedEvent> startFirstHandProcessManager;

    private final ProcessManager<NewHandIsClearedToStartEvent> startNewHandForExistingTableProcessManager;

    private final ProcessManager<GameStartedEvent> incrementBlindsCountdownProcessManager;

    private final ProcessManager<PlayerMovedToNewTableEvent> movePlayerBetweenTablesProcessManager;

    private final ProcessManager<TablePausedForBalancingEvent> pauseTableProcessManager;

    private final ProcessManager<TableResumedAfterBalancingEvent> resumeTableProcessManager;

    @Inject
    public InMemoryAsyncGameEventPublisher(
            EventSubscriber<GameEvent> gameEventSubscriber,
            ProcessManager<GameTablesCreatedAndPlayersAssociatedEvent> createInitialTablesForGameProcessManager,
            ProcessManager<GameStartedEvent> startFirstHandProcessManager,
            ProcessManager<NewHandIsClearedToStartEvent> startNewHandForExistingTableProcessManager,
            ProcessManager<GameStartedEvent> incrementBlindsCountdownProcessManager,
            ProcessManager<PlayerMovedToNewTableEvent> movePlayerBetweenTablesProcessManager,
            ProcessManager<TablePausedForBalancingEvent> pauseTableProcessManager,
            ProcessManager<TableResumedAfterBalancingEvent> resumeTableProcessManager) {
        this.gameEventSubscriber = gameEventSubscriber;
        this.createInitialTablesForGameProcessManager = createInitialTablesForGameProcessManager;
        this.startFirstHandProcessManager = startFirstHandProcessManager;
        this.startNewHandForExistingTableProcessManager = startNewHandForExistingTableProcessManager;
        this.incrementBlindsCountdownProcessManager = incrementBlindsCountdownProcessManager;
        this.movePlayerBetweenTablesProcessManager = movePlayerBetweenTablesProcessManager;
        this.pauseTableProcessManager = pauseTableProcessManager;
        this.resumeTableProcessManager = resumeTableProcessManager;
    }

    @Override
    public void publish(GameEvent event) {
        gameEventSubscriber.receive(event);

        if (event
                .getClass() == GameTablesCreatedAndPlayersAssociatedEvent.class) {
            createInitialTablesForGameProcessManager
                    .handle((GameTablesCreatedAndPlayersAssociatedEvent) event);
        } else if (event.getClass() == GameStartedEvent.class) {
            final var localStartFirstHandProcessManager = this.startFirstHandProcessManager;
            final var timer = new Timer();
            final var timerTask = new TimerTask() {
                @Override
                public void run() {
                    localStartFirstHandProcessManager.handle((GameStartedEvent) event);
                }
            };
            timer.schedule(timerTask, 10000);

            incrementBlindsCountdownProcessManager.handle((GameStartedEvent) event);
        } else if (event.getClass() == NewHandIsClearedToStartEvent.class) {
            startNewHandForExistingTableProcessManager
                    .handle((NewHandIsClearedToStartEvent) event);
        } else if (event.getClass() == PlayerMovedToNewTableEvent.class) {
            movePlayerBetweenTablesProcessManager
                    .handle((PlayerMovedToNewTableEvent) event);
        } else if (event.getClass() == TablePausedForBalancingEvent.class) {
            pauseTableProcessManager
                    .handle((TablePausedForBalancingEvent) event);
        } else if (event.getClass() == TableResumedAfterBalancingEvent.class) {
            resumeTableProcessManager
                    .handle((TableResumedAfterBalancingEvent) event);
        }
    }
}
