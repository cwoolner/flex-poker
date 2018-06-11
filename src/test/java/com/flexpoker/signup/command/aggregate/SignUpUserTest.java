package com.flexpoker.signup.command.aggregate;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.signup.SignUpUser;

public class SignUpUserTest {

    private static final String VALID_EMAIL_ADDRESS = "unique@test.com";

    private static final String VALID_USERNAME = "unique";

    private static final String VALID_PASSWORD = "123456";

    private static final String VALID_ENCRYPTED_PASSWORD = new BCryptPasswordEncoder().encode(VALID_PASSWORD);

    private static final UUID VALID_AGGREGATE_ID = UUID.randomUUID();

    private static final UUID VALID_SIGN_UP_CODE = UUID.randomUUID();

    @Test
    public void testConfirmSignedUpUserSucceedsEvent() {
        var signUpUser = new SignUpUser(VALID_AGGREGATE_ID, VALID_SIGN_UP_CODE, VALID_EMAIL_ADDRESS,
                VALID_USERNAME, VALID_ENCRYPTED_PASSWORD);
        signUpUser.confirmSignedUpUser(VALID_USERNAME, VALID_SIGN_UP_CODE);
        assertTrue(signUpUser.isConfirmed());
    }

    @Test(expected = FlexPokerException.class)
    public void testConfirmSignedUpUserFailsBadUsername() {
        var signUpUser = new SignUpUser(VALID_AGGREGATE_ID, VALID_SIGN_UP_CODE, VALID_EMAIL_ADDRESS,
                VALID_USERNAME, VALID_ENCRYPTED_PASSWORD);
        signUpUser.confirmSignedUpUser("notequalusername", VALID_SIGN_UP_CODE);
    }

    @Test(expected = FlexPokerException.class)
    public void testConfirmSignedUpUserFailsBadSignUpCode() {
        var signUpUser = new SignUpUser(VALID_AGGREGATE_ID, VALID_SIGN_UP_CODE, VALID_EMAIL_ADDRESS,
                VALID_USERNAME, VALID_ENCRYPTED_PASSWORD);
        signUpUser.confirmSignedUpUser(VALID_USERNAME, UUID.randomUUID());
    }

    @Test(expected = IllegalStateException.class)
    public void testConfirmSignedUpUserFailedWhenEventAlreadyApplied() {
        var signUpUser = new SignUpUser(VALID_AGGREGATE_ID, VALID_SIGN_UP_CODE, VALID_EMAIL_ADDRESS,
                VALID_USERNAME, VALID_ENCRYPTED_PASSWORD);
        signUpUser.confirmSignedUpUser(VALID_USERNAME, VALID_SIGN_UP_CODE);
        signUpUser.confirmSignedUpUser(VALID_USERNAME, VALID_SIGN_UP_CODE);
    }

}
