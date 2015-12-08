package com.flexpoker.login.query.eventsubscribers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.Event;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.event.EventSubscriber;
import com.flexpoker.login.command.events.LoginUserCreatedEvent;
import com.flexpoker.login.command.framework.LoginEventType;

@Component
public class InMemoryAsyncLoginEventSubscriber implements EventSubscriber<LoginEventType> {

    private final Map<UUID, List<Event<LoginEventType>>> listOfLoginEventsNeededToProcess;

    private final Map<UUID, Integer> nextExpectedEventVersion;

    private final EventHandler<LoginUserCreatedEvent> loginUserCreatedEventHandler;

    @Inject
    public InMemoryAsyncLoginEventSubscriber(
            EventHandler<LoginUserCreatedEvent> loginUserCreatedEventHandler) {
        listOfLoginEventsNeededToProcess = new ConcurrentHashMap<>();
        nextExpectedEventVersion = new ConcurrentHashMap<>();
        this.loginUserCreatedEventHandler = loginUserCreatedEventHandler;
    }

    @Async
    @Override
    public void receive(Event<LoginEventType> event) {
        listOfLoginEventsNeededToProcess.putIfAbsent(event.getAggregateId(),
                new CopyOnWriteArrayList<>());
        nextExpectedEventVersion.putIfAbsent(event.getAggregateId(), Integer.valueOf(1));

        if (isExpectedEvent(event)) {
            handleEventAndRunAnyOthers(event);
        } else {
            listOfLoginEventsNeededToProcess.get(event.getAggregateId()).add(event);
        }
    }

    private void handleEventAndRunAnyOthers(Event<LoginEventType> event) {
        handleEvent(event);
        removeEventFromUnhandleList(event);
        incrementNextEventVersion(event);
        handleAnyPreviouslyUnhandledEvents(event);
    }

    private boolean isExpectedEvent(Event<LoginEventType> event) {
        int expectedEventVersion = nextExpectedEventVersion.get(event.getAggregateId())
                .intValue();
        return expectedEventVersion == event.getVersion();
    }

    private void handleEvent(Event<LoginEventType> event) {
        switch (event.getType()) {
        case LoginUserCreated:
            loginUserCreatedEventHandler.handle((LoginUserCreatedEvent) event);
            break;
        default:
            throw new IllegalArgumentException("Event Type cannot be handled: "
                    + event.getType());
        }
    }

    private void removeEventFromUnhandleList(Event<LoginEventType> event) {
        listOfLoginEventsNeededToProcess.get(event.getAggregateId()).remove(event);
    }

    private void incrementNextEventVersion(Event<LoginEventType> event) {
        nextExpectedEventVersion.compute(event.getAggregateId(),
                (eventId, eventVersion) -> eventVersion + 1);
    }

    private void handleAnyPreviouslyUnhandledEvents(Event<LoginEventType> event) {
        List<Event<LoginEventType>> unHandledEvents = new ArrayList<>(
                listOfLoginEventsNeededToProcess.get(event.getAggregateId()));

        unHandledEvents.forEach(previouslyUnRunEvent -> {
            if (isExpectedEvent(previouslyUnRunEvent)) {
                handleEventAndRunAnyOthers(previouslyUnRunEvent);
            }
        });

    }
}
