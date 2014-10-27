package com.flexpoker.signup.command.publish;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.Event;
import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.signup.command.events.NewUserSignedUpEvent;
import com.flexpoker.signup.command.events.SignedUpUserConfirmedEvent;
import com.flexpoker.signup.command.framework.SignUpEventType;

@Component
public class InMemoryAsyncSignUpEventPublisher implements EventPublisher<SignUpEventType> {

    private final EventHandler<NewUserSignedUpEvent> newUserSignedUpEventHandler;

    private final EventHandler<SignedUpUserConfirmedEvent> signedUpUserConfirmedEventHandler;

    private final ProcessManager<SignedUpUserConfirmedEvent> newUserProcessManager;

    @Inject
    public InMemoryAsyncSignUpEventPublisher(
            EventHandler<NewUserSignedUpEvent> newUserSignedUpEventHandler,
            EventHandler<SignedUpUserConfirmedEvent> signedUpUserConfirmedEventHandler,
            ProcessManager<SignedUpUserConfirmedEvent> newUserProcessManager) {
        this.newUserSignedUpEventHandler = newUserSignedUpEventHandler;
        this.signedUpUserConfirmedEventHandler = signedUpUserConfirmedEventHandler;
        this.newUserProcessManager = newUserProcessManager;
    }

    @Override
    public void publish(Event<SignUpEventType> event) {
        switch (event.getType()) {
        case NewUserSignedUp:
            newUserSignedUpEventHandler.handle((NewUserSignedUpEvent) event);
            break;
        case SignedUpUserConfirmed:
            signedUpUserConfirmedEventHandler.handle((SignedUpUserConfirmedEvent) event);
            newUserProcessManager.handle((SignedUpUserConfirmedEvent) event);
            break;
        default:
            throw new IllegalArgumentException("Event Type cannot be handled: "
                    + event.getType());
        }

    }

}
