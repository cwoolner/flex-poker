package com.flexpoker.web.commandsenders;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.Command;
import com.flexpoker.framework.command.CommandReceiver;
import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.signup.command.framework.SignUpCommandType;

@Component
public class InMemoryAsyncSignUpCommandSender
        implements CommandSender<SignUpCommandType> {

    private final CommandReceiver<SignUpCommandType> signUpCommandReceiver;

    @Lazy
    @Inject
    public InMemoryAsyncSignUpCommandSender(
            @Qualifier("signUpCommandReceiver")
            CommandReceiver<SignUpCommandType> signUpCommandReceiver) {
        this.signUpCommandReceiver = signUpCommandReceiver;
    }

    @Override
    public void send(Command<SignUpCommandType> command) {
        signUpCommandReceiver.receive(command);
    }

}
