package com.flexpoker.chat.repository;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.flexpoker.config.ProfileNames;
import com.flexpoker.web.dto.outgoing.ChatMessageDTO;

@Profile({ ProfileNames.REDIS, ProfileNames.CHAT_REDIS })
@Repository
public class RedisChatRepository implements ChatRepository {

    private static final String LOBBY_CHAT_NAMESPACE = "chat:lobby";

    private static final String GAME_CHAT_NAMESPACE = "chat:game:";

    private static final String TABLE_CHAT_NAMESPACE = "chat:table:";

    private final RedisTemplate<String, ChatMessageDTO> redisTemplate;

    @Inject
    public RedisChatRepository(RedisTemplate<String, ChatMessageDTO> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveChatMessage(ChatMessageDTO chatMessage) {
        var gameId = chatMessage.getGameId();
        var tableId = chatMessage.getTableId();
        if (gameId != null && tableId != null) {
            redisTemplate.opsForList().rightPush(TABLE_CHAT_NAMESPACE + tableId, chatMessage);
        } else if (gameId != null) {
            redisTemplate.opsForList().rightPush(GAME_CHAT_NAMESPACE + gameId, chatMessage);
        } else {
            redisTemplate.opsForList().rightPush(LOBBY_CHAT_NAMESPACE, chatMessage);
        }
    }

    @Override
    public List<ChatMessageDTO> fetchAllLobbyChatMessages() {
        return redisTemplate.opsForList().range(LOBBY_CHAT_NAMESPACE, 0, Long.MAX_VALUE);
    }

    @Override
    public List<ChatMessageDTO> fetchAllGameChatMessages(UUID gameId) {
        return redisTemplate.opsForList().range(GAME_CHAT_NAMESPACE + gameId, 0, Long.MAX_VALUE);
    }

    @Override
    public List<ChatMessageDTO> fetchAllTableChatMessages(UUID tableId) {
        return redisTemplate.opsForList().range(TABLE_CHAT_NAMESPACE + tableId, 0, Long.MAX_VALUE);
    }

}
