package com.flexpoker.game.command.aggregate.attempttostartnewhand

import com.flexpoker.game.command.aggregate.GameState
import com.flexpoker.game.command.aggregate.eventproducers.attemptToStartNewHand
import com.flexpoker.game.command.aggregate.eventproducers.createGame
import com.flexpoker.game.command.aggregate.eventproducers.joinGame
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent
import com.flexpoker.game.command.events.PlayerBustedGameEvent
import com.flexpoker.test.util.EventProducerApplierBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.UUID

class PlayerBustedTest {

    @Test
    fun testEventIsCreated() {
        val player1Id = UUID.randomUUID()
        val player2Id = UUID.randomUUID()
        val player3Id = UUID.randomUUID()

        val gameCreatedEvent = createGame("test", 3, 3, player1Id,
            1, 20).first()

        val (state, events) = EventProducerApplierBuilder<GameState, GameEvent>()
            .initState(gameCreatedEvent)
            .andRun { joinGame(it, player1Id) }
            .andRun { joinGame(it, player2Id) }
            .andRun { joinGame(it, player3Id) }
            .run()

        val (_, tableIdToPlayerIdsMap) = events.first { it.javaClass == GameTablesCreatedAndPlayersAssociatedEvent::class.java }
                as GameTablesCreatedAndPlayersAssociatedEvent
        val tableId = tableIdToPlayerIdsMap.keys.iterator().next()
        val playerToChipsMap = mapOf(player1Id to 100, player2Id to 0, player3Id to 100)

        val newEvents = attemptToStartNewHand(state, tableId, playerToChipsMap)
        assertEquals(2, newEvents.size)
        assertEquals(player2Id, (newEvents[0] as PlayerBustedGameEvent).playerId)
    }

    @Test
    fun testRemovedUserFromTableCreatesEvent() {
        val player1Id = UUID.randomUUID()
        val player2Id = UUID.randomUUID()
        val player3Id = UUID.randomUUID()

        val gameCreatedEvent = createGame("test", 3, 3, player1Id,
            1, 20).first()

        val (state, events) = EventProducerApplierBuilder<GameState, GameEvent>()
            .initState(gameCreatedEvent)
            .andRun { joinGame(it, player1Id) }
            .andRun { joinGame(it, player2Id) }
            .andRun { joinGame(it, player3Id) }
            .run()

        val (_, tableIdToPlayerIdsMap) = events.first { it.javaClass == GameTablesCreatedAndPlayersAssociatedEvent::class.java }
                as GameTablesCreatedAndPlayersAssociatedEvent
        val tableId = tableIdToPlayerIdsMap.keys.iterator().next()
        val playerToChipsMap = mapOf(player1Id to 100, player3Id to 100)

        val newEvents = attemptToStartNewHand(state, tableId, playerToChipsMap)
        assertEquals(2, newEvents.size)
        assertEquals(player2Id, (newEvents[0] as PlayerBustedGameEvent).playerId)
    }

    @Test
    fun testMultiplePlayersBust() {
        val player1Id = UUID.randomUUID()
        val player2Id = UUID.randomUUID()
        val player3Id = UUID.randomUUID()

        val gameCreatedEvent = createGame("test", 3, 3, player1Id,
            1, 20).first()

        val (state, events) = EventProducerApplierBuilder<GameState, GameEvent>()
            .initState(gameCreatedEvent)
            .andRun { joinGame(it, player1Id) }
            .andRun { joinGame(it, player2Id) }
            .andRun { joinGame(it, player3Id) }
            .run()

        val (_, tableIdToPlayerIdsMap) = events.first { it.javaClass == GameTablesCreatedAndPlayersAssociatedEvent::class.java }
                as GameTablesCreatedAndPlayersAssociatedEvent
        val tableId = tableIdToPlayerIdsMap.keys.iterator().next()
        val playerToChipsMap = mapOf(player1Id to 0, player2Id to 0, player3Id to 100)

        val newEvents = attemptToStartNewHand(state, tableId, playerToChipsMap)
        assertEquals(2, newEvents.size)
        val bustedPlayers = setOf(
            (newEvents[0] as PlayerBustedGameEvent).playerId,
            (newEvents[1] as PlayerBustedGameEvent).playerId
        )
        assertTrue(bustedPlayers.contains(player1Id))
        assertTrue(bustedPlayers.contains(player2Id))
    }

    @Test
    fun testInvalidPlayer() {
        val player1Id = UUID.randomUUID()
        val player2Id = UUID.randomUUID()
        val player3Id = UUID.randomUUID()
        val player4Id = UUID.randomUUID()

        val gameCreatedEvent = createGame("test", 3, 3, player1Id,
            1, 20).first()

        val (state, events) = EventProducerApplierBuilder<GameState, GameEvent>()
            .initState(gameCreatedEvent)
            .andRun { joinGame(it, player1Id) }
            .andRun { joinGame(it, player2Id) }
            .andRun { joinGame(it, player3Id) }
            .run()

        val playerToChipsMap = mapOf(player1Id to 0, player2Id to 0, player3Id to 100, player4Id to 0)
        val (_, tableIdToPlayerIdsMap) = events.first { it.javaClass == GameTablesCreatedAndPlayersAssociatedEvent::class.java }
                as GameTablesCreatedAndPlayersAssociatedEvent
        val tableId = tableIdToPlayerIdsMap.keys.iterator().next()
        assertThrows(IllegalArgumentException::class.java) { attemptToStartNewHand(state, tableId, playerToChipsMap) }
    }

    @Test
    fun testPlayerBustsTwice() {
        val player1Id = UUID.randomUUID()
        val player2Id = UUID.randomUUID()
        val player3Id = UUID.randomUUID()

        val gameCreatedEvent = createGame("test", 3, 3, player1Id,
            1, 20).first()

        val (state, events) = EventProducerApplierBuilder<GameState, GameEvent>()
            .initState(gameCreatedEvent)
            .andRun { joinGame(it, player1Id) }
            .andRun { joinGame(it, player2Id) }
            .andRun { joinGame(it, player3Id) }
            .run()

        val (_, tableIdToPlayerIdsMap) = events.first { it.javaClass == GameTablesCreatedAndPlayersAssociatedEvent::class.java } as GameTablesCreatedAndPlayersAssociatedEvent
        val tableId = tableIdToPlayerIdsMap.keys.iterator().next()
        val playerToChipsMapFirst = mapOf(player1Id to 0, player2Id to 0, player3Id to 100)
        val playerToChipsMapSecond = mapOf(player1Id to 0, player2Id to 0, player3Id to 100)

        val (_, _) = EventProducerApplierBuilder<GameState, GameEvent>()
            .initState(state)
            .andRun { attemptToStartNewHand(it, tableId, playerToChipsMapFirst) }
            .andRunThrows(IllegalArgumentException::class.java) { attemptToStartNewHand(it, tableId, playerToChipsMapSecond) }
            .run()
    }

}