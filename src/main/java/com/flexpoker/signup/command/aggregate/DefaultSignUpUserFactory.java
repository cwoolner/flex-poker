package com.flexpoker.signup.command.aggregate;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.flexpoker.signup.command.factory.SignUpUserFactory;
import com.flexpoker.signup.command.framework.SignUpEvent;

@Component
public class DefaultSignUpUserFactory implements SignUpUserFactory {

    @Override
    public SignUpUser createNew() {
        UUID aggregateId = UUID.randomUUID();
        return createWithGivenId(aggregateId);
    }

    @Override
    public SignUpUser createFrom(List<SignUpEvent> events) {
        SignUpUser signUpUser = createWithGivenId(events.get(0).getAggregateId());
        signUpUser.applyAllHistoricalEvents(events);
        return signUpUser;
    }

    private SignUpUser createWithGivenId(UUID aggregateId) {
        return new SignUpUser(aggregateId);
    }

}
