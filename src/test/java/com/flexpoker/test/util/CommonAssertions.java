package com.flexpoker.test.util;

import static junit.framework.Assert.assertEquals;

import java.util.List;
import java.util.UUID;

import com.flexpoker.framework.event.Event;
import com.flexpoker.framework.event.EventType;

public class CommonAssertions {

    public static void verifyEventIdsAndVersionNumbers(UUID tableId,
            List<? extends Event<? extends EventType>> newEvents) {
        int version = 0;
        for (Event<?> event : newEvents) {
            assertEquals(tableId, event.getAggregateId());
            assertEquals(++version, event.getVersion());
        }
    }

}
