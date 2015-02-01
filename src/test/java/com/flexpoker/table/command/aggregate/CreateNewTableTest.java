package com.flexpoker.table.command.aggregate;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.table.command.framework.TableEvent;
import com.flexpoker.table.command.framework.TableEventType;
import com.flexpoker.test.util.CommonAssertions;

public class CreateNewTableTest {

    @Test
    public void testCreateNewTestSuccess() {
        UUID tableId = UUID.randomUUID();
        Set<UUID> playerIds = new HashSet<>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        Table table = new DefaultTableFactory().createNew(tableId, UUID.randomUUID(), 6);
        table.createNewTable(playerIds);

        List<TableEvent> newEvents = table.fetchNewEvents();

        assertEquals(1, newEvents.size());
        assertEquals(TableEventType.TableCreated, newEvents.get(0).getType());

        CommonAssertions.verifyEventIdsAndVersionNumbers(tableId, newEvents);
    }

    @Test(expected = FlexPokerException.class)
    public void testCantCallCreateNewTableTwice() {
        UUID tableId = UUID.randomUUID();
        Set<UUID> playerIds = new HashSet<>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        Table table = new DefaultTableFactory().createNew(tableId, UUID.randomUUID(), 6);
        table.createNewTable(playerIds);
        table.createNewTable(playerIds);
    }

    @Test(expected = FlexPokerException.class)
    public void testCantCreateATableGreaterThanMaxSize() {
        UUID tableId = UUID.randomUUID();
        Set<UUID> playerIds = new HashSet<>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        Table table = new DefaultTableFactory().createNew(tableId, UUID.randomUUID(), 2);
        table.createNewTable(playerIds);
    }

    @Test(expected = FlexPokerException.class)
    public void testCantCreateATableWithOnlyOnePlayer() {
        UUID tableId = UUID.randomUUID();
        Set<UUID> playerIds = new HashSet<>();
        playerIds.add(UUID.randomUUID());

        Table table = new DefaultTableFactory().createNew(tableId, UUID.randomUUID(), 2);
        table.createNewTable(playerIds);
    }

}
