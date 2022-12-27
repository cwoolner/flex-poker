package com.flexpoker.game.query.repository.impl

import com.flexpoker.config.ProfileNames
import com.flexpoker.game.query.repository.GamePlayerRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import java.util.UUID

@Profile(ProfileNames.DEFAULT, ProfileNames.GAME_QUERY_INMEMORY)
@Repository
class InMemoryGamePlayerRepository : GamePlayerRepository {

    private val gameIdToPlayerId: MutableMap<UUID, MutableSet<UUID>> = HashMap()

    override fun addPlayerToGame(playerId: UUID, gameId: UUID) {
        gameIdToPlayerId.putIfAbsent(gameId, HashSet())
        gameIdToPlayerId[gameId]!!.add(playerId)
    }

    override fun fetchAllPlayerIdsForGame(gameId: UUID): Set<UUID> {
        return gameIdToPlayerId[gameId] ?: HashSet()
    }

}