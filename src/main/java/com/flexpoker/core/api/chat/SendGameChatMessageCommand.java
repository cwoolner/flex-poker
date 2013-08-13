package com.flexpoker.core.api.chat;

import com.flexpoker.model.chat.outgoing.GameChatMessage;

public interface SendGameChatMessageCommand {

    void execute(GameChatMessage chatMessage);

}
