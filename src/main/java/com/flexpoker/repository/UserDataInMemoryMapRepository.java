package com.flexpoker.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.flexpoker.repository.api.UserDataRepository;

@Repository
public class UserDataInMemoryMapRepository implements UserDataRepository {

    private final Map<String, UUID> userDataMap = new HashMap<>();
    
    @Override
    public void setPersonalChatId(String username) {
        userDataMap.put(username, UUID.randomUUID());
    }

    @Override
    public UUID getPersonalChatId(String username) {
        return userDataMap.get(username);
    }

}
