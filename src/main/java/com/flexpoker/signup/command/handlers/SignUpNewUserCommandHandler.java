package com.flexpoker.signup.command.handlers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandHandler;
import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.signup.command.aggregate.SignUpUser;
import com.flexpoker.signup.command.commands.SignUpNewUserCommand;
import com.flexpoker.signup.command.factory.SignUpUserFactory;
import com.flexpoker.signup.command.framework.SignUpEventType;
import com.flexpoker.signup.command.repository.SignUpEventRepository;

@Component
public class SignUpNewUserCommandHandler implements CommandHandler<SignUpNewUserCommand> {

    private final SignUpUserFactory signUpUserFactory;

    private final EventPublisher<SignUpEventType> eventPublisher;

    private final SignUpEventRepository signUpEventRepository;

    @Inject
    public SignUpNewUserCommandHandler(SignUpUserFactory signUpUserFactory,
            EventPublisher<SignUpEventType> eventPublisher,
            SignUpEventRepository signUpEventRepository) {
        this.signUpUserFactory = signUpUserFactory;
        this.eventPublisher = eventPublisher;
        this.signUpEventRepository = signUpEventRepository;
    }

    @Async
    @Override
    public void handle(SignUpNewUserCommand command) {
        SignUpUser signUpUser = signUpUserFactory.createNew();
        signUpUser.signUpNewUser(command.getEmailAddress(), command.getUsername(),
                command.getPassword());
        signUpUser.fetchNewEvents().forEach(x -> signUpEventRepository.save(x));
        signUpUser.fetchNewEvents().forEach(x -> eventPublisher.publish(x));
    }

}
