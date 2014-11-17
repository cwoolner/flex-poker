package com.flexpoker.table.command.aggregate;

import static junit.framework.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.table.command.factory.TableFactory;
import com.flexpoker.table.command.framework.TableEventType;

public class TableTest {

    private final TableFactory tableFactory = new DefaultTableFactory();

    @Test
    public void testCreateNewTestSuccess() {
        UUID tableId = UUID.randomUUID();
        Set<UUID> playerIds = new HashSet<>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        Table table = tableFactory.createNew(tableId, UUID.randomUUID(), 6);
        table.createNewTable(playerIds);

        assertEquals(1, table.fetchNewEvents().size());
        assertEquals(TableEventType.TableCreated, table.fetchNewEvents().get(0).getType());

        TableCreatedEvent tableCreatedEvent = (TableCreatedEvent) table.fetchNewEvents()
                .get(0);
        assertEquals(tableId, tableCreatedEvent.getAggregateId());
        assertEquals(1, tableCreatedEvent.getVersion());
    }

    @Test(expected = FlexPokerException.class)
    public void testCantCallCreateNewTableTwice() {
        UUID tableId = UUID.randomUUID();
        Set<UUID> playerIds = new HashSet<>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        Table table = tableFactory.createNew(tableId, UUID.randomUUID(), 6);
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

        Table table = tableFactory.createNew(tableId, UUID.randomUUID(), 2);
        table.createNewTable(playerIds);
    }

    @Test(expected = FlexPokerException.class)
    public void testCantCreateATableWithOnlyOnePlayer() {
        UUID tableId = UUID.randomUUID();
        Set<UUID> playerIds = new HashSet<>();
        playerIds.add(UUID.randomUUID());

        Table table = tableFactory.createNew(tableId, UUID.randomUUID(), 2);
        table.createNewTable(playerIds);
    }

}
