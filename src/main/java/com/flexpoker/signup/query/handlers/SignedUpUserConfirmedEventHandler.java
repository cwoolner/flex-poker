package com.flexpoker.signup.query.handlers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.signup.command.events.SignedUpUserConfirmedEvent;
import com.flexpoker.signup.query.repository.SignUpRepository;

@Component
public class SignedUpUserConfirmedEventHandler implements
        EventHandler<SignedUpUserConfirmedEvent> {

    private final SignUpRepository signUpRepository;

    @Inject
    public SignedUpUserConfirmedEventHandler(SignUpRepository signUpRepository) {
        this.signUpRepository = signUpRepository;
    }

    @Async
    @Override
    public void handle(SignedUpUserConfirmedEvent event) {
        signUpRepository.storeNewlyConfirmedUsername(event.getUsername());
    }

}
