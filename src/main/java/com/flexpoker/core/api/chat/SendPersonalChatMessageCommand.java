package com.flexpoker.core.api.chat;

import com.flexpoker.model.chat.outgoing.PersonalChatMessage;

public interface SendPersonalChatMessageCommand {

    void execute(PersonalChatMessage chatMessage);

}
