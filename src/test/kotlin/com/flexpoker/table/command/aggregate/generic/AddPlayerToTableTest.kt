package com.flexpoker.table.command.aggregate.generic

import com.flexpoker.table.command.aggregate.applyEvents
import com.flexpoker.table.command.aggregate.eventproducers.addPlayer
import com.flexpoker.table.command.aggregate.eventproducers.createTable
import com.flexpoker.table.command.aggregate.testhelpers.TestRandomNumberGenerator
import com.flexpoker.table.command.aggregate.testhelpers.createBasicTable
import com.flexpoker.table.command.events.PlayerAddedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.UUID

class AddPlayerToTableTest {

    @Test
    fun testAddPlayerSuccess() {
        val tableId = UUID.randomUUID()
        val gameId = UUID.randomUUID()
        val player1Id = UUID.randomUUID()
        val player2Id = UUID.randomUUID()
        val initState = applyEvents(createTable(tableId, gameId, 6, setOf(UUID.randomUUID(),
            UUID.randomUUID()), TestRandomNumberGenerator(4)))

        val newEvents1 = addPlayer(initState, player1Id, 100, TestRandomNumberGenerator(2))
        val updatedState = applyEvents(initState, *newEvents1.toTypedArray())
        val player1AddedEvent = newEvents1[0] as PlayerAddedEvent
        assertEquals(1, newEvents1.size)
        assertEquals(PlayerAddedEvent(tableId, gameId, player1Id, 100, 2), player1AddedEvent)
        assertEquals(6, updatedState.seatMap.size)
        assertEquals(3, updatedState.seatMap.values.filterNotNull().size)

        val newEvents2 = addPlayer(updatedState, player2Id, 150, TestRandomNumberGenerator(0))
        val updatedState2 = applyEvents(updatedState, *newEvents2.toTypedArray())
        val player2AddedEvent = newEvents2[0] as PlayerAddedEvent
        assertEquals(1, newEvents2.size)
        assertEquals(PlayerAddedEvent(tableId, gameId, player2Id, 150, 0), player2AddedEvent)
        assertEquals(6, updatedState2.seatMap.size)
        assertEquals(4, updatedState2.seatMap.values.filterNotNull().size)
    }

    @Test
    fun testAddingExistingPlayer() {
        val existingPlayer = UUID.randomUUID()
        val initState = createBasicTable(UUID.randomUUID(), UUID.randomUUID(), existingPlayer)
        assertThrows(IllegalArgumentException::class.java) {
            addPlayer(initState, existingPlayer, 100, TestRandomNumberGenerator(0))
        }
    }

}