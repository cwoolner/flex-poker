package com.flexpoker.signup.command.aggregate;

import java.util.List;
import java.util.UUID;

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

    protected SignUpUser(boolean creatingFromEvents, UUID aggregateId,
            UUID signUpCode, String emailAddress, String username,
            String encryptedPassword) {
        this.aggregateId = aggregateId;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.signUpCode = signUpCode;

        if (!creatingFromEvents) {
            NewUserSignedUpEvent newUserSignedUpEvent = new NewUserSignedUpEvent(
                    aggregateId, ++aggregateVersion, signUpCode, emailAddress,
                    username, encryptedPassword);
            addNewEvent(newUserSignedUpEvent);
            applyCommonEvent(newUserSignedUpEvent);
        }
    }

    @Override
    public void applyAllHistoricalEvents(List<SignUpEvent> events) {
        for (SignUpEvent event : events) {
            aggregateVersion++;
            applyCommonEvent(event);
        }
    }

    private void applyCommonEvent(SignUpEvent event) {
        switch (event.getType()) {
        case NewUserSignedUp:
            break;
        case SignedUpUserConfirmed:
            applyEvent((SignedUpUserConfirmedEvent) event);
            break;
        default:
            throw new IllegalArgumentException("Event Type cannot be handled: "
                    + event.getType());
        }
        addAppliedEvent(event);
    }

    private void applyEvent(SignedUpUserConfirmedEvent event) {
        confirmed = true;
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
        applyCommonEvent(signedUpUserConfirmedEvent);
    }

}
