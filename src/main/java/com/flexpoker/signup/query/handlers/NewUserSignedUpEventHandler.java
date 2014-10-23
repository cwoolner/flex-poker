package com.flexpoker.signup.query.handlers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.signup.command.events.NewUserSignedUpEvent;
import com.flexpoker.signup.query.repository.SignUpRepository;

@Component
public class NewUserSignedUpEventHandler implements EventHandler<NewUserSignedUpEvent> {

    private final SignUpRepository signUpRepository;

    @Inject
    public NewUserSignedUpEventHandler(SignUpRepository signUpRepository) {
        this.signUpRepository = signUpRepository;
    }

    @Async
    @Override
    public void handle(NewUserSignedUpEvent event) {
        signUpRepository.storeSignUpInformation(event.getAggregateId(),
                event.getUsername(), event.getSignUpCode());
    }

}
