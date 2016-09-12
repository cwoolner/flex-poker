package com.flexpoker.table.command.aggregate.generic;

import static com.flexpoker.test.util.CommonAssertions.verifyAppliedAndNewEventsForAggregate;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.table.command.aggregate.DefaultTableFactory;
import com.flexpoker.table.command.aggregate.Table;
import com.flexpoker.table.command.commands.CreateTableCommand;
import com.flexpoker.table.command.events.TableCreatedEvent;

public class CreateNewTableTest {

    @Test
    public void testCreateNewTestSuccess() {
        UUID tableId = UUID.randomUUID();
        Set<UUID> playerIds = new HashSet<>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        CreateTableCommand command = new CreateTableCommand(tableId, UUID.randomUUID(), playerIds, 6);
        Table table = new DefaultTableFactory().createNew(command);

        verifyAppliedAndNewEventsForAggregate(table, TableCreatedEvent.class);
    }

    @Test(expected = FlexPokerException.class)
    public void testCantCreateATableGreaterThanMaxSize() {
        UUID tableId = UUID.randomUUID();
        Set<UUID> playerIds = new HashSet<>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        CreateTableCommand command = new CreateTableCommand(tableId, UUID.randomUUID(), playerIds, 2);
        new DefaultTableFactory().createNew(command);
    }

    @Test(expected = FlexPokerException.class)
    public void testCantCreateATableWithOnlyOnePlayer() {
        UUID tableId = UUID.randomUUID();
        Set<UUID> playerIds = new HashSet<>();
        playerIds.add(UUID.randomUUID());

        CreateTableCommand command = new CreateTableCommand(tableId, UUID.randomUUID(), playerIds, 2);
        new DefaultTableFactory().createNew(command);
    }

}
