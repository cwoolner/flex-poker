package com.flexpoker.model.chat.outgoing;

import java.util.UUID;

public class TableChatMessage extends BaseOutgoingChatMessage {

    private final UUID tableId;

    public TableChatMessage(String message, String senderUsername, boolean isSystemMessage, UUID tableId) {
        super(message, senderUsername, isSystemMessage);
        this.tableId = tableId;
    }

    public UUID getTableId() {
        return tableId;
    }
}
