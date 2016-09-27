package com.flexpoker.signup;

import java.util.UUID;

import com.flexpoker.exception.FlexPokerException;

public class SignUpUser {

    private final UUID aggregateId;

    private String username;

    private UUID signUpCode;

    private boolean confirmed;

    private String encryptedPassword;

    public SignUpUser(UUID aggregateId,
            UUID signUpCode, String emailAddress, String username,
            String encryptedPassword) {
        this.aggregateId = aggregateId;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.signUpCode = signUpCode;
    }

    public void confirmSignedUpUser(final String username, final UUID signUpCode) {
        if (this.username == null) {
            throw new IllegalStateException("username should be set already");
        }
        if (this.confirmed) {
            throw new IllegalStateException("confirmed should be false");
        }

        if (!this.username.equals(username)) {
            throw new FlexPokerException("username does not match");
        }
        if (!this.signUpCode.equals(signUpCode)) {
            throw new FlexPokerException("sign-up code does not match");
        }

        confirmed = true;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

}
