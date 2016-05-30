package com.flexpoker.table.command.aggregate.generic;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.table.command.aggregate.Table;
import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils;
import com.flexpoker.table.command.events.PlayerRemovedEvent;

public class RemovePlayerFromTableTest {

    @Test
    public void testRemovePlayerSuccess() {
        UUID existingPlayer = UUID.randomUUID();
        Table table = TableTestUtils.createBasicTable(UUID.randomUUID(),
                UUID.randomUUID(), existingPlayer);
        table.removePlayer(existingPlayer);
        assertEquals(PlayerRemovedEvent.class, table.fetchNewEvents()
                .get(table.fetchNewEvents().size() - 1).getClass());
    }

    @Test(expected = FlexPokerException.class)
    public void testRemovingNonExistingPlayer() {
        Table table = TableTestUtils.createBasicTable(UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID());
        table.removePlayer(UUID.randomUUID());
    }

    @Test(expected = FlexPokerException.class)
    public void testRemovingDuringAHand() {
        UUID existingPlayer = UUID.randomUUID();
        Table table = TableTestUtils.createBasicTableAndStartHand(
                UUID.randomUUID(), UUID.randomUUID(), existingPlayer);
        table.removePlayer(existingPlayer);
    }

}
