package com.flexpoker.table.command.aggregate.generic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils;
import com.flexpoker.table.command.events.TableResumedEvent;

public class ResumeTableTest {

    @Test
    void testResumeSuccess() {
        var table = TableTestUtils.createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        table.pause();
        table.resume();
        assertEquals(TableResumedEvent.class, table.fetchNewEvents().get(table.fetchNewEvents().size() - 1).getClass());
    }

    @Test
    void testResumeOnActiveTable() {
        var table = TableTestUtils.createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        assertThrows(FlexPokerException.class, () -> table.resume());
    }

    @Test
    void testResumeTwice() {
        var table = TableTestUtils.createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        table.pause();
        table.resume();
        assertThrows(FlexPokerException.class, () -> table.resume());
    }

}
