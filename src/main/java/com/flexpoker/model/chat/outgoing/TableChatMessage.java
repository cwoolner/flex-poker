package com.flexpoker.model.chat.outgoing;

import java.util.UUID;

public class TableChatMessage extends BaseOutgoingChatMessage {

    private final UUID gameId;
    
    private final UUID tableId;

    public TableChatMessage(String message, String senderUsername,
            boolean isSystemMessage, UUID gameId, UUID tableId) {
        super(message, senderUsername, isSystemMessage);
        this.gameId = gameId;
        this.tableId = tableId;
    }

    public UUID getGameId() {
        return gameId;
    }

    public UUID getTableId() {
        return tableId;
    }

}
