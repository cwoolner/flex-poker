package com.flexpoker.signup.command.eventpublishers;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.framework.event.EventSubscriber;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.signup.command.events.SignedUpUserConfirmedEvent;
import com.flexpoker.signup.command.framework.SignUpEvent;

@Component
public class InMemoryAsyncSignUpEventPublisher
        implements EventPublisher<SignUpEvent> {

    private final EventSubscriber<SignUpEvent> signUpEventSubscriber;

    private final ProcessManager<SignedUpUserConfirmedEvent> newUserProcessManager;

    @Inject
    public InMemoryAsyncSignUpEventPublisher(
            EventSubscriber<SignUpEvent> signUpEventSubscriber,
            ProcessManager<SignedUpUserConfirmedEvent> newUserProcessManager) {
        this.signUpEventSubscriber = signUpEventSubscriber;
        this.newUserProcessManager = newUserProcessManager;
    }

    @Override
    public void publish(SignUpEvent event) {
        signUpEventSubscriber.receive(event);

        if (event.getClass() == SignedUpUserConfirmedEvent.class) {
            newUserProcessManager.handle((SignedUpUserConfirmedEvent) event);
        }
    }

}
