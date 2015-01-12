package com.flexpoker.login.command.aggregate;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.flexpoker.login.command.events.LoginUserCreatedEvent;
import com.flexpoker.login.command.factory.LoginUserFactory;
import com.flexpoker.login.command.framework.LoginEvent;

@Component
public class DefaultLoginUserFactory implements LoginUserFactory {

    @Override
    public LoginUser createNew(UUID aggregateId, String username, String encryptedPassword) {
        return createWithGivenInfo(aggregateId, username, encryptedPassword);
    }

    @Override
    public LoginUser createFrom(List<LoginEvent> events) {
        LoginUserCreatedEvent loginUserCreatedEvent = (LoginUserCreatedEvent) events
                .get(0);
        LoginUser loginUser = createWithGivenInfo(loginUserCreatedEvent.getAggregateId(),
                loginUserCreatedEvent.getUsername(),
                loginUserCreatedEvent.getEncryptedPassword());
        loginUser.applyAllHistoricalEvents(events);
        return loginUser;
    }

    private LoginUser createWithGivenInfo(UUID aggregateId, String username,
            String encryptedPassword) {
        return new LoginUser(aggregateId, username, encryptedPassword);
    }

}
