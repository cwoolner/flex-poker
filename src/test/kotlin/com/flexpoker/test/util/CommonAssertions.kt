package com.flexpoker.test.util

import com.flexpoker.framework.event.Event
import com.flexpoker.table.command.aggregate.TableState
import com.flexpoker.table.command.events.TableEvent
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

    fun verifyAppliedAndNewEventsForAggregate(state: TableState, events: List<Event>, vararg eventClasses: Class<out Event>) {
        verifyEventIds(state.aggregateId, events)
        verifyNumberOfEventsAndEntireOrderByType(events, *eventClasses)
    }

    fun verifyNewEvents(aggregateId: UUID, events: List<TableEvent>, vararg eventClasses: Class<out Event>) {
        verifyEventIds(aggregateId, events)
        verifyNumberOfEventsAndEntireOrderByType(events, *eventClasses)
    }

}