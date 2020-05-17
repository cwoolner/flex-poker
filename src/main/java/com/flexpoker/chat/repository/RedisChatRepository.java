package com.flexpoker.chat.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.flexpoker.config.ProfileNames;
import com.flexpoker.web.dto.outgoing.ChatMessageDTO;

@Profile({ ProfileNames.REDIS, ProfileNames.CHAT_REDIS })
@Repository
public class RedisChatRepository implements ChatRepository {

    @Override
    public void saveChatMessage(ChatMessageDTO chatMessage) {
        // TODO Auto-generated method stub
    }

    @Override
    public List<ChatMessageDTO> fetchAllGlobalChatMessages() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ChatMessageDTO> fetchAllGameChatMessages(UUID gameId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ChatMessageDTO> fetchAllTableChatMessages(UUID tableId) {
        // TODO Auto-generated method stub
        return null;
    }

}
