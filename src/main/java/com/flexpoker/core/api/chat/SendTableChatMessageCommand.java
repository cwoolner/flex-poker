package com.flexpoker.core.api.chat;

import com.flexpoker.model.chat.outgoing.TableChatMessage;

public interface SendTableChatMessageCommand {

    void execute(TableChatMessage chatMessage);

}
