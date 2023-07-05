package com.flexpoker.game.command.aggregate

import com.flexpoker.game.command.aggregate.eventproducers.joinGame
import com.flexpoker.game.command.events.GameCreatedEvent
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent
import com.flexpoker.test.util.EventProducerApplierBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.UUID

class TableAssignmentTest {

    @Test
    fun testAssignmentIsRandom() {
        var player1AlwaysWithPlayer2 = true
        var player1SometimesWithPlayer2 = false
        for (i in 0..999) {
            val createdEvent = GameCreatedEvent(UUID.randomUUID(), "test", 4,
                2, UUID.randomUUID(), 10, 20)

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

            val (_, events) = EventProducerApplierBuilder<GameState, GameEvent>()
                .initState(createdEvent)
                .andRun { joinGame(it, player1Id) }
                .andRun { joinGame(it, player2Id) }
                .andRun { joinGame(it, player3Id) }
                .andRun { joinGame(it, player4Id) }
                .run()

            val (_, tableIdToPlayerIdsMap) = events[5] as GameTablesCreatedAndPlayersAssociatedEvent
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
        val (state, allEvents) = createGameAndJoinAllPlayers(2, 2)
        verifyTableDistribution(state, allEvents, 2)
    }

    @Test
    fun testFourFitsAllInOneTable() {
        val (state, allEvents) = createGameAndJoinAllPlayers(4, 9)
        verifyTableDistribution(state, allEvents, 4)
    }

    @Test
    fun testFourFitsAllInASmallerTable() {
        val (state, allEvents) = createGameAndJoinAllPlayers(4, 6)
        verifyTableDistribution(state, allEvents, 4)
    }

    @Test
    fun testFourFitsPerfectlyInOneTable() {
        val (state, allEvents) = createGameAndJoinAllPlayers(4, 4)
        verifyTableDistribution(state, allEvents, 4)
    }

    @Test
    fun testFourFitsEvenlyInTwoTables() {
        val (state, allEvents) = createGameAndJoinAllPlayers(4, 3)
        verifyTableDistribution(state, allEvents, 2, 2)
    }

    @Test
    fun testTwentyFitsUnevenlyOverThreeTables() {
        val (state, allEvents) = createGameAndJoinAllPlayers(20, 9)
        verifyTableDistribution(state, allEvents, 6, 7, 7)
    }

    @Test
    fun testTwentyFitsFourEvenTablesNoneFull() {
        val (state, allEvents) = createGameAndJoinAllPlayers(20, 6)
        verifyTableDistribution(state, allEvents, 5, 5, 5, 5)
    }

    @Test
    fun testTwentyFitsFiveEvenTablesAllFull() {
        val (state, allEvents) = createGameAndJoinAllPlayers(20, 4)
        verifyTableDistribution(state, allEvents, 4, 4, 4, 4, 4)
    }

    @Test
    fun testTwentyFitsSevenTablesUnevenly() {
        val (state, allEvents) = createGameAndJoinAllPlayers(20, 3)
        verifyTableDistribution(state, allEvents, 2, 3, 3, 3, 3, 3, 3)
    }

    @Test
    fun testTwentyFitsTenTablesEvenly() {
        val (state, allEvents) = createGameAndJoinAllPlayers(20, 2)
        verifyTableDistribution(state, allEvents, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2)
    }

    @Test
    fun testTwoFitsOneTable1() {
        val (state, allEvents) = createGameAndJoinAllPlayers(2, 9)
        verifyTableDistribution(state, allEvents, 2)
    }

    @Test
    fun testTwoFitsOneTable2() {
        val (state, allEvents) = createGameAndJoinAllPlayers(2, 6)
        verifyTableDistribution(state, allEvents, 2)
    }

    @Test
    fun testTwoFitsOneTable3() {
        val (state, allEvents) = createGameAndJoinAllPlayers(2, 4)
        verifyTableDistribution(state, allEvents, 2)
    }

    @Test
    fun testTwoFitsOneTable4() {
        val (state, allEvents) = createGameAndJoinAllPlayers(2, 3)
        verifyTableDistribution(state, allEvents, 2)
    }

    @Test
    fun testTwoFitsOneTable5() {
        val (state, allEvents) = createGameAndJoinAllPlayers(2, 2)
        verifyTableDistribution(state, allEvents, 2)
    }

    private fun createGameAndJoinAllPlayers(numberOfPlayers: Int, numberOfPlayersPerTable: Int
    ): Pair<GameState, List<GameEvent>> {
        val createdEvent = GameCreatedEvent(UUID.randomUUID(), "test", numberOfPlayers,
            numberOfPlayersPerTable, UUID.randomUUID(), 10, 20)
        val builder = EventProducerApplierBuilder<GameState, GameEvent>().initState(createdEvent)
        repeat(numberOfPlayers) { builder.andRun { joinGame(it, UUID.randomUUID()) } }
        val (state, newEvents) = builder.run()
        return Pair(state, listOf(createdEvent) + newEvents)
    }

    private fun verifyTableDistribution(state: GameState, allEvents: List<GameEvent>, vararg expectedPlayersPerTable: Int) {
        val expectedNumberOfTables = expectedPlayersPerTable.size
        val totalNumberOfEvents = allEvents.size

        // this event will change locations depending on the number of players,
        // but it should be the 2nd to last every time
        val (_, tableIdToPlayerIdsMap) = allEvents[totalNumberOfEvents - 2] as GameTablesCreatedAndPlayersAssociatedEvent
        val playerSizes = tableIdToPlayerIdsMap.values.map { it.size }.sorted()
        assertEquals(expectedNumberOfTables, tableIdToPlayerIdsMap.size)
        assertEquals(playerSizes, expectedPlayersPerTable.toList())
    }

}