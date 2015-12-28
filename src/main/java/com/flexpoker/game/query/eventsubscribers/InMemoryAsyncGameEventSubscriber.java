package com.flexpoker.game.query.eventsubscribers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.event.EventSubscriber;
import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.events.GameJoinedEvent;
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent;
import com.flexpoker.game.command.events.GameStartedEvent;
import com.flexpoker.game.command.framework.GameEvent;

@Component("gameEventSubscriber")
public class InMemoryAsyncGameEventSubscriber
        implements EventSubscriber<GameEvent> {

    private final Map<UUID, List<GameEvent>> listOfGameEventsNeededToProcess;

    private final Map<UUID, Integer> nextExpectedEventVersion;

    private final EventHandler<GameCreatedEvent> gameCreatedEventHandler;

    private final EventHandler<GameJoinedEvent> gameJoinedEventHandler;

    private final EventHandler<GameMovedToStartingStageEvent> gameMovedToStartingStageEventHandler;

    private final EventHandler<GameStartedEvent> gameStartedEventHandler;

    @Inject
    public InMemoryAsyncGameEventSubscriber(
            EventHandler<GameCreatedEvent> gameCreatedEventHandler,
            EventHandler<GameJoinedEvent> gameJoinedEventHandler,
            EventHandler<GameMovedToStartingStageEvent> gameMovedToStartingStageEventHandler,
            EventHandler<GameStartedEvent> gameStartedEventHandler) {
        listOfGameEventsNeededToProcess = new ConcurrentHashMap<>();
        nextExpectedEventVersion = new ConcurrentHashMap<>();
        this.gameCreatedEventHandler = gameCreatedEventHandler;
        this.gameJoinedEventHandler = gameJoinedEventHandler;
        this.gameMovedToStartingStageEventHandler = gameMovedToStartingStageEventHandler;
        this.gameStartedEventHandler = gameStartedEventHandler;
    }

    @Async
    @Override
    public void receive(GameEvent event) {
        listOfGameEventsNeededToProcess.putIfAbsent(event.getAggregateId(),
                new CopyOnWriteArrayList<>());
        nextExpectedEventVersion.putIfAbsent(event.getAggregateId(),
                Integer.valueOf(1));

        if (isExpectedEvent(event)) {
            handleEventAndRunAnyOthers(event);
        } else {
            listOfGameEventsNeededToProcess.get(event.getAggregateId())
                    .add(event);
        }
    }

    private void handleEventAndRunAnyOthers(GameEvent event) {
        handleEvent(event);
        removeEventFromUnhandleList(event);
        incrementNextEventVersion(event);
        handleAnyPreviouslyUnhandledEvents(event);
    }

    private boolean isExpectedEvent(GameEvent event) {
        int expectedEventVersion = nextExpectedEventVersion
                .get(event.getAggregateId()).intValue();
        return expectedEventVersion == event.getVersion();
    }

    private void handleEvent(GameEvent event) {
        if (event.getClass() == GameCreatedEvent.class) {
            gameCreatedEventHandler.handle((GameCreatedEvent) event);
        } else if (event.getClass() == GameJoinedEvent.class) {
            gameJoinedEventHandler.handle((GameJoinedEvent) event);
        } else if (event.getClass() == GameMovedToStartingStageEvent.class) {
            gameMovedToStartingStageEventHandler
                    .handle((GameMovedToStartingStageEvent) event);
        } else if (event.getClass() == GameStartedEvent.class) {
            final EventHandler<GameStartedEvent> localGameStartedEventHandler = this.gameStartedEventHandler;
            final Timer timer = new Timer();
            final TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    localGameStartedEventHandler
                            .handle((GameStartedEvent) event);
                }
            };
            timer.schedule(timerTask, 10000);
        }
    }

    private void removeEventFromUnhandleList(GameEvent event) {
        listOfGameEventsNeededToProcess.get(event.getAggregateId())
                .remove(event);
    }

    private void incrementNextEventVersion(GameEvent event) {
        nextExpectedEventVersion.compute(event.getAggregateId(),
                (eventId, eventVersion) -> eventVersion + 1);
    }

    private void handleAnyPreviouslyUnhandledEvents(GameEvent event) {
        List<GameEvent> unHandledEvents = new ArrayList<>(
                listOfGameEventsNeededToProcess.get(event.getAggregateId()));

        unHandledEvents.forEach(previouslyUnRunEvent -> {
            if (isExpectedEvent(previouslyUnRunEvent)) {
                handleEventAndRunAnyOthers(previouslyUnRunEvent);
            }
        });

    }
}
