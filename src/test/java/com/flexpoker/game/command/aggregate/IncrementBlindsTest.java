package com.flexpoker.game.command.aggregate;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        UUID gameId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableIdToPlayerIdsMap = new HashMap<>();
        Set<UUID> tableIds = tableIdToPlayerIdsMap.keySet();

        List<GameEvent> events = new ArrayList<>();
        events.add(new GameCreatedEvent(gameId, 1, "test", 2, 2, UUID.randomUUID(), 10, 20));
        events.add(new GameJoinedEvent(gameId, 2, UUID.randomUUID()));
        events.add(new GameJoinedEvent(gameId, 3, UUID.randomUUID()));
        events.add(new GameMovedToStartingStageEvent(gameId, 4));
        events.add(new GameTablesCreatedAndPlayersAssociatedEvent(gameId, 5, tableIdToPlayerIdsMap, 2));
        events.add(new GameStartedEvent(gameId, 6, tableIds, new BlindSchedule(10)));

        Game game = new DefaultGameFactory().createFrom(events);
        game.increaseBlinds();

        assertEquals(7, game.fetchAppliedEvents().size());
        assertEquals(1, game.fetchNewEvents().size());
        assertEquals(7, game.fetchNewEvents().get(0).getVersion());
    }

    @Test
    public void testIncrementBlindsAlreadyAtMaxLevel() {
        UUID gameId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableIdToPlayerIdsMap = new HashMap<>();
        Set<UUID> tableIds = tableIdToPlayerIdsMap.keySet();

        List<GameEvent> events = new ArrayList<>();
        events.add(new GameCreatedEvent(gameId, 1, "test", 2, 2, UUID.randomUUID(), 10, 20));
        events.add(new GameJoinedEvent(gameId, 2, UUID.randomUUID()));
        events.add(new GameJoinedEvent(gameId, 3, UUID.randomUUID()));
        events.add(new GameMovedToStartingStageEvent(gameId, 4));
        events.add(new GameTablesCreatedAndPlayersAssociatedEvent(gameId, 5, tableIdToPlayerIdsMap, 2));
        events.add(new GameStartedEvent(gameId, 6, tableIds, new BlindSchedule(10)));

        Game game = new DefaultGameFactory().createFrom(events);
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
        UUID gameId = UUID.randomUUID();
        Map<UUID, Set<UUID>> tableIdToPlayerIdsMap = new HashMap<>();

        List<GameEvent> events = new ArrayList<>();
        events.add(new GameCreatedEvent(gameId, 1, "test", 2, 2, UUID.randomUUID(), 10, 20));
        events.add(new GameJoinedEvent(gameId, 2, UUID.randomUUID()));
        events.add(new GameJoinedEvent(gameId, 3, UUID.randomUUID()));
        events.add(new GameMovedToStartingStageEvent(gameId, 4));
        events.add(new GameTablesCreatedAndPlayersAssociatedEvent(gameId, 5, tableIdToPlayerIdsMap, 2));

        Game game = new DefaultGameFactory().createFrom(events);
        game.increaseBlinds();
    }

}
