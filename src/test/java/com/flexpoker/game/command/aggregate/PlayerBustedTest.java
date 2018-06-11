package com.flexpoker.game.command.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.game.command.commands.CreateGameCommand;
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent;
import com.flexpoker.game.command.events.PlayerBustedGameEvent;

public class PlayerBustedTest {

    @Test
    public void testEventIsCreated() {
        var player1Id = UUID.randomUUID();
        var player2Id = UUID.randomUUID();
        var player3Id = UUID.randomUUID();
        var createGameCommand = new CreateGameCommand("test", 3, 3, player1Id, 1, 20);
        var game = new DefaultGameFactory().createNew(createGameCommand);
        game.joinGame(player1Id);
        game.joinGame(player2Id);
        game.joinGame(player3Id);

        var gameTablesCreatedAndPlayersAssociatedEvent = (GameTablesCreatedAndPlayersAssociatedEvent) game
                .fetchAppliedEvents().stream()
                .filter(x -> x.getClass().equals(GameTablesCreatedAndPlayersAssociatedEvent.class))
                .findFirst().get();
        var tableId = gameTablesCreatedAndPlayersAssociatedEvent.getTableIdToPlayerIdsMap()
                .keySet().iterator().next();

        var playerToChipsMap = new HashMap<UUID, Integer>();
        playerToChipsMap.put(player1Id, 100);
        playerToChipsMap.put(player2Id, 0);
        playerToChipsMap.put(player3Id, 100);
        game.attemptToStartNewHand(tableId, playerToChipsMap);

        assertEquals(9, game.fetchAppliedEvents().size());
        assertEquals(9, game.fetchNewEvents().size());
        assertEquals(PlayerBustedGameEvent.class, game.fetchAppliedEvents().get(7).getClass());
        assertEquals(player2Id, ((PlayerBustedGameEvent) game.fetchAppliedEvents().get(7)).getPlayerId());
    }

    @Test
    public void testRemovedUserFromTableCreatesEvent() {
        var player1Id = UUID.randomUUID();
        var player2Id = UUID.randomUUID();
        var player3Id = UUID.randomUUID();
        var createGameCommand = new CreateGameCommand("test", 3, 3, player1Id, 1, 20);
        var game = new DefaultGameFactory().createNew(createGameCommand);
        game.joinGame(player1Id);
        game.joinGame(player2Id);
        game.joinGame(player3Id);

        var gameTablesCreatedAndPlayersAssociatedEvent = (GameTablesCreatedAndPlayersAssociatedEvent) game
                .fetchAppliedEvents().stream()
                .filter(x -> x.getClass().equals(GameTablesCreatedAndPlayersAssociatedEvent.class)).findFirst().get();
        var tableId = gameTablesCreatedAndPlayersAssociatedEvent.getTableIdToPlayerIdsMap().keySet().iterator().next();

        var playerToChipsMap = new HashMap<UUID, Integer>();
        playerToChipsMap.put(player1Id, 100);
        playerToChipsMap.put(player3Id, 100);
        game.attemptToStartNewHand(tableId, playerToChipsMap);

        assertEquals(9, game.fetchAppliedEvents().size());
        assertEquals(9, game.fetchNewEvents().size());
        assertEquals(PlayerBustedGameEvent.class, game.fetchAppliedEvents().get(7).getClass());
        assertEquals(player2Id, ((PlayerBustedGameEvent) game.fetchAppliedEvents().get(7)).getPlayerId());
    }

