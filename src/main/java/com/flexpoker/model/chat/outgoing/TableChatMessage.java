package com.flexpoker.model.chat.outgoing;

public class TableChatMessage extends BaseOutgoingChatMessage {

    private final Integer gameId;
    
    private final Integer tableId;

    public TableChatMessage(String message, String senderUsername,
            boolean isSystemMessage, Integer gameId, Integer tableId) {
        super(message, senderUsername, isSystemMessage);
        this.gameId = gameId;
        this.tableId = tableId;
    }

    public Integer getGameId() {
        return gameId;
    }

    public Integer getTableId() {
        return tableId;
    }

}
