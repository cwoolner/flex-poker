package com.flexpoker.login.query.eventsubscribers;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.Event;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.event.subscriber.EventSubscriber;
import com.flexpoker.framework.event.subscriber.InMemoryThreadSafeEventSubscriberHelper;
import com.flexpoker.login.command.events.LoginUserCreatedEvent;
import com.flexpoker.login.command.framework.LoginEvent;

@Component("loginEventSubscriber")
public class InMemoryAsyncLoginEventSubscriber implements EventSubscriber<LoginEvent> {

    private final InMemoryThreadSafeEventSubscriberHelper inMemoryThreadSafeEventSubscriberHelper;

    private final EventHandler<LoginUserCreatedEvent> loginUserCreatedEventHandler;

    @Inject
    public InMemoryAsyncLoginEventSubscriber(
            InMemoryThreadSafeEventSubscriberHelper inMemoryThreadSafeEventSubscriberHelper,
            EventHandler<LoginUserCreatedEvent> loginUserCreatedEventHandler) {
        this.inMemoryThreadSafeEventSubscriberHelper = inMemoryThreadSafeEventSubscriberHelper;
        this.loginUserCreatedEventHandler = loginUserCreatedEventHandler;
        this.inMemoryThreadSafeEventSubscriberHelper.setHandlerMap(createEventHandlerMap());
    }

    @Async
    @Override
    public void receive(LoginEvent event) {
        inMemoryThreadSafeEventSubscriberHelper.receive(event);
    }

    private Map<Class<? extends Event>, EventHandler<? extends Event>> createEventHandlerMap() {
        Map<Class<? extends Event>, EventHandler<? extends Event>> eventHandlerMap = new HashMap<>();
        eventHandlerMap.put(LoginUserCreatedEvent.class, loginUserCreatedEventHandler);
        return eventHandlerMap;
    }

}
