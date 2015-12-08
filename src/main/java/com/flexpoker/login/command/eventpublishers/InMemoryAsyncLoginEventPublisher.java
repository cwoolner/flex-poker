package com.flexpoker.login.command.eventpublishers;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.Event;
import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.framework.event.EventSubscriber;
import com.flexpoker.login.command.framework.LoginEventType;

@Component
public class InMemoryAsyncLoginEventPublisher
        implements EventPublisher<LoginEventType> {

    private final EventSubscriber<LoginEventType> loginEventSubscriber;

    @Inject
    public InMemoryAsyncLoginEventPublisher(
            EventSubscriber<LoginEventType> loginEventSubscriber) {
        this.loginEventSubscriber = loginEventSubscriber;
    }

    @Override
    public void publish(Event<LoginEventType> event) {
        loginEventSubscriber.receive(event);
    }

}
