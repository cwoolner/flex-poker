package com.flexpoker.table.command.factory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.flexpoker.table.command.aggregate.DefaultTableFactory;
import com.flexpoker.table.command.aggregate.Table;
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
        Table game = sut.createNew(UUID.randomUUID(), UUID.randomUUID(), 6);
        assertNotNull(game);
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
