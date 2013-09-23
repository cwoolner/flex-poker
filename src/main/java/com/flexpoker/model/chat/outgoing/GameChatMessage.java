package com.flexpoker.model.chat.outgoing;

import java.util.UUID;


public class GameChatMessage extends BaseOutgoingChatMessage {

    private final UUID gameId;

    public GameChatMessage(String message, String senderUsername, boolean isSystemMessage, UUID gameId) {
        super(message, senderUsername, isSystemMessage);
        this.gameId = gameId;
    }

    public UUID getGameId() {
        return gameId;
    }

}
