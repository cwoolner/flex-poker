package com.flexpoker.login.command.aggregate;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.flexpoker.login.command.commands.CreateLoginUserCommand;
import com.flexpoker.login.command.events.LoginUserCreatedEvent;
import com.flexpoker.login.command.factory.LoginUserFactory;
import com.flexpoker.login.command.framework.LoginEvent;

@Component
public class DefaultLoginUserFactory implements LoginUserFactory {

    @Override
    public LoginUser createNew(CreateLoginUserCommand command) {
        return createWithGivenInfo(false, command.getAggregateId(),
                command.getUsername(), command.getEncryptedPassword());
    }

    @Override
    public LoginUser createFrom(List<LoginEvent> events) {
        LoginUserCreatedEvent loginUserCreatedEvent = (LoginUserCreatedEvent) events
                .get(0);
        LoginUser loginUser = createWithGivenInfo(true,
                loginUserCreatedEvent.getAggregateId(),
                loginUserCreatedEvent.getUsername(),
                loginUserCreatedEvent.getEncryptedPassword());
        loginUser.applyAllHistoricalEvents(events);
        return loginUser;
    }

    private LoginUser createWithGivenInfo(boolean creatingFromEvents,
            UUID aggregateId, String username, String encryptedPassword) {
        return new LoginUser(creatingFromEvents, aggregateId, username,
                encryptedPassword);
    }

}
