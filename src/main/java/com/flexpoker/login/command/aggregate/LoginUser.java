package com.flexpoker.login.command.aggregate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.flexpoker.framework.command.EventApplier;
import com.flexpoker.framework.domain.AggregateRoot;
import com.flexpoker.login.command.events.LoginUserCreatedEvent;
import com.flexpoker.login.command.framework.LoginEvent;

public class LoginUser extends AggregateRoot<LoginEvent> {

    private int aggregateVersion;

    private final Map<Class<? extends LoginEvent>, EventApplier<LoginEvent>> methodTable;

    protected LoginUser(boolean creatingFromEvents, UUID aggregateId,
            String username, String encryptedPassword) {
        methodTable = new HashMap<>();
        populateMethodTable();

        if (!creatingFromEvents) {
            LoginUserCreatedEvent loginUserCreatedEvent = new LoginUserCreatedEvent(
                    aggregateId, ++aggregateVersion, username,
                    encryptedPassword);
            addNewEvent(loginUserCreatedEvent);
            applyCommonEvent(loginUserCreatedEvent);
        }
    }

    @Override
    public void applyAllHistoricalEvents(List<LoginEvent> events) {
        for (LoginEvent event : events) {
            aggregateVersion++;
            applyCommonEvent(event);
        }
    }

    private void populateMethodTable() {
        methodTable.put(LoginUserCreatedEvent.class, x -> {});
    }

    private void applyCommonEvent(LoginEvent event) {
        methodTable.get(event.getClass()).applyEvent(event);
        addAppliedEvent(event);
    }

}
