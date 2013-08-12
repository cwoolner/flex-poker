package com.flexpoker.core.api.chat;

import com.flexpoker.model.chat.GlobalChatMessage;

public interface SendGlobalChatMessageCommand {

    void execute(GlobalChatMessage chatMessage);

}
