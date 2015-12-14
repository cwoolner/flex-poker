package com.flexpoker.signup.command.aggregate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.signup.command.commands.SignUpNewUserCommand;
import com.flexpoker.signup.command.events.NewUserSignedUpEvent;
import com.flexpoker.signup.command.events.SignedUpUserConfirmedEvent;
import com.flexpoker.signup.command.framework.SignUpEvent;
import com.flexpoker.signup.command.framework.SignUpEventType;

public class SignUpUserTest {

    private static final String VALID_EMAIL_ADDRESS = "unique@test.com";

    private static final String VALID_USERNAME = "unique";

    private static final String VALID_PASSWORD = "123456";

    private static final String VALID_ENCRYPTED_PASSWORD = new ShaPasswordEncoder()
            .encodePassword(VALID_PASSWORD, null);

    private static final UUID VALID_AGGREGATE_ID = UUID.randomUUID();

    private static final UUID VALID_SIGN_UP_CODE = UUID.randomUUID();

    private final DefaultSignUpUserFactory signUpUserFactory = new DefaultSignUpUserFactory();

    @Test
    public void testSignUpNewUserSuccess() {
        SignUpNewUserCommand command = new SignUpNewUserCommand(VALID_USERNAME,
                VALID_EMAIL_ADDRESS, VALID_PASSWORD);
        SignUpUser signUpUser = signUpUserFactory.createNew(command);

        assertEquals(1, signUpUser.fetchAppliedEvents().size());
        assertEquals(1, signUpUser.fetchNewEvents().size());
        assertEquals(SignUpEventType.NewUserSignedUp, signUpUser.fetchNewEvents().get(0)
                .getType());

        NewUserSignedUpEvent newUserSignedUpEvent = (NewUserSignedUpEvent) signUpUser
                .fetchNewEvents().get(0);
        assertNotNull(newUserSignedUpEvent.getAggregateId());
        assertEquals(1, newUserSignedUpEvent.getVersion());
    }

    @Test
    public void testConfirmSignedUpUserSucceedsEvent() {
        List<SignUpEvent> events = new ArrayList<>();
        events.add(new NewUserSignedUpEvent(VALID_AGGREGATE_ID, 1, VALID_SIGN_UP_CODE,
                VALID_EMAIL_ADDRESS, VALID_USERNAME, VALID_ENCRYPTED_PASSWORD));
        SignUpUser signUpUser = signUpUserFactory.createFrom(events);

        signUpUser.confirmSignedUpUser(VALID_USERNAME, VALID_SIGN_UP_CODE);

        assertEquals(2, signUpUser.fetchAppliedEvents().size());
        assertEquals(1, signUpUser.fetchNewEvents().size());
        assertEquals(2, signUpUser.fetchNewEvents().get(0).getVersion());
    }

    @Test(expected = FlexPokerException.class)
    public void testConfirmSignedUpUserFailsBadUsername() {
        List<SignUpEvent> events = new ArrayList<>();
        events.add(new NewUserSignedUpEvent(VALID_AGGREGATE_ID, 1, VALID_SIGN_UP_CODE,
                VALID_EMAIL_ADDRESS, VALID_USERNAME, VALID_ENCRYPTED_PASSWORD));
        SignUpUser signUpUser = signUpUserFactory.createFrom(events);

        signUpUser.confirmSignedUpUser("notequalusername", VALID_SIGN_UP_CODE);
    }

    @Test(expected = FlexPokerException.class)
    public void testConfirmSignedUpUserFailsBadSignUpCode() {
        List<SignUpEvent> events = new ArrayList<>();
        events.add(new NewUserSignedUpEvent(VALID_AGGREGATE_ID, 1, VALID_SIGN_UP_CODE,
                VALID_EMAIL_ADDRESS, VALID_USERNAME, VALID_ENCRYPTED_PASSWORD));
        SignUpUser signUpUser = signUpUserFactory.createFrom(events);

        signUpUser.confirmSignedUpUser(VALID_USERNAME, UUID.randomUUID());
    }

    @Test(expected = IllegalStateException.class)
    public void testConfirmSignedUpUserFailedWhenEventAlreadyApplied() {
        List<SignUpEvent> events = new ArrayList<>();
        events.add(new NewUserSignedUpEvent(VALID_AGGREGATE_ID, 1, VALID_SIGN_UP_CODE,
                VALID_EMAIL_ADDRESS, VALID_USERNAME, VALID_ENCRYPTED_PASSWORD));
        events.add(new SignedUpUserConfirmedEvent(VALID_AGGREGATE_ID, 2, VALID_USERNAME,
                VALID_ENCRYPTED_PASSWORD));
        SignUpUser signUpUser = signUpUserFactory.createFrom(events);
        signUpUser.confirmSignedUpUser(VALID_USERNAME, VALID_SIGN_UP_CODE);
    }

}
