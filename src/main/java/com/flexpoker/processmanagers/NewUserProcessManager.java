package com.flexpoker.processmanagers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.login.command.commands.CreateLoginUserCommand;
import com.flexpoker.login.command.framework.LoginCommandType;
import com.flexpoker.signup.command.events.SignedUpUserConfirmedEvent;

@Component
public class NewUserProcessManager implements ProcessManager<SignedUpUserConfirmedEvent> {

    private final CommandSender<LoginCommandType> loginCommandSender;

    @Inject
    public NewUserProcessManager(CommandSender<LoginCommandType> loginCommandSender) {
        this.loginCommandSender = loginCommandSender;
    }

    @Async
    @Override
    public void handle(SignedUpUserConfirmedEvent event) {
        CreateLoginUserCommand createLoginUserCommand = new CreateLoginUserCommand(
                event.getAggregateId(), event.getUsername(), event.getEncryptedPassword());
        loginCommandSender.send(createLoginUserCommand);
    }

}
