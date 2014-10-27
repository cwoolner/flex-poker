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

    public LoginUser(UUID aggregateId, String username, String encryptedPassword) {
        this.aggregateId = aggregateId;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
    }

    @Override
    public void applyAllEvents(List<LoginEvent> events) {
        for (LoginEvent event : events) {
            aggregateVersion++;
            switch (event.getType()) {
            case LoginUserCreated:
                applyEvent((LoginUserCreatedEvent) event);
                break;
            default:
                throw new IllegalArgumentException("Event Type cannot be handled: "
                        + event.getType());
            }
        }
    }

    private void applyEvent(LoginUserCreatedEvent event) {
        enabled = true;
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
