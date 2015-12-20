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

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.event.EventSubscriber;
import com.flexpoker.login.command.events.LoginUserCreatedEvent;
import com.flexpoker.login.command.framework.LoginEvent;

@Component
public class InMemoryAsyncLoginEventSubscriber implements EventSubscriber<LoginEvent> {

    private final Map<UUID, List<LoginEvent>> listOfLoginEventsNeededToProcess;

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
    public void receive(LoginEvent event) {
        listOfLoginEventsNeededToProcess.putIfAbsent(event.getAggregateId(),
                new CopyOnWriteArrayList<>());
        nextExpectedEventVersion.putIfAbsent(event.getAggregateId(), Integer.valueOf(1));

        if (isExpectedEvent(event)) {
            handleEventAndRunAnyOthers(event);
        } else {
            listOfLoginEventsNeededToProcess.get(event.getAggregateId()).add(event);
        }
    }

    private void handleEventAndRunAnyOthers(LoginEvent event) {
        handleEvent(event);
        removeEventFromUnhandleList(event);
        incrementNextEventVersion(event);
        handleAnyPreviouslyUnhandledEvents(event);
    }

    private boolean isExpectedEvent(LoginEvent event) {
        int expectedEventVersion = nextExpectedEventVersion.get(event.getAggregateId())
                .intValue();
        return expectedEventVersion == event.getVersion();
    }

    private void handleEvent(LoginEvent event) {
        if (event.getClass() == LoginUserCreatedEvent.class) {
            loginUserCreatedEventHandler.handle((LoginUserCreatedEvent) event);
        }
    }

    private void removeEventFromUnhandleList(LoginEvent event) {
        listOfLoginEventsNeededToProcess.get(event.getAggregateId()).remove(event);
    }

    private void incrementNextEventVersion(LoginEvent event) {
        nextExpectedEventVersion.compute(event.getAggregateId(),
                (eventId, eventVersion) -> eventVersion + 1);
    }

    private void handleAnyPreviouslyUnhandledEvents(LoginEvent event) {
        List<LoginEvent> unHandledEvents = new ArrayList<>(
                listOfLoginEventsNeededToProcess.get(event.getAggregateId()));

        unHandledEvents.forEach(previouslyUnRunEvent -> {
            if (isExpectedEvent(previouslyUnRunEvent)) {
                handleEventAndRunAnyOthers(previouslyUnRunEvent);
            }
        });

    }
}
