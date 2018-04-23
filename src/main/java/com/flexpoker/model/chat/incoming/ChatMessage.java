package com.flexpoker.model.chat.incoming;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatMessage {

    private final String message;

    private final List<String> receiverUsernames;

    private final UUID gameId;

    private final UUID tableId;

    @JsonCreator
    public ChatMessage(
            @JsonProperty(value = "message") String message,
            @JsonProperty(value = "receiverUsernames") List<String> receiverUsernames,
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "tableId") UUID tableId) {
        this.message = message;
        this.receiverUsernames = receiverUsernames;
        this.gameId = gameId;
        this.tableId = tableId;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getReceiverUsernames() {
        return receiverUsernames;
    }

    public UUID getGameId() {
        return gameId;
    }

    public UUID getTableId() {
        return tableId;
    }

}
