package com.flexpoker.table.command.factory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.table.command.aggregate.DefaultTableFactory;
import com.flexpoker.table.command.commands.CreateTableCommand;
import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class DefaultTableFactoryTest {

    private DefaultTableFactory sut;

    @Before
    public void setup() {
        sut = new DefaultTableFactory();
    }

    @Test
    public void testCreateNew() {
        var playerIds = new HashSet<UUID>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        var command = new CreateTableCommand(UUID.randomUUID(), UUID.randomUUID(), playerIds, 6);
        var table = sut.createNew(command);
        assertNotNull(table);
        assertFalse(table.fetchAppliedEvents().isEmpty());
        assertFalse(table.fetchNewEvents().isEmpty());
    }

    @Test(expected = FlexPokerException.class)
    public void testPlayerListToLongFails() {
        var playerIds = new HashSet<UUID>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        var command = new CreateTableCommand(UUID.randomUUID(), UUID.randomUUID(), playerIds, 2);
        sut.createNew(command);
    }

    @Test
    public void testPlayerListExactSucceeds() {
        var playerIds = new HashSet<UUID>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        var command = new CreateTableCommand(UUID.randomUUID(), UUID.randomUUID(), playerIds, 2);
        var table = sut.createNew(command);
        assertNotNull(table);
        assertFalse(table.fetchAppliedEvents().isEmpty());
        assertFalse(table.fetchNewEvents().isEmpty());
    }

    @Test
    public void testCreateFrom() {
        var events = new ArrayList<TableEvent>();
        events.add(new TableCreatedEvent(null, UUID.randomUUID(), 6, new HashMap<>(), 1500));
        var table = sut.createFrom(events);
        assertNotNull(table);
        assertFalse(table.fetchAppliedEvents().isEmpty());
        assertTrue(table.fetchNewEvents().isEmpty());
    }

}
