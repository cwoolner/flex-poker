package com.flexpoker.game.command.aggregate;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.events.GameJoinedEvent;
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent;
import com.flexpoker.game.command.events.GameStartedEvent;
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class IncrementBlindsTest {

    @Test
    public void testIncrementBlindsCreatesNewEvent() {
        var gameId = UUID.randomUUID();
        var tableIdToPlayerIdsMap = new HashMap<UUID, Set<UUID>>();
        var tableIds = tableIdToPlayerIdsMap.keySet();

        var events = new ArrayList<GameEvent>();
        events.add(new GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(), 10, 20));
        events.add(new GameJoinedEvent(gameId, UUID.randomUUID()));
        events.add(new GameJoinedEvent(gameId, UUID.randomUUID()));
        events.add(new GameMovedToStartingStageEvent(gameId));
        events.add(new GameTablesCreatedAndPlayersAssociatedEvent(gameId, tableIdToPlayerIdsMap, 2));
        events.add(new GameStartedEvent(gameId, tableIds, new BlindSchedule(10)));

        var game = new DefaultGameFactory().createFrom(events);
        game.increaseBlinds();

        assertEquals(7, game.fetchAppliedEvents().size());
        assertEquals(1, game.fetchNewEvents().size());
        assertEquals(7, game.fetchNewEvents().get(0).getVersion());
    }

    @Test
    public void testIncrementBlindsAlreadyAtMaxLevel() {
        var gameId = UUID.randomUUID();
        var tableIdToPlayerIdsMap = new HashMap<UUID, Set<UUID>>();
        var tableIds = tableIdToPlayerIdsMap.keySet();

        var events = new ArrayList<GameEvent>();
        events.add(new GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(), 10, 20));
        events.add(new GameJoinedEvent(gameId, UUID.randomUUID()));
        events.add(new GameJoinedEvent(gameId, UUID.randomUUID()));
        events.add(new GameMovedToStartingStageEvent(gameId));
        events.add(new GameTablesCreatedAndPlayersAssociatedEvent(gameId, tableIdToPlayerIdsMap, 2));
        events.add(new GameStartedEvent(gameId, tableIds, new BlindSchedule(10)));

        var game = new DefaultGameFactory().createFrom(events);
        game.increaseBlinds();
        game.increaseBlinds();
        game.increaseBlinds();
        game.increaseBlinds();
        game.increaseBlinds();

        assertEquals(10, game.fetchAppliedEvents().size());
        assertEquals(4, game.fetchNewEvents().size());
        assertEquals(10, game.fetchNewEvents().get(3).getVersion());
    }

    @Test(expected = FlexPokerException.class)
    public void testFailureWhenNotInTheCorrectStage() {
        var gameId = UUID.randomUUID();
        var tableIdToPlayerIdsMap = new HashMap<UUID, Set<UUID>>();

        var events = new ArrayList<GameEvent>();
        events.add(new GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(), 10, 20));
        events.add(new GameJoinedEvent(gameId, UUID.randomUUID()));
        events.add(new GameJoinedEvent(gameId, UUID.randomUUID()));
        events.add(new GameMovedToStartingStageEvent(gameId));
        events.add(new GameTablesCreatedAndPlayersAssociatedEvent(gameId, tableIdToPlayerIdsMap, 2));

        var game = new DefaultGameFactory().createFrom(events);
        game.increaseBlinds();
    }

}
