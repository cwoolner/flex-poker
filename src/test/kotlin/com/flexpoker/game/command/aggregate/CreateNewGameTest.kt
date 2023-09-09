package com.flexpoker.game.command.aggregate

import com.flexpoker.game.command.aggregate.eventproducers.createGame
import com.flexpoker.game.command.commands.CreateGameCommand
import com.flexpoker.game.command.events.GameCreatedEvent
import com.flexpoker.test.util.UnitTestClass
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.util.UUID

@UnitTestClass
class CreateNewGameTest {

    @Test
    fun testCreateNewGameSuccess() {
        val playerId = UUID.randomUUID()
        val command = CreateGameCommand("test", 2, 2, playerId,
            10, 20)
        val newEvents = createGame(command.gameName, command.numberOfPlayers, command.numberOfPlayersPerTable,
            command.createdByPlayerId, command.numberOfMinutesBetweenBlindLevels, command.numberOfSecondsForActionOnTimer)
        assertEquals(1, newEvents.size)
        val gameCreatedEvent = newEvents[0] as GameCreatedEvent
        assertEquals("test", gameCreatedEvent.gameName)
        assertEquals(2, gameCreatedEvent.numberOfPlayers)
        assertEquals(2, gameCreatedEvent.numberOfPlayersPerTable)
        assertEquals(playerId, gameCreatedEvent.createdByPlayerId)
        assertEquals(10, gameCreatedEvent.numberOfMinutesBetweenBlindLevels)
        assertEquals(20, gameCreatedEvent.numberOfSecondsForActionOnTimer)
        assertNotNull(gameCreatedEvent.gameId)
        assertNotNull(gameCreatedEvent.aggregateId)
    }

}