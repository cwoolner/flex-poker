package com.flexpoker.table.command.aggregate.generic;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.table.command.aggregate.Table;
import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils;
import com.flexpoker.table.command.events.TableResumedEvent;

public class ResumeTableTest {

    @Test
    public void testResumeSuccess() {
        Table table = TableTestUtils.createBasicTable(UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID());
        table.pause();
        table.resume();
        assertEquals(TableResumedEvent.class,
                table.fetchNewEvents().get(table.fetchNewEvents().size() - 1).getClass());
    }

    @Test(expected = FlexPokerException.class)
    public void testResumeOnActiveTable() {
        Table table = TableTestUtils.createBasicTable(UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID());
        table.resume();
    }

    @Test(expected = FlexPokerException.class)
    public void testResumeTwice() {
        Table table = TableTestUtils.createBasicTable(UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID());
        table.pause();
        table.resume();
        table.resume();
    }

}
