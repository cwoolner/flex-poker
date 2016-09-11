package com.flexpoker.game.command.aggregate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.game.command.commands.CreateGameCommand;
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent;
import com.flexpoker.game.command.events.PlayerBustedEvent;

public class PlayerBustedTest {

    @Test
    public void testEventIsCreated() {
        UUID player1Id = UUID.randomUUID();
        UUID player2Id = UUID.randomUUID();
        UUID player3Id = UUID.randomUUID();
        CreateGameCommand createGameCommand = new CreateGameCommand("test", 3, 3, player1Id, 1);
        Game game = new DefaultGameFactory().createNew(createGameCommand);
        game.joinGame(player1Id);
        game.joinGame(player2Id);
        game.joinGame(player3Id);

        GameTablesCreatedAndPlayersAssociatedEvent gameTablesCreatedAndPlayersAssociatedEvent = (GameTablesCreatedAndPlayersAssociatedEvent) game
                .fetchAppliedEvents().stream()
                .filter(x -> x.getClass().equals(GameTablesCreatedAndPlayersAssociatedEvent.class)).findFirst().get();
        UUID tableId = gameTablesCreatedAndPlayersAssociatedEvent.getTableIdToPlayerIdsMap().keySet().iterator().next();

        Map<UUID, Integer> playerToChipsMap = new HashMap<>();
        playerToChipsMap.put(player1Id, 100);
        playerToChipsMap.put(player2Id, 0);
        playerToChipsMap.put(player3Id, 100);
        game.attemptToStartNewHand(tableId, playerToChipsMap);

        assertEquals(9, game.fetchAppliedEvents().size());
        assertEquals(9, game.fetchNewEvents().size());
        assertEquals(9, game.fetchNewEvents().get(8).getVersion());
        assertEquals(PlayerBustedEvent.class, game.fetchAppliedEvents().get(7).getClass());
        assertEquals(player2Id, ((PlayerBustedEvent) game.fetchAppliedEvents().get(7)).getPlayerId());
    }

    @Test
    public void testRemovedUserFromTableCreatesEvent() {
        UUID player1Id = UUID.randomUUID();
        UUID player2Id = UUID.randomUUID();
        UUID player3Id = UUID.randomUUID();
        CreateGameCommand createGameCommand = new CreateGameCommand("test", 3, 3, player1Id, 1);
        Game game = new DefaultGameFactory().createNew(createGameCommand);
        game.joinGame(player1Id);
        game.joinGame(player2Id);
        game.joinGame(player3Id);

        GameTablesCreatedAndPlayersAssociatedEvent gameTablesCreatedAndPlayersAssociatedEvent = (GameTablesCreatedAndPlayersAssociatedEvent) game
                .fetchAppliedEvents().stream()
                .filter(x -> x.getClass().equals(GameTablesCreatedAndPlayersAssociatedEvent.class)).findFirst().get();
        UUID tableId = gameTablesCreatedAndPlayersAssociatedEvent.getTableIdToPlayerIdsMap().keySet().iterator().next();

        Map<UUID, Integer> playerToChipsMap = new HashMap<>();
        playerToChipsMap.put(player1Id, 100);
        playerToChipsMap.put(player3Id, 100);
        game.attemptToStartNewHand(tableId, playerToChipsMap);

        assertEquals(9, game.fetchAppliedEvents().size());
        assertEquals(9, game.fetchNewEvents().size());
        assertEquals(9, game.fetchNewEvents().get(8).getVersion());
        assertEquals(PlayerBustedEvent.class, game.fetchAppliedEvents().get(7).getClass());
        assertEquals(player2Id, ((PlayerBustedEvent) game.fetchAppliedEvents().get(7)).getPlayerId());
    }

