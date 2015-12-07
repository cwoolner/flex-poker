package com.flexpoker.signup.command.factory;

import java.util.List;

import com.flexpoker.signup.command.aggregate.SignUpUser;
import com.flexpoker.signup.command.commands.SignUpNewUserCommand;
import com.flexpoker.signup.command.framework.SignUpEvent;

public interface SignUpUserFactory {

    SignUpUser createNew(SignUpNewUserCommand command);

    SignUpUser createFrom(List<SignUpEvent> events);

}
