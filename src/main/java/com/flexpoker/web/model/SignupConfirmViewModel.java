package com.flexpoker.web.model;

import java.util.UUID;

public class SignupConfirmViewModel {

    private String username;

    private UUID signupCode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getSignupCode() {
        return signupCode;
    }

    public void setSignupCode(UUID signupCode) {
        this.signupCode = signupCode;
    }

}
