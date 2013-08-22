package com.flexpoker.repository.api;

import java.util.UUID;

public interface SignupRepository {

    void storeSignupCode(String username, UUID signupCode, int expirationInMinutes);

    UUID fetchSignupCode(String username);

    void removeSignupCode(String username);

}
