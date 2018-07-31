package com.flexpoker.table.command.aggregate.generic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.table.command.aggregate.Table;
import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils;
import com.flexpoker.table.command.events.TablePausedEvent;

public class PauseTableTest {

    @Test
    void testPauseSuccess() {
        var table = TableTestUtils.createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        table.pause();
        assertEquals(TablePausedEvent.class, table.fetchNewEvents().get(table.fetchNewEvents().size() - 1).getClass());
    }

    @Test
    void testPauseTwice() {
        Table table = TableTestUtils.createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        table.pause();
        assertThrows(FlexPokerException.class, () -> table.pause());
    }

}
