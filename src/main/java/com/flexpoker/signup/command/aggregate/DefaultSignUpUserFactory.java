package com.flexpoker.signup.command.aggregate;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.flexpoker.signup.command.commands.SignUpNewUserCommand;
import com.flexpoker.signup.command.events.NewUserSignedUpEvent;
import com.flexpoker.signup.command.factory.SignUpUserFactory;
import com.flexpoker.signup.command.framework.SignUpEvent;

@Component
public class DefaultSignUpUserFactory implements SignUpUserFactory {

    @Override
    public SignUpUser createNew(SignUpNewUserCommand command) {
        UUID aggregateId = UUID.randomUUID();
        UUID signUpCode = UUID.randomUUID();
        String encryptedPassword = new BCryptPasswordEncoder()
                .encode(command.getPassword());

        return createWithGivenInfo(false, aggregateId, signUpCode,
                command.getEmailAddress(), command.getUsername(),
                encryptedPassword);
    }

    @Override
    public SignUpUser createFrom(List<SignUpEvent> events) {
        NewUserSignedUpEvent newUserSignedUpEvent = (NewUserSignedUpEvent) events
                .get(0);
        SignUpUser signUpUser = createWithGivenInfo(true,
                newUserSignedUpEvent.getAggregateId(),
                newUserSignedUpEvent.getSignUpCode(),
                newUserSignedUpEvent.getEmailAddress(),
                newUserSignedUpEvent.getUsername(),
                newUserSignedUpEvent.getEncryptedPassword());
        signUpUser.applyAllHistoricalEvents(events);
        return signUpUser;
    }

    private SignUpUser createWithGivenInfo(boolean creatingFromEvents,
            UUID aggregateId, UUID signUpCode, String emailAddress, String username,
            String encryptedPassword) {
        return new SignUpUser(creatingFromEvents, aggregateId, signUpCode, emailAddress,
                username, encryptedPassword);
    }

}
