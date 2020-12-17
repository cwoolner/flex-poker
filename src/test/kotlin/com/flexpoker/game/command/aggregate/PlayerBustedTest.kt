package com.flexpoker.game.command.aggregate

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.game.command.commands.CreateGameCommand
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent
import com.flexpoker.game.command.events.PlayerBustedGameEvent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.HashMap
import java.util.HashSet
import java.util.UUID

class PlayerBustedTest {

    @Test
    fun testEventIsCreated() {
        val player1Id = UUID.randomUUID()
        val player2Id = UUID.randomUUID()
        val player3Id = UUID.randomUUID()
        val createGameCommand = CreateGameCommand("test", 3, 3, player1Id, 1, 20)
        val game = DefaultGameFactory().createNew(createGameCommand)
        game.joinGame(player1Id)
        game.joinGame(player2Id)
        game.joinGame(player3Id)
        val (_, tableIdToPlayerIdsMap) = game
            .fetchAppliedEvents().stream()
            .filter { x: GameEvent -> x.javaClass == GameTablesCreatedAndPlayersAssociatedEvent::class.java }
            .findFirst().get() as GameTablesCreatedAndPlayersAssociatedEvent
        val tableId = tableIdToPlayerIdsMap
            .keys.iterator().next()
        val playerToChipsMap = HashMap<UUID, Int>()
        playerToChipsMap[player1Id] = 100
        playerToChipsMap[player2Id] = 0
        playerToChipsMap[player3Id] = 100
        game.attemptToStartNewHand(tableId, playerToChipsMap)
        assertEquals(9, game.fetchAppliedEvents().size)
        assertEquals(9, game.fetchNewEvents().size)
        assertEquals(PlayerBustedGameEvent::class.java, game.fetchAppliedEvents()[7].javaClass)
        assertEquals(player2Id, (game.fetchAppliedEvents()[7] as PlayerBustedGameEvent).playerId)
    }

    @Test
    fun testRemovedUserFromTableCreatesEvent() {
        val player1Id = UUID.randomUUID()
        val player2Id = UUID.randomUUID()
        val player3Id = UUID.randomUUID()
        val createGameCommand = CreateGameCommand("test", 3, 3, player1Id, 1, 20)
        val game = DefaultGameFactory().createNew(createGameCommand)
        game.joinGame(player1Id)
        game.joinGame(player2Id)
        game.joinGame(player3Id)
        val (_, tableIdToPlayerIdsMap) = game
            .fetchAppliedEvents().stream()
            .filter { x: GameEvent -> x.javaClass == GameTablesCreatedAndPlayersAssociatedEvent::class.java }
            .findFirst().get() as GameTablesCreatedAndPlayersAssociatedEvent
        val tableId = tableIdToPlayerIdsMap.keys.iterator().next()
        val playerToChipsMap = HashMap<UUID, Int>()
        playerToChipsMap[player1Id] = 100
        playerToChipsMap[player3Id] = 100
        game.attemptToStartNewHand(tableId, playerToChipsMap)
        assertEquals(9, game.fetchAppliedEvents().size)
        assertEquals(9, game.fetchNewEvents().size)
        assertEquals(PlayerBustedGameEvent::class.java, game.fetchAppliedEvents()[7].javaClass)
        assertEquals(player2Id, (game.fetchAppliedEvents()[7] as PlayerBustedGameEvent).playerId)
    }

