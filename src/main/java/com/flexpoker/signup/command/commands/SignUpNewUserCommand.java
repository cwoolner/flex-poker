package com.flexpoker.signup.command.commands;

import com.flexpoker.framework.command.BaseCommand;
import com.flexpoker.signup.command.framework.SignUpCommand;
import com.flexpoker.signup.command.framework.SignUpCommandType;

public class SignUpNewUserCommand extends BaseCommand<SignUpCommandType> implements
        SignUpCommand {

    private static final SignUpCommandType TYPE = SignUpCommandType.SignUpNewUser;

    private final String username;

    private final String emailAddress;

    private final String password;

    public SignUpNewUserCommand(String username, String emailAddress, String password) {
        super(TYPE);
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }

}