    @Test
    public void testMultiplePlayersBust() {
        UUID player1Id = UUID.randomUUID();
        UUID player2Id = UUID.randomUUID();
        UUID player3Id = UUID.randomUUID();
        CreateGameCommand createGameCommand = new CreateGameCommand("test", 3, 3, player1Id, 1);
        Game game = new DefaultGameFactory().createNew(createGameCommand);
        game.joinGame(player1Id);
        game.joinGame(player2Id);
        game.joinGame(player3Id);

        GameTablesCreatedAndPlayersAssociatedEvent gameTablesCreatedAndPlayersAssociatedEvent = (GameTablesCreatedAndPlayersAssociatedEvent) game
                .fetchAppliedEvents().stream()
                .filter(x -> x.getClass().equals(GameTablesCreatedAndPlayersAssociatedEvent.class)).findFirst().get();
        UUID tableId = gameTablesCreatedAndPlayersAssociatedEvent.getTableIdToPlayerIdsMap().keySet().iterator().next();

        Map<UUID, Integer> playerToChipsMap = new HashMap<>();
        playerToChipsMap.put(player1Id, 0);
        playerToChipsMap.put(player2Id, 0);
        playerToChipsMap.put(player3Id, 100);
        game.attemptToStartNewHand(tableId, playerToChipsMap);

        assertEquals(9, game.fetchAppliedEvents().size());
        assertEquals(9, game.fetchNewEvents().size());
        assertEquals(9, game.fetchNewEvents().get(8).getVersion());
        assertEquals(PlayerBustedEvent.class, game.fetchAppliedEvents().get(7).getClass());
        assertEquals(PlayerBustedEvent.class, game.fetchAppliedEvents().get(8).getClass());

        Set<UUID> bustedPlayers = new HashSet<>();
        bustedPlayers.add(((PlayerBustedEvent) game.fetchAppliedEvents().get(7)).getPlayerId());
        bustedPlayers.add(((PlayerBustedEvent) game.fetchAppliedEvents().get(8)).getPlayerId());

        assertTrue(bustedPlayers.contains(player1Id));
        assertTrue(bustedPlayers.contains(player2Id));
    }

    @Test(expected = FlexPokerException.class)
    public void testInvalidPlayer() {
        UUID player1Id = UUID.randomUUID();
        UUID player2Id = UUID.randomUUID();
        UUID player3Id = UUID.randomUUID();
        UUID player4Id = UUID.randomUUID();
        CreateGameCommand createGameCommand = new CreateGameCommand("test", 3, 3, player1Id, 1);
        Game game = new DefaultGameFactory().createNew(createGameCommand);
        game.joinGame(player1Id);
        game.joinGame(player2Id);
        game.joinGame(player3Id);

        Map<UUID, Integer> playerToChipsMap = new HashMap<>();
        playerToChipsMap.put(player1Id, 0);
        playerToChipsMap.put(player2Id, 0);
        playerToChipsMap.put(player3Id, 100);
        playerToChipsMap.put(player4Id, 0);

        GameTablesCreatedAndPlayersAssociatedEvent gameTablesCreatedAndPlayersAssociatedEvent = (GameTablesCreatedAndPlayersAssociatedEvent) game
                .fetchAppliedEvents().stream()
                .filter(x -> x.getClass().equals(GameTablesCreatedAndPlayersAssociatedEvent.class)).findFirst().get();
        UUID tableId = gameTablesCreatedAndPlayersAssociatedEvent.getTableIdToPlayerIdsMap().keySet().iterator().next();

        game.attemptToStartNewHand(tableId, playerToChipsMap);
    }

    @Test(expected = FlexPokerException.class)
    public void testPlayerBustsTwice() {
        UUID player1Id = UUID.randomUUID();
        UUID player2Id = UUID.randomUUID();
        UUID player3Id = UUID.randomUUID();
        CreateGameCommand createGameCommand = new CreateGameCommand("test", 3, 3, player1Id, 1);
        Game game = new DefaultGameFactory().createNew(createGameCommand);
        game.joinGame(player1Id);
        game.joinGame(player2Id);
        game.joinGame(player3Id);

        GameTablesCreatedAndPlayersAssociatedEvent gameTablesCreatedAndPlayersAssociatedEvent = (GameTablesCreatedAndPlayersAssociatedEvent) game
                .fetchAppliedEvents().stream()
                .filter(x -> x.getClass().equals(GameTablesCreatedAndPlayersAssociatedEvent.class)).findFirst().get();
        UUID tableId = gameTablesCreatedAndPlayersAssociatedEvent.getTableIdToPlayerIdsMap().keySet().iterator().next();

        Map<UUID, Integer> playerToChipsMap = new HashMap<>();
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
