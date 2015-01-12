package com.flexpoker.login.command.aggregate;

import java.util.List;
import java.util.UUID;

import com.flexpoker.framework.domain.AggregateRoot;
import com.flexpoker.login.command.events.LoginUserCreatedEvent;
import com.flexpoker.login.command.framework.LoginEvent;

public class LoginUser extends AggregateRoot<LoginEvent> {

    private final UUID aggregateId;

    private int aggregateVersion;

    private final String username;

    private String encryptedPassword;

    private boolean enabled;

    protected LoginUser(UUID aggregateId, String username, String encryptedPassword) {
        this.aggregateId = aggregateId;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
    }

    @Override
    public void applyAllHistoricalEvents(List<LoginEvent> events) {
        for (LoginEvent event : events) {
            aggregateVersion++;
            applyCommonEvent(event);
        }
    }

    @Override
    public void applyAllNewEvents(List<LoginEvent> events) {
        for (LoginEvent event : events) {
            applyCommonEvent(event);
        }
    }

    private void applyCommonEvent(LoginEvent event) {
        switch (event.getType()) {
        case LoginUserCreated:
            applyEvent((LoginUserCreatedEvent) event);
            break;
        default:
            throw new IllegalArgumentException("Event Type cannot be handled: "
                    + event.getType());
        }
    }

    private void applyEvent(LoginUserCreatedEvent event) {
        enabled = true;
        addAppliedEvent(event);
    }

    public void enableNewUser() {
        if (enabled) {
            throw new IllegalStateException("enabled should be false");
        }

        LoginUserCreatedEvent loginUserCreatedEvent = new LoginUserCreatedEvent(
                aggregateId, ++aggregateVersion, username, encryptedPassword);
        addNewEvent(loginUserCreatedEvent);
        applyEvent(loginUserCreatedEvent);
    }

}
