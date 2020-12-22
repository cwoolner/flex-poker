package com.flexpoker.game.command.aggregate

import com.flexpoker.game.command.commands.CreateGameCommand
import com.flexpoker.game.command.events.GameCreatedEvent
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.factory.GameFactory
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class DefaultGameFactory : GameFactory {

    override fun createNew(command: CreateGameCommand): Game {
        return createGame(
            false, UUID.randomUUID(), command.gameName,
            command.numberOfPlayers,
            command.numberOfPlayersPerTable,
            command.createdByPlayerId,
            command.numberOfMinutesBetweenBlindLevels,
            command.numberOfSecondsForActionOnTimer
        )
    }

    override fun createFrom(events: List<GameEvent>): Game {
        val gameCreatedEvent = events[0] as GameCreatedEvent
        val game = createGame(
            true, gameCreatedEvent.aggregateId,
            gameCreatedEvent.gameName,
            gameCreatedEvent.numberOfPlayers,
            gameCreatedEvent.numberOfPlayersPerTable,
            gameCreatedEvent.createdByPlayerId,
            gameCreatedEvent.numberOfMinutesBetweenBlindLevels,
            gameCreatedEvent.numberOfSecondsForActionOnTimer
        )
        game.applyAllHistoricalEvents(events)
        return game
    }

    private fun createGame(
        creatingFromEvents: Boolean, aggregateId: UUID,
        gameName: String, maxNumberOfPlayers: Int,
        numberOfPlayersPerTable: Int, createdById: UUID,
        numberOfMinutesBetweenBlindLevels: Int,
        numberOfSecondsForActionOnTimer: Int
    ): Game {
        return Game(creatingFromEvents, aggregateId, gameName, maxNumberOfPlayers, numberOfPlayersPerTable,
            numberOfSecondsForActionOnTimer, createdById, numberOfMinutesBetweenBlindLevels)
    }
}