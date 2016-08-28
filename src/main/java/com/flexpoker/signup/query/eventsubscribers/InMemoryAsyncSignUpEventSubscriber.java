package com.flexpoker.signup.query.eventsubscribers;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.Event;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.event.subscriber.EventSubscriber;
import com.flexpoker.framework.event.subscriber.InMemoryThreadSafeEventSubscriberHelper;
import com.flexpoker.signup.command.events.NewUserSignedUpEvent;
import com.flexpoker.signup.command.events.SignedUpUserConfirmedEvent;
import com.flexpoker.signup.command.framework.SignUpEvent;

@Component("signUpEventSubscriber")
public class InMemoryAsyncSignUpEventSubscriber implements EventSubscriber<SignUpEvent> {

    private final InMemoryThreadSafeEventSubscriberHelper inMemoryThreadSafeEventSubscriberHelper;

    private final EventHandler<NewUserSignedUpEvent> newUserSignedUpEventHandler;

    private final EventHandler<SignedUpUserConfirmedEvent> signedUpUserConfirmedEventHandler;

    @Inject
    public InMemoryAsyncSignUpEventSubscriber(
            InMemoryThreadSafeEventSubscriberHelper inMemoryThreadSafeEventSubscriberHelper,
            EventHandler<NewUserSignedUpEvent> newUserSignedUpEventHandler,
            EventHandler<SignedUpUserConfirmedEvent> signedUpUserConfirmedEventHandler) {
        this.inMemoryThreadSafeEventSubscriberHelper = inMemoryThreadSafeEventSubscriberHelper;
        this.newUserSignedUpEventHandler = newUserSignedUpEventHandler;
        this.signedUpUserConfirmedEventHandler = signedUpUserConfirmedEventHandler;
        this.inMemoryThreadSafeEventSubscriberHelper.setHandlerMap(createEventHandlerMap());
    }

    @Async
    @Override
    public void receive(SignUpEvent event) {
        inMemoryThreadSafeEventSubscriberHelper.receive(event);
    }

    private Map<Class<? extends Event>, EventHandler<? extends Event>> createEventHandlerMap() {
        Map<Class<? extends Event>, EventHandler<? extends Event>> eventHandlerMap = new HashMap<>();
        eventHandlerMap.put(NewUserSignedUpEvent.class, newUserSignedUpEventHandler);
        eventHandlerMap.put(SignedUpUserConfirmedEvent.class, signedUpUserConfirmedEventHandler);
        return eventHandlerMap;
    }

}
