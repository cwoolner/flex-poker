package com.flexpoker.game.command.factory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import com.flexpoker.game.command.aggregate.DefaultGameFactory;
import com.flexpoker.game.command.commands.CreateGameCommand;
import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class GameFactoryTest {

    private DefaultGameFactory sut;

    @Before
    public void setup() {
        sut = new DefaultGameFactory();
    }

    @Test
    public void testCreateNew() {
        var createGameCommand = new CreateGameCommand("test", 5, 5, UUID.randomUUID(), 10, 20);
        var game = sut.createNew(createGameCommand);
        assertNotNull(game);
        assertFalse(game.fetchAppliedEvents().isEmpty());
        assertFalse(game.fetchNewEvents().isEmpty());
    }

    @Test
    public void testCreateFrom() {
        var events = new ArrayList<GameEvent>();
        events.add(new GameCreatedEvent(null, null, 0, 0, null, 10, 20));
        var game = sut.createFrom(events);
        assertNotNull(game);
        assertFalse(game.fetchAppliedEvents().isEmpty());
        assertTrue(game.fetchNewEvents().isEmpty());
    }

}
