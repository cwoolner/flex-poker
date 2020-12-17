package com.flexpoker.game.command.aggregate

import com.flexpoker.game.command.events.GameCreatedEvent
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.ArrayList
import java.util.UUID

class TableAssignmentTest {

    @Test
    fun testAssignmentIsRandom() {
        var player1AlwaysWithPlayer2 = true
        var player1SometimesWithPlayer2 = false
        for (i in 0..999) {
            val events = ArrayList<GameEvent>()
            events.add(GameCreatedEvent(UUID.randomUUID(), "test", 4, 2, UUID.randomUUID(), 10, 20))
            val game = DefaultGameFactory().createFrom(events)

            // create a bunch of static UUIDs that will hash the same and will
            // thus be transformed into the same list before shuffling occurs
            // (and will fail the test without the shuffle().
            // this gets rid of the randomizing effect of using a HashSet with
            // random/on-the-fly UUIDs, which isn't good when remain attached to
            //  players over periods of time
            val player1Id = UUID.fromString("07755923-95b4-4ae7-9f45-b67a8e7929fe")
            val player2Id = UUID.fromString("07755923-95b4-4ae7-9f45-b67a8e7929ff")
            val player3Id = UUID.fromString("17755923-95b4-4ae7-9f45-b67a8e7929fe")
            val player4Id = UUID.fromString("17755923-95b4-4ae7-9f45-b67a8e7929ff")
            game.joinGame(player1Id)
            game.joinGame(player2Id)
            game.joinGame(player3Id)
            game.joinGame(player4Id)
            val (_, tableIdToPlayerIdsMap) = game.fetchAppliedEvents()[6] as GameTablesCreatedAndPlayersAssociatedEvent
            val player1sTable = tableIdToPlayerIdsMap.values.first { it.contains(player1Id) }
            if (player1sTable.contains(player2Id)) {
                player1SometimesWithPlayer2 = true
            } else {
                player1AlwaysWithPlayer2 = false
            }
        }
        assertFalse(player1AlwaysWithPlayer2)
        assertTrue(player1SometimesWithPlayer2)
    }

    @Test
    fun testTwoPlayersOneTable() {
        val game = createGameAndJoinAllPlayers(2, 2)
        verifyTableDistribution(game, 2)
    }

    @Test
    fun testFourFitsAllInOneTable() {
        val game = createGameAndJoinAllPlayers(4, 9)
        verifyTableDistribution(game, 4)
    }

    @Test
    fun testFourFitsAllInASmallerTable() {
        val game = createGameAndJoinAllPlayers(4, 6)
        verifyTableDistribution(game, 4)
    }

    @Test
    fun testFourFitsPerfectlyInOneTable() {
        val game = createGameAndJoinAllPlayers(4, 4)
        verifyTableDistribution(game, 4)
    }

    @Test
    fun testFourFitsEvenlyInTwoTables() {
        val game = createGameAndJoinAllPlayers(4, 3)
        verifyTableDistribution(game, 2, 2)
    }

    @Test
    fun testTwentyFitsUnevenlyOverThreeTables() {
        val game = createGameAndJoinAllPlayers(20, 9)
        verifyTableDistribution(game, 6, 7, 7)
    }

    @Test
    fun testTwentyFitsFourEvenTablesNoneFull() {
        val game = createGameAndJoinAllPlayers(20, 6)
        verifyTableDistribution(game, 5, 5, 5, 5)
    }

    @Test
    fun testTwentyFitsFiveEvenTablesAllFull() {
        val game = createGameAndJoinAllPlayers(20, 4)
        verifyTableDistribution(game, 4, 4, 4, 4, 4)
    }

    @Test
    fun testTwentyFitsSevenTablesUnevenly() {
        val game = createGameAndJoinAllPlayers(20, 3)
        verifyTableDistribution(game, 2, 3, 3, 3, 3, 3, 3)
    }

    @Test
    fun testTwentyFitsTenTablesEvenly() {
        val game = createGameAndJoinAllPlayers(20, 2)
        verifyTableDistribution(game, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2)
    }

    @Test
    fun testTwoFitsOneTable1() {
        val game = createGameAndJoinAllPlayers(2, 9)
        verifyTableDistribution(game, 2)
    }

    @Test
    fun testTwoFitsOneTable2() {
        val game = createGameAndJoinAllPlayers(2, 6)
        verifyTableDistribution(game, 2)
    }

    @Test
    fun testTwoFitsOneTable3() {
        val game = createGameAndJoinAllPlayers(2, 4)
        verifyTableDistribution(game, 2)
    }

    @Test
    fun testTwoFitsOneTable4() {
        val game = createGameAndJoinAllPlayers(2, 3)
        verifyTableDistribution(game, 2)
    }

    @Test
    fun testTwoFitsOneTable5() {
        val game = createGameAndJoinAllPlayers(2, 2)
        verifyTableDistribution(game, 2)
    }

    private fun createGameAndJoinAllPlayers(numberOfPlayers: Int, numberOfPlayersPerTable: Int): Game {
        val events = ArrayList<GameEvent>()
        events.add(GameCreatedEvent(UUID.randomUUID(), "test", numberOfPlayers, numberOfPlayersPerTable,
                UUID.randomUUID(), 10, 20))
        val game = DefaultGameFactory().createFrom(events)
        repeat(numberOfPlayers) { game.joinGame(UUID.randomUUID()) }
        return game
    }

    private fun verifyTableDistribution(game: Game, vararg expectedPlayersPerTable: Int) {
        val expectedNumberOfTables = expectedPlayersPerTable.size
        val totalNumberOfEvents = game.fetchNewEvents().size

        // this event will change locations depending on the number of players,
        // but it should be the 2nd to last every time
        val (_, tableIdToPlayerIdsMap) = game.fetchNewEvents()[totalNumberOfEvents - 2]
                as GameTablesCreatedAndPlayersAssociatedEvent
        val playerSizes = tableIdToPlayerIdsMap.values.map { it.size }.sorted()
        assertEquals(expectedNumberOfTables, tableIdToPlayerIdsMap.size)
        assertEquals(playerSizes, expectedPlayersPerTable.toList())
    }

}