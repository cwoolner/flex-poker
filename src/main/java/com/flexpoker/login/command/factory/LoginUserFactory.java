package com.flexpoker.login.command.factory;

import java.util.List;

import com.flexpoker.login.command.aggregate.LoginUser;
import com.flexpoker.login.command.commands.CreateLoginUserCommand;
import com.flexpoker.login.command.framework.LoginEvent;

public interface LoginUserFactory {

    LoginUser createNew(CreateLoginUserCommand command);

    LoginUser createFrom(List<LoginEvent> events);

}
