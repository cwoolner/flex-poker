package com.flexpoker.signup.command.commandreceivers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.Command;
import com.flexpoker.framework.command.CommandHandler;
import com.flexpoker.framework.command.CommandReceiver;
import com.flexpoker.signup.command.commands.ConfirmSignUpUserEmailCommand;
import com.flexpoker.signup.command.commands.SignUpNewUserCommand;
import com.flexpoker.signup.command.framework.SignUpCommandType;

@Component("signUpCommandReceiver")
public class InMemoryAsyncSignUpCommandReceiver
        implements CommandReceiver<SignUpCommandType> {

    private final CommandHandler<SignUpNewUserCommand> signUpNewUserCommandHandler;

    private final CommandHandler<ConfirmSignUpUserEmailCommand> confirmSignUpUserEmailCommandHandler;

    @Inject
    public InMemoryAsyncSignUpCommandReceiver(
            CommandHandler<SignUpNewUserCommand> signUpNewUserCommandHandler,
            CommandHandler<ConfirmSignUpUserEmailCommand> confirmSignUpUserEmailCommandHandler) {
        this.signUpNewUserCommandHandler = signUpNewUserCommandHandler;
        this.confirmSignUpUserEmailCommandHandler = confirmSignUpUserEmailCommandHandler;
    }

    @Async
    @Override
    public void receive(Command<SignUpCommandType> command) {
        switch (command.getType()) {
        case SignUpNewUser:
            signUpNewUserCommandHandler.handle((SignUpNewUserCommand) command);
            break;
        case ConfirmSignUpUserEmail:
            confirmSignUpUserEmailCommandHandler
                    .handle((ConfirmSignUpUserEmailCommand) command);
            break;
        default:
            throw new IllegalArgumentException(
                    "Command Type cannot be handled: " + command.getType());
        }
    }

}
