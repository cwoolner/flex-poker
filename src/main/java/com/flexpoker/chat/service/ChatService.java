package com.flexpoker.chat.service;

import java.util.UUID;

public interface ChatService {

    void saveAndPushSystemLobbyChatMessage(String message);

    void saveAndPushUserLobbyChatMessage(String message, String username);

    void saveAndPushSystemGameChatMessage(UUID gameId, String message);

    void saveAndPushUserGameChatMessage(UUID gameId, String message, String username);

    void saveAndPushSystemTableChatMessage(UUID gameId, UUID tableId, String message);

    void saveAndPushUserTableChatMessage(UUID gameId, UUID tableId, String message, String username);

}