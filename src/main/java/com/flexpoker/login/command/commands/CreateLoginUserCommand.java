package com.flexpoker.login.command.commands;

import java.util.UUID;

import com.flexpoker.framework.command.BaseCommand;
import com.flexpoker.login.command.framework.LoginCommand;
import com.flexpoker.login.command.framework.LoginCommandType;

public class CreateLoginUserCommand extends BaseCommand<LoginCommandType> implements
        LoginCommand {

    private static final LoginCommandType TYPE = LoginCommandType.CreateLoginUser;

    private final UUID aggregateId;

    private final String username;

    private final String encryptedPassword;

    public CreateLoginUserCommand(UUID aggregateId, String username,
            String encryptedPassword) {
        super(TYPE);
        this.aggregateId = aggregateId;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

}
