package com.flexpoker.game.command.aggregate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.Test;

import com.flexpoker.game.command.commands.CreateGameCommand;
import com.flexpoker.game.command.events.GameCreatedEvent;

public class CreateNewGameTest {

    @Test
    public void testCreateNewGameSuccess() {
        CreateGameCommand createGameCommand = new CreateGameCommand("test", 2,
                2, UUID.randomUUID(), 10);
        Game game = new DefaultGameFactory().createNew(createGameCommand);

        assertEquals(1, game.fetchAppliedEvents().size());
        assertEquals(1, game.fetchNewEvents().size());
        assertEquals(GameCreatedEvent.class,
                game.fetchNewEvents().get(0).getClass());

        GameCreatedEvent gameCreatedEvent = (GameCreatedEvent) game
                .fetchNewEvents().get(0);
        assertNotNull(gameCreatedEvent.getAggregateId());
        assertEquals(1, gameCreatedEvent.getVersion());
    }

}
