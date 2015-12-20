package com.flexpoker.signup.command.events;

import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.signup.command.framework.SignUpEvent;

public class NewUserSignedUpEvent extends BaseEvent
        implements SignUpEvent {

    private final UUID signUpCode;

    private final String emailAddress;

    private final String username;

    private final String encryptedPassword;

    public NewUserSignedUpEvent(UUID aggregateId, int version, UUID signUpCode,
            String emailAddress, String username, String encryptedPassword) {
        super(aggregateId, version);
        this.signUpCode = signUpCode;
        this.emailAddress = emailAddress;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getUsername() {
        return username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public UUID getSignUpCode() {
        return signUpCode;
    }

}
