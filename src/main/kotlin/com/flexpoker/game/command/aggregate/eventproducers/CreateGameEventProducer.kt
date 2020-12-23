package com.flexpoker.game.command.aggregate.eventproducers

import com.flexpoker.game.command.events.GameCreatedEvent
import com.flexpoker.game.command.events.GameEvent
import java.util.UUID

fun createGame(gameName: String, maxNumberOfPlayers: Int, numberOfPlayersPerTable: Int,
               createdById: UUID, numberOfMinutesBetweenBlindLevels: Int, numberOfSecondsForActionOnTimer: Int
): List<GameEvent> {
    return listOf(GameCreatedEvent(UUID.randomUUID(), gameName, maxNumberOfPlayers, numberOfPlayersPerTable,
        createdById, numberOfMinutesBetweenBlindLevels, numberOfSecondsForActionOnTimer))
}