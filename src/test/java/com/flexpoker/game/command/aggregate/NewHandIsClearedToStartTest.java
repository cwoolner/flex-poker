package com.flexpoker.game.command.aggregate;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.events.GameJoinedEvent;
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent;
import com.flexpoker.game.command.events.GameStartedEvent;
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class NewHandIsClearedToStartTest {

    @Test
    public void testJoinGameSuccessFirstPlayerJoins() {
        var gameId = UUID.randomUUID();
        var tableId = UUID.randomUUID();
        var player1Id = UUID.randomUUID();
        var player2Id = UUID.randomUUID();

        var tableToPlayersMap = new HashMap<UUID, Set<UUID>>();
        var playerIds = new HashSet<UUID>();
        playerIds.add(player1Id);
        playerIds.add(player2Id);
        tableToPlayersMap.put(tableId, playerIds);
        var tableIds = tableToPlayersMap.keySet();

        var events = new ArrayList<GameEvent>();
        events.add(new GameCreatedEvent(gameId, 1, "test", 2, 2, UUID.randomUUID(), 10, 20));
        events.add(new GameJoinedEvent(gameId, 2, player1Id));
        events.add(new GameJoinedEvent(gameId, 3, player2Id));
        events.add(new GameMovedToStartingStageEvent(gameId, 4));
        events.add(new GameTablesCreatedAndPlayersAssociatedEvent(gameId, 5, tableToPlayersMap, 2));
        events.add(new GameStartedEvent(gameId, 6, tableIds, new BlindSchedule(10)));

        var game = new DefaultGameFactory().createFrom(events);

        var playersToChipsMap = new HashMap<UUID, Integer>();
        playersToChipsMap.put(player1Id, 100);
        playersToChipsMap.put(player2Id, 100);

        game.attemptToStartNewHand(tableId, playersToChipsMap);
        
        assertEquals(7, game.fetchAppliedEvents().size());
        assertEquals(1, game.fetchNewEvents().size());
        assertEquals(7, game.fetchNewEvents().get(0).getVersion());
    }

}
