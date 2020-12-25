package com.flexpoker.test.util

import com.flexpoker.framework.event.Event
import com.flexpoker.table.command.aggregate.Table
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.UUID

object CommonAssertions {

    fun verifyEventIds(tableId: UUID, events: List<Event>) {
        for (event in events) {
            assertEquals(tableId, event.aggregateId)
        }
    }

    fun verifyNumberOfEventsAndEntireOrderByType(events: List<Event>, vararg eventClasses: Class<out Event>) {
        assertEquals(eventClasses.size, events.size)
        assertArrayEquals(eventClasses, events.map { it.javaClass }.toTypedArray())
    }

    fun verifyAppliedAndNewEventsForAggregate(table: Table, vararg eventClasses: Class<out Event>) {
        verifyEventIds(table.state.aggregateId, table.fetchAppliedEvents())
        verifyEventIds(table.state.aggregateId, table.fetchNewEvents())
        verifyNumberOfEventsAndEntireOrderByType(table.fetchAppliedEvents(), *eventClasses)
        verifyNumberOfEventsAndEntireOrderByType(table.fetchNewEvents(), *eventClasses)
    }

}