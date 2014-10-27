package com.flexpoker.login.command.publish;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.Event;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.login.command.events.LoginUserCreatedEvent;
import com.flexpoker.login.command.framework.LoginEventType;

@Component
public class InMemoryAsyncLoginEventPublisher implements EventPublisher<LoginEventType> {

    private final EventHandler<LoginUserCreatedEvent> loginUserCreatedEventHandler;

    @Inject
    public InMemoryAsyncLoginEventPublisher(
            EventHandler<LoginUserCreatedEvent> loginUserCreatedEventHandler) {
        this.loginUserCreatedEventHandler = loginUserCreatedEventHandler;
    }

    @Override
    public void publish(Event<LoginEventType> event) {
        switch (event.getType()) {
        case LoginUserCreated:
            loginUserCreatedEventHandler.handle((LoginUserCreatedEvent) event);
            break;
        default:
            throw new IllegalArgumentException("Event Type cannot be handled: "
                    + event.getType());
        }

    }

}
