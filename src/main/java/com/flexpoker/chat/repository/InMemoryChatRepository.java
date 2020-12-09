package com.flexpoker.chat.repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.flexpoker.config.ProfileNames;
import com.flexpoker.web.dto.OutgoingChatMessageDTO;

@Profile({ ProfileNames.DEFAULT, ProfileNames.CHAT_INMEMORY })
@Repository
public class InMemoryChatRepository implements ChatRepository {

    private final List<OutgoingChatMessageDTO> lobbyChatMessages;

    private final Map<UUID, List<OutgoingChatMessageDTO>> gameChatMessages;

    private final Map<UUID, List<OutgoingChatMessageDTO>> tableChatMessages;

    public InMemoryChatRepository() {
        lobbyChatMessages = new CopyOnWriteArrayList<OutgoingChatMessageDTO>();
        gameChatMessages = new ConcurrentHashMap<>();
        tableChatMessages = new ConcurrentHashMap<>();
    }

    @Override
    public void saveChatMessage(OutgoingChatMessageDTO chatMessage) {
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
    public List<OutgoingChatMessageDTO> fetchAllLobbyChatMessages() {
        return lobbyChatMessages;
    }

    @Override
    public List<OutgoingChatMessageDTO> fetchAllGameChatMessages(UUID gameId) {
        return gameChatMessages.get(gameId);
    }

    @Override
    public List<OutgoingChatMessageDTO> fetchAllTableChatMessages(UUID tableId) {
        return tableChatMessages.get(tableId);
    }

}
