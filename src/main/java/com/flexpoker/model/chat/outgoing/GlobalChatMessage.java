package com.flexpoker.model.chat.outgoing;

public class GlobalChatMessage extends BaseOutgoingChatMessage {

    public GlobalChatMessage(String message, String senderUsername, boolean isSystemMessage) {
        super(message, senderUsername, isSystemMessage);
    }

}
