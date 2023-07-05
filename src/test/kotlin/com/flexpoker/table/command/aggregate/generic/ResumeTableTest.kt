package com.flexpoker.table.command.aggregate.generic

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.table.command.aggregate.TableState
import com.flexpoker.table.command.aggregate.eventproducers.pause
import com.flexpoker.table.command.aggregate.eventproducers.resume
import com.flexpoker.table.command.aggregate.testhelpers.createBasicTable
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.table.command.events.TableResumedEvent
import com.flexpoker.test.util.EventProducerApplierBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.UUID

class ResumeTableTest {

    @Test
    fun testResumeSuccess() {
        val initState = createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        val (_, events) = EventProducerApplierBuilder<TableState, TableEvent>()
            .initState(initState)
            .andRun { pause(it) }
            .andRun { resume(it) }
            .run()
        assertEquals(2, events.size)
        assertEquals(TableResumedEvent::class.java, events[1].javaClass)
    }

    @Test
    fun testResumeOnActiveTable() {
        val initState = createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        assertThrows(FlexPokerException::class.java) { resume(initState) }
    }

    @Test
    fun testResumeTwice() {
        val initState = createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        EventProducerApplierBuilder<TableState, TableEvent>()
            .initState(initState)
            .andRun { pause(it) }
            .andRun { resume(it) }
            .andRunThrows(FlexPokerException::class.java) { resume(it) }
            .run()
    }

}