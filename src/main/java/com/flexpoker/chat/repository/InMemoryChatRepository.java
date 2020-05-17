package com.flexpoker.chat.repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.flexpoker.config.ProfileNames;
import com.flexpoker.web.dto.outgoing.ChatMessageDTO;

@Profile({ ProfileNames.DEFAULT, ProfileNames.CHAT_INMEMORY })
@Repository
public class InMemoryChatRepository implements ChatRepository {

    private final List<ChatMessageDTO> lobbyChatMessages;

    private final Map<UUID, List<ChatMessageDTO>> gameChatMessages;

    private final Map<UUID, List<ChatMessageDTO>> tableChatMessages;

    public InMemoryChatRepository() {
        lobbyChatMessages = new CopyOnWriteArrayList<ChatMessageDTO>();
        gameChatMessages = new ConcurrentHashMap<>();
        tableChatMessages = new ConcurrentHashMap<>();
    }

    @Override
    public void saveChatMessage(ChatMessageDTO chatMessage) {
        if (chatMessage.getGameId() != null && chatMessage.getTableId() != null) {
            tableChatMessages.putIfAbsent(chatMessage.getTableId(), new CopyOnWriteArrayList<>());
            tableChatMessages.get(chatMessage.getTableId()).add(chatMessage);
        } else if (chatMessage.getGameId() != null) {
            gameChatMessages.putIfAbsent(chatMessage.getGameId(), new CopyOnWriteArrayList<>());
            gameChatMessages.get(chatMessage.getGameId()).add(chatMessage);
        } else {
            lobbyChatMessages.add(chatMessage);
        }
    }

    @Override
    public List<ChatMessageDTO> fetchAllLobbyChatMessages() {
        return lobbyChatMessages;
    }

    @Override
    public List<ChatMessageDTO> fetchAllGameChatMessages(UUID gameId) {
        return gameChatMessages.get(gameId);
    }

    @Override
    public List<ChatMessageDTO> fetchAllTableChatMessages(UUID tableId) {
        return tableChatMessages.get(tableId);
    }

}
