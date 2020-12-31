package com.flexpoker.table.command.aggregate.generic

import com.flexpoker.table.command.aggregate.eventproducers.addPlayer
import com.flexpoker.table.command.aggregate.testhelpers.createBasicTable
import com.flexpoker.table.command.events.PlayerAddedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.UUID

class AddPlayerToTableTest {

    @Test
    fun testAddPlayerSuccess() {
        val initState = createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        val events = addPlayer(initState, UUID.randomUUID(), 100)
        assertEquals(PlayerAddedEvent::class.java, events[0].javaClass)
    }

    @Test
    fun testAddingExistingPlayer() {
        val existingPlayer = UUID.randomUUID()
        val initState = createBasicTable(UUID.randomUUID(), UUID.randomUUID(), existingPlayer)
        assertThrows(IllegalArgumentException::class.java) { addPlayer(initState, existingPlayer, 100) }
    }

}