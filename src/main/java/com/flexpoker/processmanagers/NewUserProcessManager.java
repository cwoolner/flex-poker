package com.flexpoker.processmanagers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.login.query.repository.LoginRepository;
import com.flexpoker.signup.command.events.SignedUpUserConfirmedEvent;

@Component
public class NewUserProcessManager implements ProcessManager<SignedUpUserConfirmedEvent> {

    private final LoginRepository loginRepository;

    @Inject
    public NewUserProcessManager(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @Async
    @Override
    public void handle(SignedUpUserConfirmedEvent event) {
        loginRepository.saveUsernameAndPassword(event.getUsername(), event.getEncryptedPassword());
        loginRepository.saveAggregateIdAndUsername(event.getAggregateId(), event.getUsername());
    }

}
