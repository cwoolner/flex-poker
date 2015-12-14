package com.flexpoker.login.command.factory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.flexpoker.login.command.aggregate.DefaultLoginUserFactory;
import com.flexpoker.login.command.aggregate.LoginUser;
import com.flexpoker.login.command.commands.CreateLoginUserCommand;
import com.flexpoker.login.command.events.LoginUserCreatedEvent;
import com.flexpoker.login.command.framework.LoginEvent;

public class LoginUserFactoryTest {

    private DefaultLoginUserFactory sut;

    @Before
    public void setup() {
        sut = new DefaultLoginUserFactory();
    }

    @Test
    public void testCreateNew() {
        CreateLoginUserCommand command = new CreateLoginUserCommand(
                UUID.randomUUID(), "test", "encryptedPassword");
        LoginUser loginUser = sut.createNew(command);
        assertNotNull(loginUser);
        assertFalse(loginUser.fetchAppliedEvents().isEmpty());
        assertFalse(loginUser.fetchNewEvents().isEmpty());
    }

    @Test
    public void testCreateFrom() {
        List<LoginEvent> events = new ArrayList<>();
        events.add(new LoginUserCreatedEvent(null, 1, null, null));
        LoginUser loginUser = sut.createFrom(events);
        assertNotNull(loginUser);
        assertFalse(loginUser.fetchAppliedEvents().isEmpty());
        assertTrue(loginUser.fetchNewEvents().isEmpty());
    }

}
