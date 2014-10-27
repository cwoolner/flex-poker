package com.flexpoker.signup.command.events;

import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.signup.command.framework.SignUpEvent;
import com.flexpoker.signup.command.framework.SignUpEventType;

public class SignedUpUserConfirmedEvent extends BaseEvent<SignUpEventType> implements
        SignUpEvent {

    private static final SignUpEventType TYPE = SignUpEventType.SignedUpUserConfirmed;

    private final String username;

    private final String encryptedPassword;

    public SignedUpUserConfirmedEvent(UUID aggregateId, int version, String username,
            String encryptedPassword) {
        super(aggregateId, version, TYPE);
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
