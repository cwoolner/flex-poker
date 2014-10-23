package com.flexpoker.signup.command.events;

import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.signup.command.framework.SignUpEvent;
import com.flexpoker.signup.command.framework.SignUpEventType;

public class NewUserSignedUpEvent extends BaseEvent<SignUpEventType> implements
        SignUpEvent {

    private static final SignUpEventType TYPE = SignUpEventType.NewUserSignedUp;

    private final UUID signUpCode;

    private final String emailAddress;

    private final String username;

    private final String encryptedPassword;

    public NewUserSignedUpEvent(UUID aggregateId, int version, UUID signUpCode,
            String emailAddress, String username, String encryptedPassword) {
        super(aggregateId, version, TYPE);
        this.signUpCode = signUpCode;
        this.emailAddress = emailAddress;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
    }

    public String getUsername() {
        return username;
    }

    public UUID getSignUpCode() {
        return signUpCode;
    }

}
