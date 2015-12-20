package com.flexpoker.signup.command.aggregate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.framework.command.EventApplier;
import com.flexpoker.framework.domain.AggregateRoot;
import com.flexpoker.signup.command.events.NewUserSignedUpEvent;
import com.flexpoker.signup.command.events.SignedUpUserConfirmedEvent;
import com.flexpoker.signup.command.framework.SignUpEvent;

public class SignUpUser extends AggregateRoot<SignUpEvent> {

    private final UUID aggregateId;

    private int aggregateVersion;

    private final Map<Class<? extends SignUpEvent>, EventApplier<SignUpEvent>> methodTable;

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

        methodTable = new HashMap<>();
        populateMethodTable();

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

    private void populateMethodTable() {
        methodTable.put(NewUserSignedUpEvent.class, x -> {});
        methodTable.put(SignedUpUserConfirmedEvent.class, x -> confirmed = true);
    }

    private void applyCommonEvent(SignUpEvent event) {
        methodTable.get(event.getClass()).applyEvent(event);
        addAppliedEvent(event);
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
