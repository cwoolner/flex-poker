package com.flexpoker.model.chat.outgoing;

public abstract class BaseOutgoingChatMessage {

    private final String message;

    private final String senderUsername;

    private final boolean isSystemMessage;

    public BaseOutgoingChatMessage(String message, String senderUsername, boolean isSystemMessage) {
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
