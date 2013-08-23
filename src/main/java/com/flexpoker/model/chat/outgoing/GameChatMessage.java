package com.flexpoker.model.chat.outgoing;


public class GameChatMessage extends BaseOutgoingChatMessage {

    private final Integer gameId;

    public GameChatMessage(String message, String senderUsername, boolean isSystemMessage, Integer gameId) {
        super(message, senderUsername, isSystemMessage);
        this.gameId = gameId;
    }

    public Integer getGameId() {
        return gameId;
    }

}
