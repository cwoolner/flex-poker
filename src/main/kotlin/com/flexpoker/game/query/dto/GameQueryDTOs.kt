package com.flexpoker.game.query.dto

import com.flexpoker.game.command.GameStage
import java.util.UUID

data class GameInListDTO (val id: UUID, val name: String, val stage: String,
                          val numberOfRegisteredPlayers: Int, val maxNumberOfPlayers: Int,
                          val maxPlayersPerTable: Int, val blindLevelIncreaseInMinutes: Int,
                          val actionOnTimerInSeconds: Int, val createdBy: String, val createdOn: String)

data class OpenGameForUser (val gameId: UUID, val myTableId: UUID?, val name: String,
                            val gameStage: GameStage, val ordinal: Int, val viewingTables: List<String>)