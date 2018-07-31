package com.flexpoker.table.command.aggregate.generic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils;
import com.flexpoker.table.command.events.PlayerAddedEvent;

public class AddPlayerToTableTest {

    @Test
    void testAddPlayerSuccess() {
        var table = TableTestUtils.createBasicTable(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        table.addPlayer(UUID.randomUUID(), 100);
        assertEquals(PlayerAddedEvent.class, table.fetchNewEvents().get(table.fetchNewEvents().size() - 1).getClass());
    }

    @Test
    void testAddingExistingPlayer() {
        var existingPlayer = UUID.randomUUID();
        var table = TableTestUtils.createBasicTable(UUID.randomUUID(), UUID.randomUUID(), existingPlayer);
        assertThrows(FlexPokerException.class, () -> table.addPlayer(existingPlayer, 100));
    }

}
