package com.flexpoker.table.command.aggregate.generic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils;
import com.flexpoker.table.command.events.PlayerRemovedEvent;

public class RemovePlayerFromTableTest {

    @Test
    public void testRemovePlayerSuccess() {
        var existingPlayer = UUID.randomUUID();
        var table = TableTestUtils.createBasicTable(UUID.randomUUID(), UUID.randomUUID(), existingPlayer);
        table.removePlayer(existingPlayer);
        assertEquals(PlayerRemovedEvent.class, table.fetchNewEvents().get(table.fetchNewEvents().size() - 1).getClass());
    }

    @Test(expected = FlexPokerException.class)
    public void testRemovingNonExistingPlayer() {
        var table = TableTestUtils.createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        table.removePlayer(UUID.randomUUID());
    }

    @Test(expected = FlexPokerException.class)
    public void testRemovingDuringAHand() {
        var existingPlayer = UUID.randomUUID();
        var table = TableTestUtils.createBasicTableAndStartHand(UUID.randomUUID(), UUID.randomUUID(), existingPlayer);
        table.removePlayer(existingPlayer);
    }

}
