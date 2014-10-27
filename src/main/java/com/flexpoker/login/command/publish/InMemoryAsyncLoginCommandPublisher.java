package com.flexpoker.login.command.publish;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.Command;
import com.flexpoker.framework.command.CommandHandler;
import com.flexpoker.framework.command.CommandPublisher;
import com.flexpoker.login.command.commands.CreateLoginUserCommand;
import com.flexpoker.login.command.framework.LoginCommandType;

@Component
public class InMemoryAsyncLoginCommandPublisher implements
        CommandPublisher<LoginCommandType> {

    private final CommandHandler<CreateLoginUserCommand> createLoginUserCommandHandler;

    @Inject
    public InMemoryAsyncLoginCommandPublisher(
            CommandHandler<CreateLoginUserCommand> createLoginUserCommandHandler) {
        this.createLoginUserCommandHandler = createLoginUserCommandHandler;
    }

    @Override
    public void publish(Command<LoginCommandType> command) {
        switch (command.getType()) {
        case CreateLoginUser:
            createLoginUserCommandHandler.handle((CreateLoginUserCommand) command);
            break;
        default:
            throw new IllegalArgumentException("Command Type cannot be handled: "
                    + command.getType());
        }
    }

}
