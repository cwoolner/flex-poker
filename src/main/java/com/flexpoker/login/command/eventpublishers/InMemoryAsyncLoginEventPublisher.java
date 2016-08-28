package com.flexpoker.login.command.eventpublishers;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.framework.event.subscriber.EventSubscriber;
import com.flexpoker.login.command.framework.LoginEvent;

@Component
public class InMemoryAsyncLoginEventPublisher
        implements EventPublisher<LoginEvent> {

    private final EventSubscriber<LoginEvent> loginEventSubscriber;

    @Inject
    public InMemoryAsyncLoginEventPublisher(
            EventSubscriber<LoginEvent> loginEventSubscriber) {
        this.loginEventSubscriber = loginEventSubscriber;
    }

    @Override
    public void publish(LoginEvent event) {
        loginEventSubscriber.receive(event);
    }

}
