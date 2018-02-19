package com.flexpoker.game.command.factory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.flexpoker.game.command.aggregate.DefaultGameFactory;
import com.flexpoker.game.command.aggregate.Game;
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
        CreateGameCommand createGameCommand = new CreateGameCommand("test", 5,
                5, UUID.randomUUID(), 10, 20);
        Game game = sut.createNew(createGameCommand);
        assertNotNull(game);
        assertFalse(game.fetchAppliedEvents().isEmpty());
        assertFalse(game.fetchNewEvents().isEmpty());
    }

    @Test
    public void testCreateFrom() {
        List<GameEvent> events = new ArrayList<>();
        events.add(new GameCreatedEvent(null, 1, null, 0, 0, null, 10, 20));
        Game game = sut.createFrom(events);
        assertNotNull(game);
        assertFalse(game.fetchAppliedEvents().isEmpty());
        assertTrue(game.fetchNewEvents().isEmpty());
    }

}
