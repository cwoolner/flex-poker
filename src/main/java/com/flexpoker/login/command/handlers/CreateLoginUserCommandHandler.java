package com.flexpoker.login.command.handlers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandHandler;
import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.login.command.aggregate.LoginUser;
import com.flexpoker.login.command.commands.CreateLoginUserCommand;
import com.flexpoker.login.command.factory.LoginUserFactory;
import com.flexpoker.login.command.framework.LoginEventType;
import com.flexpoker.login.command.repository.LoginEventRepository;

@Component
public class CreateLoginUserCommandHandler implements
        CommandHandler<CreateLoginUserCommand> {

    private final LoginUserFactory loginUserFactory;

    private final EventPublisher<LoginEventType> eventPublisher;

    private final LoginEventRepository loginEventRepository;

    @Inject
    public CreateLoginUserCommandHandler(LoginUserFactory loginUserFactory,
            EventPublisher<LoginEventType> eventPublisher,
            LoginEventRepository loginEventRepository) {
        this.loginUserFactory = loginUserFactory;
        this.eventPublisher = eventPublisher;
        this.loginEventRepository = loginEventRepository;
    }

    @Async
    @Override
    public void handle(CreateLoginUserCommand command) {
        LoginUser loginUser = loginUserFactory.createNew(command);
        loginUser.fetchNewEvents().forEach(x -> loginEventRepository.save(x));
        loginUser.fetchNewEvents().forEach(x -> eventPublisher.publish(x));
    }

}
