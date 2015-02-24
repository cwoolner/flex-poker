package com.flexpoker.signup.command.handlers;

import java.util.List;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandHandler;
import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.signup.command.aggregate.SignUpUser;
import com.flexpoker.signup.command.commands.ConfirmSignUpUserEmailCommand;
import com.flexpoker.signup.command.factory.SignUpUserFactory;
import com.flexpoker.signup.command.framework.SignUpEvent;
import com.flexpoker.signup.command.framework.SignUpEventType;
import com.flexpoker.signup.command.repository.SignUpEventRepository;

@Component
public class ConfirmSignUpUserEmailCommandHandler implements
        CommandHandler<ConfirmSignUpUserEmailCommand> {

    private final SignUpUserFactory signUpUserFactory;

    private final EventPublisher<SignUpEventType> eventPublisher;

    private final SignUpEventRepository signUpEventRepository;

    @Inject
    public ConfirmSignUpUserEmailCommandHandler(SignUpUserFactory signUpUserFactory,
            EventPublisher<SignUpEventType> eventPublisher,
            SignUpEventRepository signUpEventRepository) {
        this.signUpUserFactory = signUpUserFactory;
        this.eventPublisher = eventPublisher;
        this.signUpEventRepository = signUpEventRepository;
    }

    @Async
    @Override
    public void handle(ConfirmSignUpUserEmailCommand command) {
        List<SignUpEvent> signUpEvents = signUpEventRepository.fetchAll(command
                .getAggregateId());
        SignUpUser signUpUser = signUpUserFactory.createFrom(signUpEvents);
        signUpUser.confirmSignedUpUser(command.getUsername(), command.getSignUpCode());
        signUpUser.fetchNewEvents().forEach(x -> signUpEventRepository.save(x));
        signUpUser.fetchNewEvents().forEach(x -> eventPublisher.publish(x));
    }

}
