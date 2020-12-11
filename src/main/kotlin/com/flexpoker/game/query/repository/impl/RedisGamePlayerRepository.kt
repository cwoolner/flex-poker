package com.flexpoker.game.query.repository.impl

import com.flexpoker.config.ProfileNames
import com.flexpoker.game.query.repository.GamePlayerRepository
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.stream.Collectors
import javax.inject.Inject

@Profile(ProfileNames.REDIS, ProfileNames.GAME_QUERY_REDIS)
@Repository
class RedisGamePlayerRepository @Inject constructor(private val redisTemplate: RedisTemplate<String, String>) :
    GamePlayerRepository {

    companion object {
        private const val GAME_PLAYER_NAMESPACE = "game-player:"
    }

    override fun addPlayerToGame(playerId: UUID, gameId: UUID) {
        redisTemplate.opsForSet().add(redisKey(gameId), playerId.toString())
    }

    override fun fetchAllPlayerIdsForGame(gameId: UUID): Set<UUID> {
        return redisTemplate
            .opsForSet()
            .members(redisKey(gameId))!!
            .stream()
            .map { UUID.fromString(it) }
            .collect(Collectors.toSet())
    }

    private fun redisKey(gameId: UUID): String {
        return GAME_PLAYER_NAMESPACE + gameId
    }

}