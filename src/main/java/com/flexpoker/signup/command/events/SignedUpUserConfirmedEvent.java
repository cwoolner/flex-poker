package com.flexpoker.signup.command.events;

import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.signup.command.framework.SignUpEvent;

public class SignedUpUserConfirmedEvent extends BaseEvent implements
        SignUpEvent {

    private final String username;

    private final String encryptedPassword;

    public SignedUpUserConfirmedEvent(UUID aggregateId, int version, String username,
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
