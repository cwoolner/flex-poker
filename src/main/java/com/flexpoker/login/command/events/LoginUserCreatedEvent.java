package com.flexpoker.login.command.events;

import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.login.command.framework.LoginEvent;

public class LoginUserCreatedEvent extends BaseEvent implements
        LoginEvent {

    private final String username;

    private final String encryptedPassword;

    public LoginUserCreatedEvent(UUID aggregateId, int version, String username,
            String encryptedPassword) {
        super(aggregateId, version);
        this.username = username;
        this.encryptedPassword = encryptedPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

}
