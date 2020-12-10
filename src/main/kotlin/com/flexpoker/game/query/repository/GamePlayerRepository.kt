package com.flexpoker.game.query.repository

import java.util.UUID

interface GamePlayerRepository {
    fun addPlayerToGame(playerId: UUID, gameId: UUID)
    fun fetchAllPlayerIdsForGame(gameId: UUID): Set<UUID>
}