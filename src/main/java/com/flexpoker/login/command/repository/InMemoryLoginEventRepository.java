package com.flexpoker.login.command.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.flexpoker.login.command.framework.LoginEvent;

@Repository
public class InMemoryLoginEventRepository implements LoginEventRepository {

    private final Map<UUID, List<LoginEvent>> loginEventMap;

    public InMemoryLoginEventRepository() {
        loginEventMap = new HashMap<>();
    }

    @Override
    public List<LoginEvent> fetchAll(UUID id) {
        return loginEventMap.get(id);
    }

    @Override
    public void save(LoginEvent event) {
        if (!loginEventMap.containsKey(event.getAggregateId())) {
            loginEventMap.put(event.getAggregateId(), new ArrayList<>());
        }
        loginEventMap.get(event.getAggregateId()).add(event);
    }

}
