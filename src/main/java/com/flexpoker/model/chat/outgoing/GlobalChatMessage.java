package com.flexpoker.model.chat.outgoing;

public class GlobalChatMessage {

    private final String message;

    private final String senderUsername;

    private final boolean isSystemMessage;

    public GlobalChatMessage(String message, String senderUsername, boolean isSystemMessage) {
        this.message = message;
        this.senderUsername = senderUsername;
        this.isSystemMessage = isSystemMessage;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public boolean isSystemMessage() {
        return isSystemMessage;
    }

}
