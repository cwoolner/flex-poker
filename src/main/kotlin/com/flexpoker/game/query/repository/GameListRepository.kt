package com.flexpoker.game.query.repository

import com.flexpoker.game.query.dto.GameInListDTO
import com.flexpoker.game.query.dto.GameStage
import java.util.UUID

interface GameListRepository {
    fun saveNew(gameInListDTO: GameInListDTO)
    fun fetchAll(): List<GameInListDTO>
    fun incrementRegisteredPlayers(aggregateId: UUID)
    fun fetchGameName(aggregateId: UUID): String
    fun changeGameStage(aggregateId: UUID, gameStage: GameStage)
}