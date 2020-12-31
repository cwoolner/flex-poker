package com.flexpoker.table.command.aggregate.generic

import com.flexpoker.table.command.aggregate.applyEvents
import com.flexpoker.table.command.aggregate.eventproducers.removePlayer
import com.flexpoker.table.command.aggregate.testhelpers.createBasicTable
import com.flexpoker.table.command.aggregate.testhelpers.createBasicTableAndStartHand
import com.flexpoker.table.command.events.PlayerRemovedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.UUID

class RemovePlayerFromTableTest {

    @Test
    fun testRemovePlayerSuccess() {
        val existingPlayer = UUID.randomUUID()
        val initState = createBasicTable(UUID.randomUUID(), UUID.randomUUID(), existingPlayer)
        val events = removePlayer(initState, existingPlayer)
        assertEquals(PlayerRemovedEvent::class.java, events[0].javaClass)
    }

    @Test
    fun testRemovingNonExistingPlayer() {
        val initState = createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        assertThrows(IllegalArgumentException::class.java) { removePlayer(initState, UUID.randomUUID()) }
    }

    @Test
    fun testRemovingDuringAHand() {
        val existingPlayer = UUID.randomUUID()
        val events = createBasicTableAndStartHand(UUID.randomUUID(), UUID.randomUUID(), existingPlayer)
        val initState = applyEvents(events)
        assertThrows(IllegalArgumentException::class.java) { removePlayer(initState, existingPlayer) }
    }

}