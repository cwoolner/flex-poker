package com.flexpoker.signup.command.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.flexpoker.signup.command.framework.SignUpEvent;

@Repository
public class InMemorySignUpEventRepository implements SignUpEventRepository {

    private final Map<UUID, List<SignUpEvent>> signUpEventMap;

    public InMemorySignUpEventRepository() {
        signUpEventMap = new HashMap<>();
    }

    @Override
    public List<SignUpEvent> fetchAll(UUID id) {
        return signUpEventMap.get(id);
    }

    @Override
    public void save(SignUpEvent event) {
        if (!signUpEventMap.containsKey(event.getAggregateId())) {
            signUpEventMap.put(event.getAggregateId(), new ArrayList<SignUpEvent>());
        }
        signUpEventMap.get(event.getAggregateId()).add(event);
    }

}
