package com.flexpoker.game.command.aggregate

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.game.command.events.GameCreatedEvent
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent
import com.flexpoker.game.command.events.GameStartedEvent
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.ArrayList
import java.util.UUID

class JoinGameTest {

    @Test
    fun testJoinGameSuccessFirstPlayerJoins() {
        val events = ArrayList<GameEvent>()
        events.add(GameCreatedEvent(UUID.randomUUID(), "test", 2, 2, UUID.randomUUID(), 10, 20))
        val game = DefaultGameFactory().createFrom(events)
        game.joinGame(UUID.randomUUID())
        assertEquals(2, game.fetchAppliedEvents().size)
        assertEquals(1, game.fetchNewEvents().size)
    }

    @Test
    fun testJoinGameSuccessGameStarting() {
        val events = ArrayList<GameEvent>()
        events.add(GameCreatedEvent(UUID.randomUUID(), "test", 2, 2, UUID.randomUUID(), 10, 20))
        val game = DefaultGameFactory().createFrom(events)
        game.joinGame(UUID.randomUUID())
        game.joinGame(UUID.randomUUID())
        assertEquals(6, game.fetchAppliedEvents().size)
        assertEquals(5, game.fetchNewEvents().size)
        assertEquals(GameMovedToStartingStageEvent::class.java, game.fetchNewEvents()[2].javaClass)
        assertEquals(GameTablesCreatedAndPlayersAssociatedEvent::class.java, game.fetchNewEvents()[3].javaClass)
        assertEquals(GameStartedEvent::class.java, game.fetchNewEvents()[4].javaClass)
    }

    @Test
    fun testJoinGameAttemptToJoinTwice() {
        val events = ArrayList<GameEvent>()
        events.add(GameCreatedEvent(UUID.randomUUID(), "test", 2, 2, UUID.randomUUID(), 10, 20))
        val playerId = UUID.randomUUID()
        val game = DefaultGameFactory().createFrom(events)
        game.joinGame(playerId)
        assertThrows(FlexPokerException::class.java) { game.joinGame(playerId) }
    }

    @Test
    fun testJoinGameAttemptToJoinMoreThanMax() {
        val events = ArrayList<GameEvent>()
        events.add(GameCreatedEvent(UUID.randomUUID(), "test", 2, 2, UUID.randomUUID(), 10, 20))
        val game = DefaultGameFactory().createFrom(events)
        game.joinGame(UUID.randomUUID())
        game.joinGame(UUID.randomUUID())
        assertThrows(FlexPokerException::class.java) { game.joinGame(UUID.randomUUID()) }
    }

}