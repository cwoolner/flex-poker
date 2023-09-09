package com.flexpoker.table.command.aggregate.generic

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.table.command.aggregate.TableState
import com.flexpoker.table.command.aggregate.eventproducers.pause
import com.flexpoker.table.command.aggregate.testhelpers.createBasicTable
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.table.command.events.TablePausedEvent
import com.flexpoker.test.util.EventProducerApplierBuilder
import com.flexpoker.test.util.UnitTestClass
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.UUID

@UnitTestClass
class PauseTableTest {

    @Test
    fun testPauseSuccess() {
        val initState = createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        val (_, events) = EventProducerApplierBuilder<TableState, TableEvent>()
            .initState(initState)
            .andRun { pause(it) }
            .run()
        assertEquals(TablePausedEvent::class.java, events.first().javaClass)
    }

    @Test
    fun testPauseTwice() {
        val initState = createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        EventProducerApplierBuilder<TableState, TableEvent>()
            .initState(initState)
            .andRun { pause(it) }
            .andRunThrows(FlexPokerException::class.java) { pause(it) }
            .run()
    }

}