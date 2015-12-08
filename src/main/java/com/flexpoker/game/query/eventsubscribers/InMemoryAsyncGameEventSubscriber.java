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

import com.flexpoker.framework.event.Event;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.event.EventSubscriber;
import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.events.GameJoinedEvent;
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent;
import com.flexpoker.game.command.events.GameStartedEvent;
import com.flexpoker.game.command.framework.GameEventType;

@Component
public class InMemoryAsyncGameEventSubscriber
        implements EventSubscriber<GameEventType> {

    private final Map<UUID, List<Event<GameEventType>>> listOfGameEventsNeededToProcess;

    private final Map<UUID, Integer> nextExpectedEventVersion;

    private final EventHandler<GameCreatedEvent> gameCreatedEventHandler;

    private final EventHandler<GameJoinedEvent> gameJoinedEventHandler;

    private final EventHandler<GameMovedToStartingStageEvent> gameMovedToStartingStageEventHandler;

    private final EventHandler<GameStartedEvent> gameStartedEventHandler;

    @Inject
    public InMemoryAsyncGameEventSubscriber(
            EventHandler<GameCreatedEvent> gameCreatedEventHandler,
            EventHandler<GameJoinedEvent> gameJoinedEventHandler,
            EventHandler<GameMovedToStartingStageEvent> gameMovedToStartingStageEvent,
            EventHandler<GameStartedEvent> gameStartedEventHandler) {
        listOfGameEventsNeededToProcess = new ConcurrentHashMap<>();
        nextExpectedEventVersion = new ConcurrentHashMap<>();
        this.gameCreatedEventHandler = gameCreatedEventHandler;
        this.gameJoinedEventHandler = gameJoinedEventHandler;
        this.gameMovedToStartingStageEventHandler = gameMovedToStartingStageEvent;
        this.gameStartedEventHandler = gameStartedEventHandler;
    }

    @Async
    @Override
    public void receive(Event<GameEventType> event) {
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

    private void handleEventAndRunAnyOthers(Event<GameEventType> event) {
        handleEvent(event);
        removeEventFromUnhandleList(event);
        incrementNextEventVersion(event);
        handleAnyPreviouslyUnhandledEvents(event);
    }

    private boolean isExpectedEvent(Event<GameEventType> event) {
        int expectedEventVersion = nextExpectedEventVersion
                .get(event.getAggregateId()).intValue();
        return expectedEventVersion == event.getVersion();
    }

    private void handleEvent(Event<GameEventType> event) {
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
            break;
        case GameStarted:
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
            break;
        case GameFinished:
            break;
        case NewHandIsClearedToStart:
            break;
        default:
            throw new IllegalArgumentException(
                    "Event Type cannot be handled: " + event.getType());
        }
    }

    private void removeEventFromUnhandleList(Event<GameEventType> event) {
        listOfGameEventsNeededToProcess.get(event.getAggregateId())
                .remove(event);
    }

    private void incrementNextEventVersion(Event<GameEventType> event) {
        nextExpectedEventVersion.compute(event.getAggregateId(),
                (eventId, eventVersion) -> eventVersion + 1);
    }

    private void handleAnyPreviouslyUnhandledEvents(
            Event<GameEventType> event) {
        List<Event<GameEventType>> unHandledEvents = new ArrayList<>(
                listOfGameEventsNeededToProcess.get(event.getAggregateId()));

        unHandledEvents.forEach(previouslyUnRunEvent -> {
            if (isExpectedEvent(previouslyUnRunEvent)) {
                handleEventAndRunAnyOthers(previouslyUnRunEvent);
            }
        });

    }
}
