package com.flexpoker.game.command.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent;
import com.flexpoker.game.command.events.GameStartedEvent;
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class JoinGameTest {

    @Test
    void testJoinGameSuccessFirstPlayerJoins() {
        var events = new ArrayList<GameEvent>();
        events.add(new GameCreatedEvent(UUID.randomUUID(), "test", 2, 2, UUID.randomUUID(), 10, 20));

        var game = new DefaultGameFactory().createFrom(events);
        game.joinGame(UUID.randomUUID());

        assertEquals(2, game.fetchAppliedEvents().size());
        assertEquals(1, game.fetchNewEvents().size());
    }

    @Test
    void testJoinGameSuccessGameStarting() {
        var events = new ArrayList<GameEvent>();
        events.add(new GameCreatedEvent(UUID.randomUUID(), "test", 2, 2, UUID.randomUUID(), 10, 20));

        var game = new DefaultGameFactory().createFrom(events);
        game.joinGame(UUID.randomUUID());
        game.joinGame(UUID.randomUUID());

        assertEquals(6, game.fetchAppliedEvents().size());
        assertEquals(5, game.fetchNewEvents().size());
        assertEquals(GameMovedToStartingStageEvent.class, game.fetchNewEvents().get(2).getClass());
        assertEquals(GameTablesCreatedAndPlayersAssociatedEvent.class, game.fetchNewEvents().get(3).getClass());
        assertEquals(GameStartedEvent.class, game.fetchNewEvents().get(4).getClass());
    }

    @Test
    void testJoinGameAttemptToJoinTwice() {
        var events = new ArrayList<GameEvent>();
        events.add(new GameCreatedEvent(UUID.randomUUID(), "test", 2, 2, UUID.randomUUID(), 10, 20));

        var playerId = UUID.randomUUID();

        var game = new DefaultGameFactory().createFrom(events);
        game.joinGame(playerId);
        assertThrows(FlexPokerException.class, () -> game.joinGame(playerId));
    }

    @Test
    void testJoinGameAttemptToJoinMoreThanMax() {
        var events = new ArrayList<GameEvent>();
        events.add(new GameCreatedEvent(UUID.randomUUID(), "test", 2, 2, UUID.randomUUID(), 10, 20));

        var game = new DefaultGameFactory().createFrom(events);
        game.joinGame(UUID.randomUUID());
        game.joinGame(UUID.randomUUID());
        assertThrows(FlexPokerException.class, () -> game.joinGame(UUID.randomUUID()));
    }

}
