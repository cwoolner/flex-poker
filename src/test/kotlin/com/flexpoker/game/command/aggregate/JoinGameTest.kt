package com.flexpoker.game.command.aggregate

import com.flexpoker.game.command.aggregate.eventproducers.joinGame
import com.flexpoker.game.command.events.GameCreatedEvent
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent
import com.flexpoker.game.command.events.GameStartedEvent
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent
import com.flexpoker.test.util.EventProducerApplierBuilder
import com.flexpoker.test.util.UnitTestClass
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.UUID

@UnitTestClass
class JoinGameTest {

    @Test
    fun testJoinGameSuccessFirstPlayerJoins() {
        val gameCreatedEvent = GameCreatedEvent(UUID.randomUUID(), "test", 2, 2,
            UUID.randomUUID(), 10, 20)
        val (_, events) = EventProducerApplierBuilder<GameState, GameEvent>()
            .initState(gameCreatedEvent)
            .andRun { joinGame(it, UUID.randomUUID()) }
            .run()
        assertEquals(1, events.size)
    }

    @Test
    fun testJoinGameSuccessGameStarting() {
        val gameCreatedEvent = GameCreatedEvent(UUID.randomUUID(), "test", 2,
            2, UUID.randomUUID(), 10, 20)

        val (_, events) = EventProducerApplierBuilder<GameState, GameEvent>()
            .initState(gameCreatedEvent)
            .andRun { joinGame(it, UUID.randomUUID()) }
            .andRun { joinGame(it, UUID.randomUUID()) }
            .run()

        assertEquals(5, events.size)
        assertEquals(GameMovedToStartingStageEvent::class.java, events[2].javaClass)
        assertEquals(GameTablesCreatedAndPlayersAssociatedEvent::class.java, events[3].javaClass)
        assertEquals(GameStartedEvent::class.java, events[4].javaClass)
    }

    @Test
    fun testJoinGameAttemptToJoinTwice() {
        val gameCreatedEvent = GameCreatedEvent(UUID.randomUUID(), "test", 2,
            2, UUID.randomUUID(), 10, 20)
        val playerId = UUID.randomUUID()

        EventProducerApplierBuilder<GameState, GameEvent>()
            .initState(gameCreatedEvent)
            .andRun { joinGame(it, playerId) }
            .andRunThrows(IllegalArgumentException::class.java) { joinGame(it, playerId) }
            .run()
    }

    @Test
    fun testJoinGameAttemptToJoinMoreThanMax() {
        val gameCreatedEvent = GameCreatedEvent(UUID.randomUUID(), "test", 2,
            2, UUID.randomUUID(), 10, 20)
        EventProducerApplierBuilder<GameState, GameEvent>()
            .initState(gameCreatedEvent)
            .andRun { joinGame(it, UUID.randomUUID()) }
            .andRun { joinGame(it, UUID.randomUUID()) }
            .andRunThrows(IllegalArgumentException::class.java) { joinGame(it, UUID.randomUUID()) }
    }

}