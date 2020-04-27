package com.flexpoker.game.query.eventsubscribers;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.Event;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.event.subscriber.EventSubscriber;
import com.flexpoker.framework.event.subscriber.InMemoryThreadSafeEventSubscriberHelper;
import com.flexpoker.game.command.events.BlindsIncreasedEvent;
import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.events.GameFinishedEvent;
import com.flexpoker.game.command.events.GameJoinedEvent;
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent;
import com.flexpoker.game.command.events.GameStartedEvent;
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent;
import com.flexpoker.game.command.events.NewHandIsClearedToStartEvent;
import com.flexpoker.game.command.events.PlayerBustedGameEvent;
import com.flexpoker.game.command.events.PlayerMovedToNewTableEvent;
import com.flexpoker.game.command.events.TablePausedForBalancingEvent;
import com.flexpoker.game.command.events.TableRemovedEvent;
import com.flexpoker.game.command.events.TableResumedAfterBalancingEvent;
import com.flexpoker.game.command.framework.GameEvent;

@Component("gameEventSubscriber")
public class InMemoryAsyncGameEventSubscriber implements EventSubscriber<GameEvent> {

    private final InMemoryThreadSafeEventSubscriberHelper inMemoryThreadSafeEventSubscriberHelper;

    private final EventHandler<GameCreatedEvent> gameCreatedEventHandler;

    private final EventHandler<GameJoinedEvent> gameJoinedEventHandler;

    private final EventHandler<GameMovedToStartingStageEvent> gameMovedToStartingStageEventHandler;

    private final EventHandler<GameStartedEvent> gameStartedEventHandler;

    private final EventHandler<PlayerBustedGameEvent> playerBustedGameEventHandler;

    private static final EventHandler<Event> NOOP = x -> {};

    @Inject
    public InMemoryAsyncGameEventSubscriber(
            InMemoryThreadSafeEventSubscriberHelper inMemoryThreadSafeEventSubscriberHelper,
            EventHandler<GameCreatedEvent> gameCreatedEventHandler,
            EventHandler<GameJoinedEvent> gameJoinedEventHandler,
            EventHandler<GameMovedToStartingStageEvent> gameMovedToStartingStageEventHandler,
            EventHandler<GameStartedEvent> gameStartedEventHandler,
            EventHandler<PlayerBustedGameEvent> playerBustedGameEventHandler) {
        this.inMemoryThreadSafeEventSubscriberHelper = inMemoryThreadSafeEventSubscriberHelper;
        this.gameCreatedEventHandler = gameCreatedEventHandler;
        this.gameJoinedEventHandler = gameJoinedEventHandler;
        this.gameMovedToStartingStageEventHandler = gameMovedToStartingStageEventHandler;
        this.gameStartedEventHandler = gameStartedEventHandler;
        this.playerBustedGameEventHandler = playerBustedGameEventHandler;
        this.inMemoryThreadSafeEventSubscriberHelper.setHandlerMap(createEventHandlerMap());
    }

    @Async
    @Override
    public void receive(GameEvent event) {
        inMemoryThreadSafeEventSubscriberHelper.receive(event);
    }

    private Map<Class<? extends Event>, EventHandler<? extends Event>> createEventHandlerMap() {
        var eventHandlerMap = new HashMap<Class<? extends Event>, EventHandler<? extends Event>>();
        eventHandlerMap.put(BlindsIncreasedEvent.class, NOOP);
        eventHandlerMap.put(GameCreatedEvent.class, gameCreatedEventHandler);
        eventHandlerMap.put(GameFinishedEvent.class, NOOP);
        eventHandlerMap.put(GameJoinedEvent.class, gameJoinedEventHandler);
        eventHandlerMap.put(GameMovedToStartingStageEvent.class, gameMovedToStartingStageEventHandler);
        eventHandlerMap.put(GameStartedEvent.class, x -> {
            final var timer = new Timer();
            final var timerTask = new TimerTask() {
                @Override
                public void run() {
                    gameStartedEventHandler.handle((GameStartedEvent) x);
                }
            };
            timer.schedule(timerTask, 10000);
        });
        eventHandlerMap.put(GameTablesCreatedAndPlayersAssociatedEvent.class, NOOP);
        eventHandlerMap.put(NewHandIsClearedToStartEvent.class, NOOP);
        eventHandlerMap.put(PlayerBustedGameEvent.class, playerBustedGameEventHandler);
        eventHandlerMap.put(PlayerMovedToNewTableEvent.class, NOOP);
        eventHandlerMap.put(TablePausedForBalancingEvent.class, NOOP);
        eventHandlerMap.put(TableRemovedEvent.class, NOOP);
        eventHandlerMap.put(TableResumedAfterBalancingEvent.class, NOOP);
        return eventHandlerMap;
    }

}