    @Test
    public void testMultiplePlayersBust() {
        var player1Id = UUID.randomUUID();
        var player2Id = UUID.randomUUID();
        var player3Id = UUID.randomUUID();
        var createGameCommand = new CreateGameCommand("test", 3, 3, player1Id, 1, 20);
        var game = new DefaultGameFactory().createNew(createGameCommand);
        game.joinGame(player1Id);
        game.joinGame(player2Id);
        game.joinGame(player3Id);

        var gameTablesCreatedAndPlayersAssociatedEvent = (GameTablesCreatedAndPlayersAssociatedEvent) game
                .fetchAppliedEvents().stream()
                .filter(x -> x.getClass().equals(GameTablesCreatedAndPlayersAssociatedEvent.class)).findFirst().get();
        var tableId = gameTablesCreatedAndPlayersAssociatedEvent.getTableIdToPlayerIdsMap().keySet().iterator().next();

        var playerToChipsMap = new HashMap<UUID, Integer>();
        playerToChipsMap.put(player1Id, 0);
        playerToChipsMap.put(player2Id, 0);
        playerToChipsMap.put(player3Id, 100);
        game.attemptToStartNewHand(tableId, playerToChipsMap);

        assertEquals(9, game.fetchAppliedEvents().size());
        assertEquals(9, game.fetchNewEvents().size());
        assertEquals(PlayerBustedGameEvent.class, game.fetchAppliedEvents().get(7).getClass());
        assertEquals(PlayerBustedGameEvent.class, game.fetchAppliedEvents().get(8).getClass());

        var bustedPlayers = new HashSet<>();
        bustedPlayers.add(((PlayerBustedGameEvent) game.fetchAppliedEvents().get(7)).getPlayerId());
        bustedPlayers.add(((PlayerBustedGameEvent) game.fetchAppliedEvents().get(8)).getPlayerId());

        assertTrue(bustedPlayers.contains(player1Id));
        assertTrue(bustedPlayers.contains(player2Id));
    }

    @Test(expected = FlexPokerException.class)
    public void testInvalidPlayer() {
        var player1Id = UUID.randomUUID();
        var player2Id = UUID.randomUUID();
        var player3Id = UUID.randomUUID();
        var player4Id = UUID.randomUUID();
        var createGameCommand = new CreateGameCommand("test", 3, 3, player1Id, 1, 20);
        var game = new DefaultGameFactory().createNew(createGameCommand);
        game.joinGame(player1Id);
        game.joinGame(player2Id);
        game.joinGame(player3Id);

        var playerToChipsMap = new HashMap<UUID, Integer>();
        playerToChipsMap.put(player1Id, 0);
        playerToChipsMap.put(player2Id, 0);
        playerToChipsMap.put(player3Id, 100);
        playerToChipsMap.put(player4Id, 0);

        var gameTablesCreatedAndPlayersAssociatedEvent = (GameTablesCreatedAndPlayersAssociatedEvent) game
                .fetchAppliedEvents().stream()
                .filter(x -> x.getClass().equals(GameTablesCreatedAndPlayersAssociatedEvent.class)).findFirst().get();
        var tableId = gameTablesCreatedAndPlayersAssociatedEvent.getTableIdToPlayerIdsMap().keySet().iterator().next();

        game.attemptToStartNewHand(tableId, playerToChipsMap);
    }

    @Test(expected = FlexPokerException.class)
    public void testPlayerBustsTwice() {
        var player1Id = UUID.randomUUID();
        var player2Id = UUID.randomUUID();
        var player3Id = UUID.randomUUID();
        var createGameCommand = new CreateGameCommand("test", 3, 3, player1Id, 1, 20);
        var game = new DefaultGameFactory().createNew(createGameCommand);
        game.joinGame(player1Id);
        game.joinGame(player2Id);
        game.joinGame(player3Id);

        var gameTablesCreatedAndPlayersAssociatedEvent = (GameTablesCreatedAndPlayersAssociatedEvent) game
                .fetchAppliedEvents().stream()
                .filter(x -> x.getClass().equals(GameTablesCreatedAndPlayersAssociatedEvent.class)).findFirst().get();
        var tableId = gameTablesCreatedAndPlayersAssociatedEvent.getTableIdToPlayerIdsMap().keySet().iterator().next();

        var playerToChipsMap = new HashMap<UUID, Integer>();
        playerToChipsMap.put(player1Id, 0);
        playerToChipsMap.put(player2Id, 0);
        playerToChipsMap.put(player3Id, 100);
        game.attemptToStartNewHand(tableId, playerToChipsMap);

        playerToChipsMap = new HashMap<>();
        playerToChipsMap.put(player1Id, 0);
        playerToChipsMap.put(player2Id, 0);
        playerToChipsMap.put(player3Id, 100);
        game.attemptToStartNewHand(tableId, playerToChipsMap);
    }

}
