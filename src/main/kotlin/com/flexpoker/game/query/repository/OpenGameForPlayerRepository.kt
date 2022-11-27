package com.flexpoker.game.query.repository

import com.flexpoker.game.command.GameStage
import com.flexpoker.game.query.dto.OpenGameForUser
import java.util.UUID

interface OpenGameForPlayerRepository {
    fun fetchAllOpenGamesForPlayer(playerId: UUID): List<OpenGameForUser>?
    fun deleteOpenGameForPlayer(playerId: UUID, gameId: UUID)
    fun addOpenGameForUser(playerId: UUID, gameId: UUID, gameName: String)
    fun changeGameStage(playerId: UUID, gameId: UUID, gameStage: GameStage)
    fun assignTableToOpenGame(playerId: UUID, gameId: UUID, tableId: UUID)
}