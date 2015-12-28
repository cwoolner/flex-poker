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

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.event.EventSubscriber;
import com.flexpoker.signup.command.events.NewUserSignedUpEvent;
import com.flexpoker.signup.command.events.SignedUpUserConfirmedEvent;
import com.flexpoker.signup.command.framework.SignUpEvent;

@Component("signUpEventSubscriber")
public class InMemoryAsyncSignUpEventSubscriber
        implements EventSubscriber<SignUpEvent> {

    private final Map<UUID, List<SignUpEvent>> listOfEventsNeededToProcess;

    private final Map<UUID, Integer> nextExpectedEventVersion;

    private final EventHandler<NewUserSignedUpEvent> newUserSignedUpEventHandler;

    private final EventHandler<SignedUpUserConfirmedEvent> signedUpUserConfirmedEventHandler;

    @Inject
    public InMemoryAsyncSignUpEventSubscriber(
            EventHandler<NewUserSignedUpEvent> newUserSignedUpEventHandler,
            EventHandler<SignedUpUserConfirmedEvent> signedUpUserConfirmedEventHandler) {
        listOfEventsNeededToProcess = new ConcurrentHashMap<>();
        nextExpectedEventVersion = new ConcurrentHashMap<>();
        this.newUserSignedUpEventHandler = newUserSignedUpEventHandler;
        this.signedUpUserConfirmedEventHandler = signedUpUserConfirmedEventHandler;
    }

    @Async
    @Override
    public void receive(SignUpEvent event) {
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

    private void handleEventAndRunAnyOthers(SignUpEvent event) {
        handleEvent(event);
        removeEventFromUnhandleList(event);
        incrementNextEventVersion(event);
        handleAnyPreviouslyUnhandledEvents(event);
    }

    private boolean isExpectedEvent(SignUpEvent event) {
        int expectedEventVersion = nextExpectedEventVersion
                .get(event.getAggregateId()).intValue();
        return expectedEventVersion == event.getVersion();
    }

    private void handleEvent(SignUpEvent event) {
        if (event.getClass() == NewUserSignedUpEvent.class) {
            newUserSignedUpEventHandler.handle((NewUserSignedUpEvent) event);
        } else if (event.getClass() == SignedUpUserConfirmedEvent.class) {
            signedUpUserConfirmedEventHandler
                    .handle((SignedUpUserConfirmedEvent) event);
        }
    }

    private void removeEventFromUnhandleList(SignUpEvent event) {
        listOfEventsNeededToProcess.get(event.getAggregateId()).remove(event);
    }

    private void incrementNextEventVersion(SignUpEvent event) {
        nextExpectedEventVersion.compute(event.getAggregateId(),
                (eventId, eventVersion) -> eventVersion + 1);
    }

    private void handleAnyPreviouslyUnhandledEvents(
            SignUpEvent event) {
        List<SignUpEvent> unHandledEvents = new ArrayList<>(
                listOfEventsNeededToProcess.get(event.getAggregateId()));

        unHandledEvents.forEach(previouslyUnRunEvent -> {
            if (isExpectedEvent(previouslyUnRunEvent)) {
                handleEventAndRunAnyOthers(previouslyUnRunEvent);
            }
        });

    }
}