    @Test
    fun testMultiplePlayersBust() {
        val player1Id = UUID.randomUUID()
        val player2Id = UUID.randomUUID()
        val player3Id = UUID.randomUUID()
        val createGameCommand = CreateGameCommand("test", 3, 3, player1Id, 1, 20)
        val game = DefaultGameFactory().createNew(createGameCommand)
        game.joinGame(player1Id)
        game.joinGame(player2Id)
        game.joinGame(player3Id)
        val (_, tableIdToPlayerIdsMap) = game
            .fetchAppliedEvents().stream()
            .filter { x: GameEvent -> x.javaClass == GameTablesCreatedAndPlayersAssociatedEvent::class.java }
            .findFirst().get() as GameTablesCreatedAndPlayersAssociatedEvent
        val tableId = tableIdToPlayerIdsMap.keys.iterator().next()
        val playerToChipsMap = HashMap<UUID, Int>()
        playerToChipsMap[player1Id] = 0
        playerToChipsMap[player2Id] = 0
        playerToChipsMap[player3Id] = 100
        game.attemptToStartNewHand(tableId, playerToChipsMap)
        assertEquals(9, game.fetchAppliedEvents().size)
        assertEquals(9, game.fetchNewEvents().size)
        assertEquals(PlayerBustedGameEvent::class.java, game.fetchAppliedEvents()[7].javaClass)
        assertEquals(PlayerBustedGameEvent::class.java, game.fetchAppliedEvents()[8].javaClass)
        val bustedPlayers = HashSet<Any>()
        bustedPlayers.add((game.fetchAppliedEvents()[7] as PlayerBustedGameEvent).playerId)
        bustedPlayers.add((game.fetchAppliedEvents()[8] as PlayerBustedGameEvent).playerId)
        assertTrue(bustedPlayers.contains(player1Id))
        assertTrue(bustedPlayers.contains(player2Id))
    }

    @Test
    fun testInvalidPlayer() {
        val player1Id = UUID.randomUUID()
        val player2Id = UUID.randomUUID()
        val player3Id = UUID.randomUUID()
        val player4Id = UUID.randomUUID()
        val createGameCommand = CreateGameCommand("test", 3, 3, player1Id, 1, 20)
        val game = DefaultGameFactory().createNew(createGameCommand)
        game.joinGame(player1Id)
        game.joinGame(player2Id)
        game.joinGame(player3Id)
        val playerToChipsMap = HashMap<UUID, Int>()
        playerToChipsMap[player1Id] = 0
        playerToChipsMap[player2Id] = 0
        playerToChipsMap[player3Id] = 100
        playerToChipsMap[player4Id] = 0
        val (_, tableIdToPlayerIdsMap) = game
            .fetchAppliedEvents()
            .first { it.javaClass == GameTablesCreatedAndPlayersAssociatedEvent::class.java }
            as GameTablesCreatedAndPlayersAssociatedEvent
        val tableId = tableIdToPlayerIdsMap.keys.iterator().next()
        Assertions.assertThrows(FlexPokerException::class.java) {
            game.attemptToStartNewHand(tableId, playerToChipsMap)
        }
    }

    @Test
    fun testPlayerBustsTwice() {
        val player1Id = UUID.randomUUID()
        val player2Id = UUID.randomUUID()
        val player3Id = UUID.randomUUID()
        val createGameCommand = CreateGameCommand("test", 3, 3, player1Id, 1, 20)
        val game = DefaultGameFactory().createNew(createGameCommand)
        game.joinGame(player1Id)
        game.joinGame(player2Id)
        game.joinGame(player3Id)
        val (_, tableIdToPlayerIdsMap) = game
            .fetchAppliedEvents().stream()
            .filter { x: GameEvent -> x.javaClass == GameTablesCreatedAndPlayersAssociatedEvent::class.java }
            .findFirst().get() as GameTablesCreatedAndPlayersAssociatedEvent
        val tableId = tableIdToPlayerIdsMap.keys.iterator().next()
        val playerToChipsMapFirst = HashMap<UUID, Int>()
        playerToChipsMapFirst[player1Id] = 0
        playerToChipsMapFirst[player2Id] = 0
        playerToChipsMapFirst[player3Id] = 100
        game.attemptToStartNewHand(tableId, playerToChipsMapFirst)
        val playerToChipsMapSecond = HashMap<UUID, Int>()
        playerToChipsMapSecond[player1Id] = 0
        playerToChipsMapSecond[player2Id] = 0
        playerToChipsMapSecond[player3Id] = 100
        assertThrows(FlexPokerException::class.java) { game.attemptToStartNewHand(tableId, playerToChipsMapSecond) }
    }

}