package com.flexpoker.game.query.repository.impl

import com.flexpoker.config.ProfileNames
import com.flexpoker.game.query.dto.GameInListDTO
import com.flexpoker.game.command.GameStage
import com.flexpoker.game.query.repository.GameListRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import java.util.ArrayList
import java.util.UUID

@Profile(ProfileNames.DEFAULT, ProfileNames.GAME_QUERY_INMEMORY)
@Repository
class InMemoryGameListRepository : GameListRepository {

    private val gameInListDTOList: MutableList<GameInListDTO> = ArrayList()

    override fun saveNew(gameInListDTO: GameInListDTO) {
        gameInListDTOList.add(gameInListDTO)
    }

    override fun fetchAll(): List<GameInListDTO> {
        return ArrayList(gameInListDTOList)
    }

    override fun incrementRegisteredPlayers(aggregateId: UUID) {
        val (id, name, stage, numberOfRegisteredPlayers, maxNumberOfPlayers, maxPlayersPerTable,
            blindLevelIncreaseInMinutes, actionOnTimerInSeconds, createdBy, createdOn) = fetchById(aggregateId)
        val updatedGameInListDTO = GameInListDTO(id, name, stage, numberOfRegisteredPlayers + 1,
            maxNumberOfPlayers, maxPlayersPerTable, blindLevelIncreaseInMinutes, actionOnTimerInSeconds,
            createdBy, createdOn)
        removeGame(aggregateId)
        saveNew(updatedGameInListDTO)
    }

    override fun fetchGameName(aggregateId: UUID): String {
        return fetchById(aggregateId).name
    }

    override fun changeGameStage(aggregateId: UUID, gameStage: GameStage) {
        val (id, name, _, numberOfRegisteredPlayers, maxNumberOfPlayers, maxPlayersPerTable, blindLevelIncreaseInMinutes,
            actionOnTimerInSeconds, createdBy, createdOn) = fetchById(aggregateId)
        val updatedGameInListDTO = GameInListDTO(id, name, gameStage.toString(), numberOfRegisteredPlayers,
            maxNumberOfPlayers, maxPlayersPerTable, blindLevelIncreaseInMinutes, actionOnTimerInSeconds,
            createdBy, createdOn)
        removeGame(aggregateId)
        saveNew(updatedGameInListDTO)
    }

    private fun fetchById(aggregateId: UUID): GameInListDTO {
        return gameInListDTOList.stream()
            .filter { x: GameInListDTO? -> x!!.id === aggregateId }
            .findAny()
            .get()
    }

    private fun removeGame(aggregateId: UUID) {
        gameInListDTOList.removeIf { x: GameInListDTO? -> x!!.id == aggregateId }
    }

}