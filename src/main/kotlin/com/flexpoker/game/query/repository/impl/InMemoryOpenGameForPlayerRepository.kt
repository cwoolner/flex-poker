package com.flexpoker.game.query.repository.impl

import com.flexpoker.config.ProfileNames
import com.flexpoker.game.command.GameStage
import com.flexpoker.game.query.dto.OpenGameForUser
import com.flexpoker.game.query.repository.OpenGameForPlayerRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Collectors

@Profile(ProfileNames.DEFAULT, ProfileNames.GAME_QUERY_INMEMORY)
@Repository
class InMemoryOpenGameForPlayerRepository : OpenGameForPlayerRepository {

    private val openGameForUserMap: MutableMap<UUID?, MutableMap<UUID?, OpenGameForUser>> = ConcurrentHashMap()

    override fun fetchAllOpenGamesForPlayer(playerId: UUID): List<OpenGameForUser> {
        return openGameForUserMap.getOrDefault(playerId, emptyMap())
            .values
            .stream()
            .sorted(Comparator.comparingInt(OpenGameForUser::ordinal))
            .collect(Collectors.toList())
    }

    override fun deleteOpenGameForPlayer(playerId: UUID, gameId: UUID) {
        openGameForUserMap[playerId]!!.remove(gameId)
    }

    override fun addOpenGameForUser(playerId: UUID, gameId: UUID, gameName: String) {
        openGameForUserMap.putIfAbsent(playerId, HashMap())
        val openGameForUser = OpenGameForUser(gameId, null, gameName, GameStage.REGISTERING,
            openGameForUserMap[playerId]!!.size, emptyList())
        openGameForUserMap[playerId]!![gameId] = openGameForUser
    }

    override fun changeGameStage(playerId: UUID, gameId: UUID, gameStage: GameStage) {
        val openGameForUser = openGameForUserMap[playerId]!![gameId]!!
        val updatedOpenGameForUser = openGameForUser.copy(gameStage = gameStage)
        openGameForUserMap[playerId]!![gameId] = updatedOpenGameForUser
    }

    override fun assignTableToOpenGame(playerId: UUID, gameId: UUID, tableId: UUID) {
        val openGameForUser = openGameForUserMap[playerId]!![gameId]!!
        val updatedOpenGameForUser = openGameForUser.copy(myTableId = tableId)
        openGameForUserMap[playerId]!![gameId] = updatedOpenGameForUser
    }

}