package com.flexpoker.core.chat;

import java.security.Principal;

import javax.inject.Inject;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.chat.CreatePersonalChatIdCommand;
import com.flexpoker.repository.api.UserDataRepository;

@Command
public class CreatePersonalChatIdImplCommand implements CreatePersonalChatIdCommand {

    private final UserDataRepository userDataRepository;

    @Inject
    public CreatePersonalChatIdImplCommand(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    @Override
    public void execute(Principal principal) {
        userDataRepository.setPersonalChatId(principal.getName());
    }

}
