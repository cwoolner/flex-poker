package com.flexpoker.login.command.aggregate;

import java.util.List;
import java.util.UUID;

import com.flexpoker.framework.domain.AggregateRoot;
import com.flexpoker.login.command.events.LoginUserCreatedEvent;
import com.flexpoker.login.command.framework.LoginEvent;

public class LoginUser extends AggregateRoot<LoginEvent> {

    private int aggregateVersion;

    protected LoginUser(boolean creatingFromEvents, UUID aggregateId,
            String username, String encryptedPassword) {
        if (!creatingFromEvents) {
            LoginUserCreatedEvent loginUserCreatedEvent = new LoginUserCreatedEvent(
                    aggregateId, ++aggregateVersion, username,
                    encryptedPassword);
            addNewEvent(loginUserCreatedEvent);
        }
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
            break;
        default:
            throw new IllegalArgumentException(
                    "Event Type cannot be handled: " + event.getType());
        }
    }

}
