package com.flexpoker.game.query.repository.impl

import com.flexpoker.config.ProfileNames
import com.flexpoker.game.command.GameStage
import com.flexpoker.game.query.dto.GameInListDTO
import com.flexpoker.game.query.repository.GameListRepository
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.stereotype.Repository
import java.util.UUID
import javax.inject.Inject

@Profile(ProfileNames.REDIS, ProfileNames.GAME_QUERY_REDIS)
@Repository
class RedisGameListRepository @Inject constructor(
    private val redisTemplate: RedisTemplate<String, GameInListDTO>,
) : GameListRepository {

    companion object {
        private const val GAME_LIST_NAMESPACE = "game-list"

        private val GAME_LIST_SCAN_OPTIONS = ScanOptions.scanOptions()
            .match("$GAME_LIST_NAMESPACE:*")
            .count(100)
            .build()
    }

    private fun redisKey(aggregateId: UUID) = "$GAME_LIST_NAMESPACE:$aggregateId"

    override fun saveNew(gameInListDTO: GameInListDTO) {
        redisTemplate.opsForValue().setIfAbsent(redisKey(gameInListDTO.id), gameInListDTO)
    }

    override fun fetchAll(): List<GameInListDTO> {
        val cursor = redisTemplate.scan(GAME_LIST_SCAN_OPTIONS)

        val keys = mutableListOf<String>()
        while (cursor.hasNext()) {
            keys.add(cursor.next())
        }

        return redisTemplate.opsForValue().multiGet(keys)
    }

    override fun incrementRegisteredPlayers(aggregateId: UUID) {
        val existingGameInListDTO = fetchById(aggregateId)
        val updatedGameInListDTO = existingGameInListDTO.copy(
            numberOfRegisteredPlayers = existingGameInListDTO.numberOfRegisteredPlayers + 1)
        removeGame(aggregateId)
        saveNew(updatedGameInListDTO)
    }

    override fun fetchGameName(aggregateId: UUID): String {
        return fetchById(aggregateId).name
    }

    override fun changeGameStage(aggregateId: UUID, gameStage: GameStage) {
        val existingGameInListDTO = fetchById(aggregateId)
        val updatedGameInListDTO = existingGameInListDTO.copy(stage = gameStage.toString())
        removeGame(aggregateId)
        saveNew(updatedGameInListDTO)
    }

    private fun fetchById(aggregateId: UUID): GameInListDTO {
        return redisTemplate.opsForValue()[redisKey(aggregateId)]!!
    }

    private fun removeGame(aggregateId: UUID) {
        redisTemplate.delete(redisKey(aggregateId))
    }

}