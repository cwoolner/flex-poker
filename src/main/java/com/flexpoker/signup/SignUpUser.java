package com.flexpoker.signup;

import java.util.UUID;

import com.flexpoker.exception.FlexPokerException;

public class SignUpUser {

    private final UUID aggregateId;

    private String username;

    private UUID signUpCode;

    private boolean confirmed;

    private String encryptedPassword;

    private String email;

    public SignUpUser(UUID aggregateId,
            UUID signUpCode, String email, String username,
            String encryptedPassword) {
        this.aggregateId = aggregateId;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.signUpCode = signUpCode;
        this.email = email;
        this.confirmed = false;
    }

    public String getEmail() {
        return email;
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

    public String getUsername() {
        return username;
    }

    public UUID getSignUpCode() {
        return signUpCode;
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
