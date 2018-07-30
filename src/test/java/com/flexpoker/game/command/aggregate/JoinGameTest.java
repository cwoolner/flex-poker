package com.flexpoker.game.command.aggregate;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent;
import com.flexpoker.game.command.events.GameStartedEvent;
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class JoinGameTest {

    @Test
    public void testJoinGameSuccessFirstPlayerJoins() {
        var events = new ArrayList<GameEvent>();
        events.add(new GameCreatedEvent(UUID.randomUUID(), "test", 2, 2, UUID.randomUUID(), 10, 20));

        var game = new DefaultGameFactory().createFrom(events);
        game.joinGame(UUID.randomUUID());

        assertEquals(2, game.fetchAppliedEvents().size());
        assertEquals(1, game.fetchNewEvents().size());
        assertEquals(2, game.fetchNewEvents().get(0).getVersion());
    }

    @Test
    public void testJoinGameSuccessGameStarting() {
        var events = new ArrayList<GameEvent>();
        events.add(new GameCreatedEvent(UUID.randomUUID(), "test", 2, 2, UUID.randomUUID(), 10, 20));

        var game = new DefaultGameFactory().createFrom(events);
        game.joinGame(UUID.randomUUID());
        game.joinGame(UUID.randomUUID());

        assertEquals(6, game.fetchAppliedEvents().size());
        assertEquals(5, game.fetchNewEvents().size());
        assertEquals(2, game.fetchNewEvents().get(0).getVersion());
        assertEquals(3, game.fetchNewEvents().get(1).getVersion());
        assertEquals(4, game.fetchNewEvents().get(2).getVersion());
        assertEquals(5, game.fetchNewEvents().get(3).getVersion());
        assertEquals(6, game.fetchNewEvents().get(4).getVersion());
        assertEquals(GameMovedToStartingStageEvent.class, game.fetchNewEvents().get(2).getClass());
        assertEquals(GameTablesCreatedAndPlayersAssociatedEvent.class, game.fetchNewEvents().get(3).getClass());
        assertEquals(GameStartedEvent.class, game.fetchNewEvents().get(4).getClass());
    }

    @Test(expected = FlexPokerException.class)
    public void testJoinGameAttemptToJoinTwice() {
        var events = new ArrayList<GameEvent>();
        events.add(new GameCreatedEvent(UUID.randomUUID(), "test", 2, 2, UUID.randomUUID(), 10, 20));

        var playerId = UUID.randomUUID();

        var game = new DefaultGameFactory().createFrom(events);
        game.joinGame(playerId);
        game.joinGame(playerId);
    }

    @Test(expected = FlexPokerException.class)
    public void testJoinGameAttemptToJoinMoreThanMax() {
        var events = new ArrayList<GameEvent>();
        events.add(new GameCreatedEvent(UUID.randomUUID(), "test", 2, 2, UUID.randomUUID(), 10, 20));

        var game = new DefaultGameFactory().createFrom(events);
        game.joinGame(UUID.randomUUID());
        game.joinGame(UUID.randomUUID());
        game.joinGame(UUID.randomUUID());
    }

}
