package com.flexpoker.game.command.aggregate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.game.command.commands.CreateGameCommand;
import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent;
import com.flexpoker.game.command.events.GameStartedEvent;
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent;
import com.flexpoker.game.command.factory.GameFactory;
import com.flexpoker.game.command.framework.GameEvent;
import com.flexpoker.game.command.framework.GameEventType;

public class GameTest {

    private final GameFactory gameFactory = new DefaultGameFactory();

    @Test
    public void testCreateNewGameSuccess() {
        CreateGameCommand createGameCommand = new CreateGameCommand("test", 2, 2, UUID.randomUUID());
        Game game = gameFactory.createNew(createGameCommand);

        assertEquals(1, game.fetchAppliedEvents().size());
        assertEquals(1, game.fetchNewEvents().size());
        assertEquals(GameEventType.GameCreated, game.fetchNewEvents().get(0).getType());

        GameCreatedEvent gameCreatedEvent = (GameCreatedEvent) game.fetchNewEvents().get(
                0);
        assertNotNull(gameCreatedEvent.getAggregateId());
        assertEquals(1, gameCreatedEvent.getVersion());
    }

    @Test
    public void testJoinGameSuccessFirstPlayerJoins() {
        List<GameEvent> events = new ArrayList<>();
        events.add(new GameCreatedEvent(UUID.randomUUID(), 1, "test", 2, 2, UUID
                .randomUUID()));

        Game game = gameFactory.createFrom(events);
        game.joinGame(UUID.randomUUID());

        assertEquals(2, game.fetchAppliedEvents().size());
        assertEquals(1, game.fetchNewEvents().size());
        assertEquals(2, game.fetchNewEvents().get(0).getVersion());
    }

    @Test
    public void testJoinGameSuccessGameStarting() {
        List<GameEvent> events = new ArrayList<>();
        events.add(new GameCreatedEvent(UUID.randomUUID(), 1, "test", 2, 2, UUID
                .randomUUID()));

        Game game = gameFactory.createFrom(events);
        game.joinGame(UUID.randomUUID());
        game.joinGame(UUID.randomUUID());

        assertEquals(6, game.fetchAppliedEvents().size());
        assertEquals(5, game.fetchNewEvents().size());
        assertEquals(2, game.fetchNewEvents().get(0).getVersion());
        assertEquals(3, game.fetchNewEvents().get(1).getVersion());
        assertEquals(4, game.fetchNewEvents().get(2).getVersion());
        assertEquals(5, game.fetchNewEvents().get(3).getVersion());
        assertEquals(6, game.fetchNewEvents().get(4).getVersion());
        assertEquals(GameMovedToStartingStageEvent.class, game.fetchNewEvents().get(2)
                .getClass());
        assertEquals(GameTablesCreatedAndPlayersAssociatedEvent.class, game
                .fetchNewEvents().get(3).getClass());
        assertEquals(GameStartedEvent.class, game.fetchNewEvents().get(4).getClass());
    }

    @Test(expected = FlexPokerException.class)
    public void testJoinGameAttemptToJoinTwice() {
        List<GameEvent> events = new ArrayList<>();
        events.add(new GameCreatedEvent(UUID.randomUUID(), 1, "test", 2, 2, UUID
                .randomUUID()));

        UUID playerId = UUID.randomUUID();

        Game game = gameFactory.createFrom(events);
        game.joinGame(playerId);
        game.joinGame(playerId);
    }

    @Test(expected = FlexPokerException.class)
    public void testJoinGameAttemptToJoinMoreThanMax() {
        List<GameEvent> events = new ArrayList<>();
        events.add(new GameCreatedEvent(UUID.randomUUID(), 1, "test", 2, 2, UUID
                .randomUUID()));

        Game game = gameFactory.createFrom(events);
        game.joinGame(UUID.randomUUID());
        game.joinGame(UUID.randomUUID());
        game.joinGame(UUID.randomUUID());
    }

}
