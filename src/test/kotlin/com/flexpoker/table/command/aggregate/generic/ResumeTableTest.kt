package com.flexpoker.table.command.aggregate.generic

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils.createBasicTable
import com.flexpoker.table.command.events.TableResumedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.UUID

class ResumeTableTest {

    @Test
    fun testResumeSuccess() {
        val table = createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        table.pause()
        table.resume()
        assertEquals(TableResumedEvent::class.java, table.fetchNewEvents()[table.fetchNewEvents().size - 1].javaClass)
    }

    @Test
    fun testResumeOnActiveTable() {
        val table = createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        assertThrows(FlexPokerException::class.java) { table.resume() }
    }

    @Test
    fun testResumeTwice() {
        val table = createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        table.pause()
        table.resume()
        assertThrows(FlexPokerException::class.java) { table.resume() }
    }

}