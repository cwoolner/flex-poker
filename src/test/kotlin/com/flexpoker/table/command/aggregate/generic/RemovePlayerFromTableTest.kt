package com.flexpoker.table.command.aggregate.generic

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils.createBasicTable
import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils.createBasicTableAndStartHand
import com.flexpoker.table.command.events.PlayerRemovedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.UUID

class RemovePlayerFromTableTest {

    @Test
    fun testRemovePlayerSuccess() {
        val existingPlayer = UUID.randomUUID()
        val table = createBasicTable(UUID.randomUUID(), UUID.randomUUID(), existingPlayer)
        table.removePlayer(existingPlayer)
        assertEquals(PlayerRemovedEvent::class.java, table.fetchNewEvents()[table.fetchNewEvents().size - 1].javaClass)
    }

    @Test
    fun testRemovingNonExistingPlayer() {
        val table = createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        assertThrows(FlexPokerException::class.java) { table.removePlayer(UUID.randomUUID()) }
    }

    @Test
    fun testRemovingDuringAHand() {
        val existingPlayer = UUID.randomUUID()
        val table = createBasicTableAndStartHand(UUID.randomUUID(), UUID.randomUUID(), existingPlayer)
        assertThrows(FlexPokerException::class.java) { table.removePlayer(existingPlayer) }
    }

}