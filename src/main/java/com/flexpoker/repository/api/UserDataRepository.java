package com.flexpoker.repository.api;

import java.util.UUID;

public interface UserDataRepository {

    void setPersonalChatId(String username);
    
    UUID getPersonalChatId(String username);

}
