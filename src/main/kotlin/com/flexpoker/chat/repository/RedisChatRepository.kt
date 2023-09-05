package com.flexpoker.chat.repository

import com.flexpoker.config.ProfileNames
import com.flexpoker.web.dto.OutgoingChatMessageDTO
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.UUID
import javax.inject.Inject

@Profile(ProfileNames.REDIS, ProfileNames.CHAT_REDIS)
@Repository
class RedisChatRepository @Inject constructor(
    private val redisTemplate: RedisTemplate<String, OutgoingChatMessageDTO>,
) : ChatRepository {

    companion object {
        private const val LOBBY_CHAT_KEY = "chat:lobby"
        private const val GAME_CHAT_NAMESPACE = "chat:game"
        private const val TABLE_CHAT_NAMESPACE = "chat:table"
    }

    private fun gameChatRedisKey(gameId: UUID) = "$GAME_CHAT_NAMESPACE:$gameId"
    private fun tableChatRedisKey(tableId: UUID) = "$TABLE_CHAT_NAMESPACE:$tableId"

    override fun saveChatMessage(chatMessage: OutgoingChatMessageDTO) {
        val gameId = chatMessage.gameId
        val tableId = chatMessage.tableId
        if (gameId != null && tableId != null) {
            redisTemplate.opsForList().rightPush(tableChatRedisKey(tableId), chatMessage)
        } else if (gameId != null) {
            redisTemplate.opsForList().rightPush(gameChatRedisKey(gameId), chatMessage)
        } else {
            redisTemplate.opsForList().rightPush(LOBBY_CHAT_KEY, chatMessage)
        }
    }

    override fun fetchAllLobbyChatMessages(): List<OutgoingChatMessageDTO> {
        return redisTemplate.opsForList().range(LOBBY_CHAT_KEY, 0, Long.MAX_VALUE)!!
    }

    override fun fetchAllGameChatMessages(gameId: UUID): List<OutgoingChatMessageDTO> {
        return redisTemplate.opsForList().range(gameChatRedisKey(gameId), 0, Long.MAX_VALUE)!!
    }

    override fun fetchAllTableChatMessages(tableId: UUID): List<OutgoingChatMessageDTO> {
        return redisTemplate.opsForList().range(tableChatRedisKey(tableId), 0, Long.MAX_VALUE)!!
    }

}