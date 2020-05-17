package com.flexpoker.chat.repository;

import java.util.List;
import java.util.UUID;

import com.flexpoker.web.dto.outgoing.ChatMessageDTO;

public interface ChatRepository {

    void saveChatMessage(ChatMessageDTO chatMessage);

    List<ChatMessageDTO> fetchAllLobbyChatMessages();

    List<ChatMessageDTO> fetchAllGameChatMessages(UUID gameId);

    List<ChatMessageDTO> fetchAllTableChatMessages(UUID tableId);

}
