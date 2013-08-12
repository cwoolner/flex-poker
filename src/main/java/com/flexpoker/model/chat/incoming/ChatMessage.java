package com.flexpoker.model.chat.incoming;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatMessage {

    private final String message;

    private final List<String> receiverUsernames;

    private final String gameId;

    private final String tableId;

    @JsonCreator
    public ChatMessage(
            @JsonProperty(value = "message") String message,
            @JsonProperty(value = "receiverUsernames") List<String> receiverUsernames,
            @JsonProperty(value = "gameId") String gameId,
            @JsonProperty(value = "tableId") String tableId) {
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

    public String getGameId() {
        return gameId;
    }

    public String getTableId() {
        return tableId;
    }

}
