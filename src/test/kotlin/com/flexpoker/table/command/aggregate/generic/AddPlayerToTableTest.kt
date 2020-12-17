package com.flexpoker.table.command.aggregate.generic

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils.createBasicTable
import com.flexpoker.table.command.events.PlayerAddedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.UUID

class AddPlayerToTableTest {

    @Test
    fun testAddPlayerSuccess() {
        val table = createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        table.addPlayer(UUID.randomUUID(), 100)
        assertEquals(PlayerAddedEvent::class.java, table.fetchNewEvents()[table.fetchNewEvents().size - 1].javaClass)
    }

    @Test
    fun testAddingExistingPlayer() {
        val existingPlayer = UUID.randomUUID()
        val table = createBasicTable(UUID.randomUUID(), UUID.randomUUID(), existingPlayer)
        assertThrows(FlexPokerException::class.java) { table.addPlayer(existingPlayer, 100) }
    }

}