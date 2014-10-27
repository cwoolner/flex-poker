package com.flexpoker.login.command.factory;

import java.util.List;
import java.util.UUID;

import com.flexpoker.login.command.aggregate.LoginUser;
import com.flexpoker.login.command.framework.LoginEvent;

public interface LoginUserFactory {

    LoginUser createNew(UUID aggregateId, String username, String encryptedPassword);

    LoginUser createFrom(List<LoginEvent> events);

}
