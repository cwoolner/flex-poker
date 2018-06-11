package com.flexpoker.game.command.aggregate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.flexpoker.game.command.commands.CreateGameCommand;
import com.flexpoker.game.command.events.GameCreatedEvent;

public class CreateNewGameTest {

    @Test
    public void testCreateNewGameSuccess() {
        var createGameCommand = new CreateGameCommand("test", 2, 2, UUID.randomUUID(), 10, 20);
        var game = new DefaultGameFactory().createNew(createGameCommand);

        assertEquals(1, game.fetchAppliedEvents().size());
        assertEquals(1, game.fetchNewEvents().size());
        assertEquals(GameCreatedEvent.class, game.fetchNewEvents().get(0).getClass());

        var gameCreatedEvent = (GameCreatedEvent) game.fetchNewEvents().get(0);
        assertNotNull(gameCreatedEvent.getAggregateId());
    }

}
