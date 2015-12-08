package com.flexpoker.login.command.commandreceivers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.Command;
import com.flexpoker.framework.command.CommandHandler;
import com.flexpoker.framework.command.CommandReceiver;
import com.flexpoker.login.command.commands.CreateLoginUserCommand;
import com.flexpoker.login.command.framework.LoginCommandType;

@Component("loginCommandReceiver")
public class InMemoryAsyncLoginCommandReceiver
        implements CommandReceiver<LoginCommandType> {

    private final CommandHandler<CreateLoginUserCommand> createLoginUserCommandHandler;

    @Inject
    public InMemoryAsyncLoginCommandReceiver(
            CommandHandler<CreateLoginUserCommand> createLoginUserCommandHandler) {
        this.createLoginUserCommandHandler = createLoginUserCommandHandler;
    }

    @Async
    @Override
    public void receive(Command<LoginCommandType> command) {
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
