package com.flexpoker.model.chat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatMessage {

    private final String message;

    private final String username;

    private final String gameId;

    private final String tableId;

    @JsonCreator
    public ChatMessage(
            @JsonProperty(value = "message") String message,
            @JsonProperty(value = "username") String username,
            @JsonProperty(value = "gameId") String gameId,
            @JsonProperty(value = "tableId") String tableId) {
        this.message = message;
        this.username = username;
        this.gameId = gameId;
        this.tableId = tableId;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public String getGameId() {
        return gameId;
    }

    public String getTableId() {
        return tableId;
    }

}
