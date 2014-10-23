package com.flexpoker.signup.command.commands;

import java.util.UUID;

import com.flexpoker.framework.command.BaseCommand;
import com.flexpoker.signup.command.framework.SignUpCommand;
import com.flexpoker.signup.command.framework.SignUpCommandType;

public class ConfirmSignUpUserEmailCommand extends BaseCommand<SignUpCommandType>
        implements SignUpCommand {

    private static final SignUpCommandType TYPE = SignUpCommandType.ConfirmSignUpUserEmail;

    private final UUID aggregateId;

    private final String username;

    private final UUID signUpCode;

    public ConfirmSignUpUserEmailCommand(UUID aggregateId, String username,
            UUID signUpCode) {
        super(TYPE);
        this.aggregateId = aggregateId;
        this.username = username;
        this.signUpCode = signUpCode;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public String getUsername() {
        return username;
    }

    public UUID getSignUpCode() {
        return signUpCode;
    }

}
