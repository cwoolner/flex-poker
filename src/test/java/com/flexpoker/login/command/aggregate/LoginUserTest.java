package com.flexpoker.login.command.aggregate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.Test;

import com.flexpoker.login.command.commands.CreateLoginUserCommand;
import com.flexpoker.login.command.events.LoginUserCreatedEvent;
import com.flexpoker.login.command.factory.LoginUserFactory;
import com.flexpoker.login.command.framework.LoginEventType;

public class LoginUserTest {

    private final LoginUserFactory loginUserFactory = new DefaultLoginUserFactory();

    @Test
    public void testCreate() {
        CreateLoginUserCommand command = new CreateLoginUserCommand(
                UUID.randomUUID(), "test", "password");
        LoginUser loginUser = loginUserFactory.createNew(command);

        assertEquals(1, loginUser.fetchNewEvents().size());
        assertEquals(LoginEventType.LoginUserCreated, loginUser.fetchNewEvents().get(0)
                .getType());

        LoginUserCreatedEvent loginUserCreatedEvent = (LoginUserCreatedEvent) loginUser
                .fetchNewEvents().get(0);
        assertNotNull(loginUserCreatedEvent.getAggregateId());
        assertEquals(1, loginUserCreatedEvent.getVersion());
    }

}
