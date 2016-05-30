package com.flexpoker.table.command.aggregate.generic;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.table.command.aggregate.Table;
import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils;
import com.flexpoker.table.command.events.PlayerAddedEvent;

public class AddPlayerToTableTest {

    @Test
    public void testAddPlayerSuccess() {
        Table table = TableTestUtils.createBasicTable(UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID());
        table.addPlayer(UUID.randomUUID(), 100);
        assertEquals(PlayerAddedEvent.class,
                table.fetchNewEvents().get(table.fetchNewEvents().size() - 1).getClass());
    }

    @Test(expected = FlexPokerException.class)
    public void testAddingExistingPlayer() {
        UUID existingPlayer = UUID.randomUUID();
        
        Table table = TableTestUtils.createBasicTable(UUID.randomUUID(),
                UUID.randomUUID(), existingPlayer);
        table.addPlayer(existingPlayer, 100);
    }

}
