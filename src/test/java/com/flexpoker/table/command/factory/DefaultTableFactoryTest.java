package com.flexpoker.table.command.factory;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.table.command.aggregate.DefaultTableFactory;
import com.flexpoker.table.command.commands.CreateTableCommand;
import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.table.command.framework.TableEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pcollections.HashTreePMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultTableFactoryTest {

    private DefaultTableFactory sut;

    @BeforeEach
    void setup() {
        sut = new DefaultTableFactory();
    }

    @Test
    void testCreateNew() {
        var playerIds = new HashSet<UUID>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        var command = new CreateTableCommand(UUID.randomUUID(), UUID.randomUUID(), playerIds, 6);
        var table = sut.createNew(command);
        assertNotNull(table);
        assertFalse(table.fetchAppliedEvents().isEmpty());
        assertFalse(table.fetchNewEvents().isEmpty());
    }

    @Test
    void testPlayerListToLongFails() {
        var playerIds = new HashSet<UUID>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        var command = new CreateTableCommand(UUID.randomUUID(), UUID.randomUUID(), playerIds, 2);
        assertThrows(FlexPokerException.class, () -> sut.createNew(command));
    }

    @Test
    void testPlayerListExactSucceeds() {
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
    void testCreateFrom() {
        var events = new ArrayList<TableEvent>();
        events.add(new TableCreatedEvent(UUID.randomUUID(), UUID.randomUUID(), 6, HashTreePMap.empty(), 1500));
        var table = sut.createFrom(events);
        assertNotNull(table);
        assertFalse(table.fetchAppliedEvents().isEmpty());
        assertTrue(table.fetchNewEvents().isEmpty());
    }

}
