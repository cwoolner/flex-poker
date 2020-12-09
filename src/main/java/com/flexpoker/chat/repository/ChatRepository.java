package com.flexpoker.chat.repository;

import java.util.List;
import java.util.UUID;

import com.flexpoker.web.dto.OutgoingChatMessageDTO;

public interface ChatRepository {

    void saveChatMessage(OutgoingChatMessageDTO chatMessage);

    List<OutgoingChatMessageDTO> fetchAllLobbyChatMessages();

    List<OutgoingChatMessageDTO> fetchAllGameChatMessages(UUID gameId);

    List<OutgoingChatMessageDTO> fetchAllTableChatMessages(UUID tableId);

}
