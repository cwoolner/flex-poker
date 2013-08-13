package com.flexpoker.bso;

import java.security.Principal;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.api.ChatBso;
import com.flexpoker.core.api.chat.CreatePersonalChatIdCommand;
import com.flexpoker.core.api.chat.GetPersonalChatIdQuery;
import com.flexpoker.core.api.chat.SendChatMessageCommand;
import com.flexpoker.model.chat.incoming.ChatMessage;

@Service
public class ChatBsoImpl implements ChatBso {

    private final SendChatMessageCommand sendChatMessageCommand;
    
    private final GetPersonalChatIdQuery personalChatIdQuery;
    
    private final CreatePersonalChatIdCommand createPersonalChatIdCommand;

    @Inject
    public ChatBsoImpl(SendChatMessageCommand sendChatMessageCommand,
            GetPersonalChatIdQuery personalChatIdQuery,
            CreatePersonalChatIdCommand createPersonalChatIdCommand) {
        this.sendChatMessageCommand = sendChatMessageCommand;
        this.personalChatIdQuery = personalChatIdQuery;
        this.createPersonalChatIdCommand = createPersonalChatIdCommand;
    }

    @Override
    public void sendChatMessage(ChatMessage chatMessage, Principal principal) {
        sendChatMessageCommand.execute(chatMessage, principal);
    }

    @Override
    public UUID fetchPersonalChatId(Principal principal) {
        return personalChatIdQuery.execute(principal);
    }

    @Override
    public void createPersonalChatId(Principal principal) {
        createPersonalChatIdCommand.execute(principal);
    }

}
