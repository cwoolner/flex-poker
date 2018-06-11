package com.flexpoker.table.command.aggregate.generic;

import static com.flexpoker.test.util.CommonAssertions.verifyAppliedAndNewEventsForAggregate;

import java.util.HashSet;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.table.command.aggregate.DefaultTableFactory;
import com.flexpoker.table.command.commands.CreateTableCommand;
import com.flexpoker.table.command.events.TableCreatedEvent;

public class CreateNewTableTest {

    @Test
    public void testCreateNewTestSuccess() {
        var tableId = UUID.randomUUID();
        var playerIds = new HashSet<UUID>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        var command = new CreateTableCommand(tableId, UUID.randomUUID(), playerIds, 6);
        var table = new DefaultTableFactory().createNew(command);

        verifyAppliedAndNewEventsForAggregate(table, TableCreatedEvent.class);
    }

    @Test(expected = FlexPokerException.class)
    public void testCantCreateATableGreaterThanMaxSize() {
        var tableId = UUID.randomUUID();
        var playerIds = new HashSet<UUID>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        var command = new CreateTableCommand(tableId, UUID.randomUUID(), playerIds, 2);
        new DefaultTableFactory().createNew(command);
    }

    @Test(expected = FlexPokerException.class)
    public void testCantCreateATableWithOnlyOnePlayer() {
        var tableId = UUID.randomUUID();
        var playerIds = new HashSet<UUID>();
        playerIds.add(UUID.randomUUID());

        var command = new CreateTableCommand(tableId, UUID.randomUUID(), playerIds, 2);
        new DefaultTableFactory().createNew(command);
    }

}
