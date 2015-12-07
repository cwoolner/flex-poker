package com.flexpoker.table.command.factory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.flexpoker.table.command.aggregate.DefaultTableFactory;
import com.flexpoker.table.command.aggregate.Table;
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
        Set<UUID> playerIds = new HashSet<>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        CreateTableCommand command = new CreateTableCommand(UUID.randomUUID(),
                UUID.randomUUID(), playerIds, 6);
        Table table = sut.createNew(command);
        assertNotNull(table);
    }

    @Test
    public void testCreateFrom() {
        List<TableEvent> events = new ArrayList<>();
        events.add(new TableCreatedEvent(null, 1, UUID.randomUUID(), 6, new HashMap<>(),
                1500));
        Table table = sut.createFrom(events);
        assertNotNull(table);
        assertTrue(table.fetchNewEvents().isEmpty());
    }

}
