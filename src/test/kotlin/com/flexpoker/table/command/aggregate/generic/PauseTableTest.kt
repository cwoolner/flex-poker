package com.flexpoker.table.command.aggregate.generic

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils.createBasicTable
import com.flexpoker.table.command.events.TablePausedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.UUID

class PauseTableTest {

    @Test
    fun testPauseSuccess() {
        val table = createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        table.pause()
        assertEquals(TablePausedEvent::class.java, table.fetchNewEvents()[table.fetchNewEvents().size - 1].javaClass)
    }

    @Test
    fun testPauseTwice() {
        val table = createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        table.pause()
        assertThrows(FlexPokerException::class.java) { table.pause() }
    }

}