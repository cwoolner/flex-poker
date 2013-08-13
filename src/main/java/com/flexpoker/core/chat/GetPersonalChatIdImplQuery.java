package com.flexpoker.core.chat;

import java.security.Principal;
import java.util.UUID;

import javax.inject.Inject;

import com.flexpoker.config.Query;
import com.flexpoker.core.api.chat.GetPersonalChatIdQuery;
import com.flexpoker.repository.api.UserDataRepository;

@Query
public class GetPersonalChatIdImplQuery implements GetPersonalChatIdQuery {

    private UserDataRepository userDataRepository;
    
    @Inject
    public GetPersonalChatIdImplQuery(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }
    
    @Override
    public UUID execute(Principal principal) {
        return userDataRepository.getPersonalChatId(principal.getName());
    }

}
