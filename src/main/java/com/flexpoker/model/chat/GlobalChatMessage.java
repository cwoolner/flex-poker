package com.flexpoker.model.chat;

public class GlobalChatMessage {

    private final String message;

    private final String username;

    private final boolean isSystemMessage;

    public GlobalChatMessage(String message, String username, boolean isSystemMessage) {
        this.message = message;
        this.username = username;
        this.isSystemMessage = isSystemMessage;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public boolean isSystemMessage() {
        return isSystemMessage;
    }

}
