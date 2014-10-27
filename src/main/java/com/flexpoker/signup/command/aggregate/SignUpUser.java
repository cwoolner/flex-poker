package com.flexpoker.signup.command.aggregate;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.framework.domain.AggregateRoot;
import com.flexpoker.signup.command.events.NewUserSignedUpEvent;
import com.flexpoker.signup.command.events.SignedUpUserConfirmedEvent;
import com.flexpoker.signup.command.framework.SignUpEvent;

public class SignUpUser extends AggregateRoot<SignUpEvent> {

    private final UUID aggregateId;

    private int aggregateVersion;

    private String username;

    private UUID signUpCode;

    private boolean confirmed;

    private String encryptedPassword;

    protected SignUpUser(final UUID aggregateId) {
        this.aggregateId = aggregateId;
    }

    @Override
    public void applyAllEvents(List<SignUpEvent> events) {
        for (SignUpEvent event : events) {
            aggregateVersion++;
            switch (event.getType()) {
            case NewUserSignedUp:
                applyEvent((NewUserSignedUpEvent) event);
                break;
            case SignedUpUserConfirmed:
                applyEvent((SignedUpUserConfirmedEvent) event);
                break;
            default:
                throw new IllegalArgumentException("Event Type cannot be handled: "
                        + event.getType());
            }
        }
    }

    private void applyEvent(NewUserSignedUpEvent event) {
        username = event.getUsername();
        signUpCode = event.getSignUpCode();
        encryptedPassword = event.getEncryptedPassword();
        addAppliedEvent(event);
    }

    private void applyEvent(SignedUpUserConfirmedEvent event) {
        confirmed = true;
        addAppliedEvent(event);
    }

    public void signUpNewUser(final String emailAddress, final String username,
            final String password) {
        if (this.username != null) {
            throw new IllegalStateException("username should not be set");
        }
        if (this.confirmed) {
            throw new IllegalStateException("confirmed should be false");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(password);
        UUID signUpCode = UUID.randomUUID();
        NewUserSignedUpEvent newUserSignedUpEvent = new NewUserSignedUpEvent(aggregateId,
                ++aggregateVersion, signUpCode, emailAddress, username, encryptedPassword);
        addNewEvent(newUserSignedUpEvent);
        applyEvent(newUserSignedUpEvent);
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

        SignedUpUserConfirmedEvent signedUpUserConfirmedEvent = new SignedUpUserConfirmedEvent(
                aggregateId, ++aggregateVersion, username, encryptedPassword);
        addNewEvent(signedUpUserConfirmedEvent);
        applyEvent(signedUpUserConfirmedEvent);
    }

}
