package com.flexpoker.login.query.handlers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.login.command.events.LoginUserCreatedEvent;
import com.flexpoker.login.query.repository.LoginRepository;

@Component
public class LoginUserCreatedEventHandler implements EventHandler<LoginUserCreatedEvent> {

    private final LoginRepository loginRepository;

    @Inject
    public LoginUserCreatedEventHandler(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @Async
    @Override
    public void handle(LoginUserCreatedEvent event) {
        loginRepository.saveLogin(event.getUsername(), event.getEncryptedPassword());
    }

}
