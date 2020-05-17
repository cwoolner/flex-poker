package com.flexpoker.web.dto.outgoing;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatMessageDTO {

    private final UUID id;

    private final UUID gameId;

    private final UUID tableId;

    private final String message;

    private final String senderUsername;

    private final boolean systemMessage;

    @JsonCreator
    public ChatMessageDTO(
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "tableId") UUID tableId,
            @JsonProperty(value = "message") String message,
            @JsonProperty(value = "senderUsername") String senderUsername,
            @JsonProperty(value = "systemMessage") boolean systemMessage) {
        this.id = UUID.randomUUID();
        this.gameId = gameId;
        this.tableId = tableId;
        this.message = message;
        this.senderUsername = senderUsername;
        this.systemMessage = systemMessage;
    }

    public UUID getGameId() {
        return gameId;
    }

    public UUID getTableId() {
        return tableId;
    }

    public UUID getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public boolean isSystemMessage() {
        return systemMessage;
    }

}
