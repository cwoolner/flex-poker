package com.flexpoker.signup.command.eventpublishers;

import static com.flexpoker.signup.command.framework.SignUpEventType.SignedUpUserConfirmed;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.Event;
import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.framework.event.EventSubscriber;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.signup.command.events.SignedUpUserConfirmedEvent;
import com.flexpoker.signup.command.framework.SignUpEventType;

@Component
public class InMemoryAsyncSignUpEventPublisher
        implements EventPublisher<SignUpEventType> {

    private final EventSubscriber<SignUpEventType> signUpEventSubscriber;

    private final ProcessManager<SignedUpUserConfirmedEvent> newUserProcessManager;

    @Inject
    public InMemoryAsyncSignUpEventPublisher(
            EventSubscriber<SignUpEventType> signUpEventSubscriber,
            ProcessManager<SignedUpUserConfirmedEvent> newUserProcessManager) {
        this.signUpEventSubscriber = signUpEventSubscriber;
        this.newUserProcessManager = newUserProcessManager;
    }

    @Override
    public void publish(Event<SignUpEventType> event) {
        signUpEventSubscriber.receive(event);

        if (event.getType() == SignedUpUserConfirmed) {
            newUserProcessManager.handle((SignedUpUserConfirmedEvent) event);
        }

    }

}
