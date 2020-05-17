package com.flexpoker.web.dto.outgoing;

import java.util.UUID;

public class ChatMessageDTO {

    private final UUID id;

    private final UUID gameId;

    private final UUID tableId;

    private final String message;

    private final String senderUsername;

    private final boolean isSystemMessage;

    public ChatMessageDTO(UUID gameId, UUID tableId, String message, String senderUsername,
            boolean isSystemMessage) {
        this.id = UUID.randomUUID();
        this.gameId = gameId;
        this.tableId = tableId;
        this.message = message;
        this.senderUsername = senderUsername;
        this.isSystemMessage = isSystemMessage;
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
        return isSystemMessage;
    }

}
