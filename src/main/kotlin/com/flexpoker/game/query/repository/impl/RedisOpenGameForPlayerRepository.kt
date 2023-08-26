package com.flexpoker.game.query.repository.impl

import com.flexpoker.config.ProfileNames
import com.flexpoker.game.command.GameStage
import com.flexpoker.game.query.dto.OpenGameForUser
import com.flexpoker.game.query.repository.OpenGameForPlayerRepository
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.stream.Collectors
import java.util.stream.IntStream
import javax.inject.Inject

@Profile(ProfileNames.REDIS, ProfileNames.GAME_QUERY_REDIS)
@Repository
class RedisOpenGameForPlayerRepository @Inject constructor(
    private val redisTemplate: RedisTemplate<String, OpenGameForUser>,
) : OpenGameForPlayerRepository {

    companion object {
        private const val OPEN_GAME_FOR_PLAYER_NAMESPACE = "open-game-for-player:"
    }

    override fun fetchAllOpenGamesForPlayer(playerId: UUID): List<OpenGameForUser> {
        return redisTemplate
            .opsForList()
            .range(redisKey(playerId), 0, Long.MAX_VALUE)!!
            .stream()
            .sorted(Comparator.comparingInt(OpenGameForUser::ordinal))
            .collect(Collectors.toList())
    }

    override fun deleteOpenGameForPlayer(playerId: UUID, gameId: UUID) {
        val openGameForPlayer = fetchOpenGameForPlayer(playerId, gameId)!!
        redisTemplate.opsForList().remove(redisKey(playerId), 1, openGameForPlayer)
        val removedOrdinal = openGameForPlayer.ordinal
        val gamesToDecrementOrdinal = fetchAllOpenGamesForPlayer(playerId)
            .stream()
            .filter { it.ordinal > removedOrdinal }
            .map { it.copy(ordinal = it.ordinal - 1) }
            .collect(Collectors.toList())
        for (decrementedOrdinal in gamesToDecrementOrdinal) {
            updateOpenGameForPlayer(playerId, decrementedOrdinal)
        }
    }

    override fun addOpenGameForUser(playerId: UUID, gameId: UUID, gameName: String) {
        val openGamesForPlayer = fetchAllOpenGamesForPlayer(playerId)
        val openGameForUser = OpenGameForUser(gameId, null, gameName, GameStage.REGISTERING,
            openGamesForPlayer.size, emptyList())
        redisTemplate.opsForList().rightPush(redisKey(playerId), openGameForUser)
    }

    override fun changeGameStage(playerId: UUID, gameId: UUID, gameStage: GameStage) {
        val openGameForPlayer = fetchOpenGameForPlayer(playerId, gameId)!!
        val updatedOpenGameForPlayer = openGameForPlayer.copy(gameStage = gameStage)
        updateOpenGameForPlayer(playerId, updatedOpenGameForPlayer)
    }

    override fun assignTableToOpenGame(playerId: UUID, gameId: UUID, tableId: UUID) {
        val openGameForPlayer = fetchOpenGameForPlayer(playerId, gameId)!!
        val updatedOpenGameForPlayer = openGameForPlayer.copy(myTableId = tableId)
        updateOpenGameForPlayer(playerId, updatedOpenGameForPlayer)
    }

    private fun redisKey(playerId: UUID): String =  OPEN_GAME_FOR_PLAYER_NAMESPACE + playerId

    private fun fetchOpenGameForPlayer(playerId: UUID, gameId: UUID): OpenGameForUser? {
        return fetchAllOpenGamesForPlayer(playerId)
            .stream()
            .filter { it.gameId == gameId }
            .findFirst()
            .orElse(null)
    }

    private fun updateOpenGameForPlayer(playerId: UUID, openGameForPlayer: OpenGameForUser) {
        val openGames = redisTemplate.opsForList().range(redisKey(playerId), 0, Long.MAX_VALUE)!!
        val index = IntStream.range(0, openGames.size)
            .filter { openGames[it].gameId == openGameForPlayer.gameId }
            .findFirst()
            .asInt
        redisTemplate.opsForList()[redisKey(playerId), index.toLong()] = openGameForPlayer
    }

}