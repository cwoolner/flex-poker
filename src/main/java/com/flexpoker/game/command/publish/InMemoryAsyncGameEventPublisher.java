package com.flexpoker.game.command.publish;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.Event;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.events.GameJoinedEvent;
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent;
import com.flexpoker.game.command.events.GameStartedEvent;
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent;
import com.flexpoker.game.command.events.NewHandIsClearedToStartEvent;
import com.flexpoker.game.command.framework.GameEventType;

@Component
public class InMemoryAsyncGameEventPublisher implements EventPublisher<GameEventType> {

    private final EventHandler<GameCreatedEvent> gameCreatedEventHandler;

    private final EventHandler<GameJoinedEvent> gameJoinedEventHandler;

    private final EventHandler<GameMovedToStartingStageEvent> gameMovedToStartingStageEventHandler;

    private final EventHandler<GameStartedEvent> gameStartedEventHandler;

    private final ProcessManager<GameTablesCreatedAndPlayersAssociatedEvent> createInitialTablesForGameProcessManager;

    private final ProcessManager<GameStartedEvent> startFirstHandProcessManager;

    private final ProcessManager<NewHandIsClearedToStartEvent> startNewHandForExistingTableProcessManager;

    @Inject
    public InMemoryAsyncGameEventPublisher(
            EventHandler<GameCreatedEvent> gameCreatedEventHandler,
            EventHandler<GameJoinedEvent> gameJoinedEventHandler,
            EventHandler<GameMovedToStartingStageEvent> gameMovedToStartingStageEvent,
            EventHandler<GameStartedEvent> gameStartedEventHandler,
            ProcessManager<GameTablesCreatedAndPlayersAssociatedEvent> createInitialTablesForGameProcessManager,
            ProcessManager<GameStartedEvent> startFirstHandProcessManager,
            ProcessManager<NewHandIsClearedToStartEvent> startNewHandForExistingTableProcessManager) {
        this.gameCreatedEventHandler = gameCreatedEventHandler;
        this.gameJoinedEventHandler = gameJoinedEventHandler;
        this.gameMovedToStartingStageEventHandler = gameMovedToStartingStageEvent;
        this.gameStartedEventHandler = gameStartedEventHandler;
        this.createInitialTablesForGameProcessManager = createInitialTablesForGameProcessManager;
        this.startFirstHandProcessManager = startFirstHandProcessManager;
        this.startNewHandForExistingTableProcessManager = startNewHandForExistingTableProcessManager;
    }

    @Override
    public void publish(Event<GameEventType> event) {
        switch (event.getType()) {
        case GameCreated:
            gameCreatedEventHandler.handle((GameCreatedEvent) event);
            break;
        case GameJoined:
            gameJoinedEventHandler.handle((GameJoinedEvent) event);
            break;
        case GameMovedToStartingStage:
            gameMovedToStartingStageEventHandler
                    .handle((GameMovedToStartingStageEvent) event);
            break;
        case GameTablesCreatedAndPlayersAssociated:
            createInitialTablesForGameProcessManager
                    .handle((GameTablesCreatedAndPlayersAssociatedEvent) event);
            break;
        case GameStarted:
            final EventHandler<GameStartedEvent> gameStartedEventHandler = this.gameStartedEventHandler;
            final ProcessManager<GameStartedEvent> startFirstHandProcessManager = this.startFirstHandProcessManager;
            final Timer timer = new Timer();
            final TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    gameStartedEventHandler.handle((GameStartedEvent) event);
                    startFirstHandProcessManager.handle((GameStartedEvent) event);
                }
            };
            timer.schedule(timerTask, 10000);
            break;
        case GameFinished:
            break;
        case NewHandIsClearedToStart:
            startNewHandForExistingTableProcessManager
                    .handle((NewHandIsClearedToStartEvent) event);
            break;
        default:
            throw new IllegalArgumentException("Event Type cannot be handled: "
                    + event.getType());
        }
    }
}
