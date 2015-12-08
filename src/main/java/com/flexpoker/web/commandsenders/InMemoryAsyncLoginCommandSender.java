package com.flexpoker.web.commandsenders;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.Command;
import com.flexpoker.framework.command.CommandReceiver;
import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.login.command.framework.LoginCommandType;

@Component
public class InMemoryAsyncLoginCommandSender
        implements CommandSender<LoginCommandType> {

    private final CommandReceiver<LoginCommandType> loginCommandReceiver;

    @Lazy
    @Inject
    public InMemoryAsyncLoginCommandSender(
            @Qualifier("loginCommandReceiver")
            CommandReceiver<LoginCommandType> loginCommandReceiver) {
        this.loginCommandReceiver = loginCommandReceiver;
    }

    @Override
    public void send(Command<LoginCommandType> command) {
        loginCommandReceiver.receive(command);
    }

}
