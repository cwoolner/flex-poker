package com.flexpoker.test.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.UUID;

import com.flexpoker.framework.event.Event;
import com.flexpoker.table.command.aggregate.Table;

public class CommonAssertions {

    public static void verifyEventIds(UUID tableId, List<? extends Event> events) {
        var version = 0;
        for (Event event : events) {
            assertEquals(tableId, event.getAggregateId());
        }
    }

    public static void verifyNumberOfEventsAndEntireOrderByType(
            List<? extends Event> events, Class<? extends Event>... eventClasses) {
        assertEquals(eventClasses.length, events.size());
        assertArrayEquals(eventClasses, events.stream().map(x -> x.getClass()).toArray());
    }

    public static void verifyAppliedAndNewEventsForAggregate(Table table, Class<? extends Event>... eventClasses) {
        verifyEventIds(table.getAggregateId(), table.fetchAppliedEvents());
        verifyEventIds(table.getAggregateId(), table.fetchNewEvents());
        verifyNumberOfEventsAndEntireOrderByType(table.fetchAppliedEvents(), eventClasses);
        verifyNumberOfEventsAndEntireOrderByType(table.fetchNewEvents(), eventClasses);
    }

}
