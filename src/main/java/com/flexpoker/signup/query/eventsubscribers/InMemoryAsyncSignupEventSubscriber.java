package com.flexpoker.signup.query.eventsubscribers;

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
import com.flexpoker.signup.command.events.NewUserSignedUpEvent;
import com.flexpoker.signup.command.events.SignedUpUserConfirmedEvent;
import com.flexpoker.signup.command.framework.SignUpEventType;

@Component
public class InMemoryAsyncSignupEventSubscriber
        implements EventSubscriber<SignUpEventType> {

    private final Map<UUID, List<Event<SignUpEventType>>> listOfEventsNeededToProcess;

    private final Map<UUID, Integer> nextExpectedEventVersion;

    private final EventHandler<NewUserSignedUpEvent> newUserSignedUpEventHandler;

    private final EventHandler<SignedUpUserConfirmedEvent> signedUpUserConfirmedEventHandler;

    @Inject
    public InMemoryAsyncSignupEventSubscriber(
            EventHandler<NewUserSignedUpEvent> newUserSignedUpEventHandler,
            EventHandler<SignedUpUserConfirmedEvent> signedUpUserConfirmedEventHandler) {
        listOfEventsNeededToProcess = new ConcurrentHashMap<>();
        nextExpectedEventVersion = new ConcurrentHashMap<>();
        this.newUserSignedUpEventHandler = newUserSignedUpEventHandler;
        this.signedUpUserConfirmedEventHandler = signedUpUserConfirmedEventHandler;
    }

    @Async
    @Override
    public void receive(Event<SignUpEventType> event) {
        listOfEventsNeededToProcess.putIfAbsent(event.getAggregateId(),
                new CopyOnWriteArrayList<>());
        nextExpectedEventVersion.putIfAbsent(event.getAggregateId(),
                Integer.valueOf(1));

        if (isExpectedEvent(event)) {
            handleEventAndRunAnyOthers(event);
        } else {
            listOfEventsNeededToProcess.get(event.getAggregateId()).add(event);
        }
    }

    private void handleEventAndRunAnyOthers(Event<SignUpEventType> event) {
        handleEvent(event);
        removeEventFromUnhandleList(event);
        incrementNextEventVersion(event);
        handleAnyPreviouslyUnhandledEvents(event);
    }

    private boolean isExpectedEvent(Event<SignUpEventType> event) {
        int expectedEventVersion = nextExpectedEventVersion
                .get(event.getAggregateId()).intValue();
        return expectedEventVersion == event.getVersion();
    }

    private void handleEvent(Event<SignUpEventType> event) {
        switch (event.getType()) {
        case NewUserSignedUp:
            newUserSignedUpEventHandler.handle((NewUserSignedUpEvent) event);
            break;
        case SignedUpUserConfirmed:
            signedUpUserConfirmedEventHandler
                    .handle((SignedUpUserConfirmedEvent) event);
            break;
        default:
            throw new IllegalArgumentException(
                    "Event Type cannot be handled: " + event.getType());
        }
    }

    private void removeEventFromUnhandleList(Event<SignUpEventType> event) {
        listOfEventsNeededToProcess.get(event.getAggregateId()).remove(event);
    }

    private void incrementNextEventVersion(Event<SignUpEventType> event) {
        nextExpectedEventVersion.compute(event.getAggregateId(),
                (eventId, eventVersion) -> eventVersion + 1);
    }

    private void handleAnyPreviouslyUnhandledEvents(
            Event<SignUpEventType> event) {
        List<Event<SignUpEventType>> unHandledEvents = new ArrayList<>(
                listOfEventsNeededToProcess.get(event.getAggregateId()));

        unHandledEvents.forEach(previouslyUnRunEvent -> {
            if (isExpectedEvent(previouslyUnRunEvent)) {
                handleEventAndRunAnyOthers(previouslyUnRunEvent);
            }
        });

    }
}
