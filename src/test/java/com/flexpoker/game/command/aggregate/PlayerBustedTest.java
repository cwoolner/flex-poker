package com.flexpoker.game.command.aggregate;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.game.command.commands.CreateGameCommand;
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
        game.bustPlayer(player2Id);

        assertEquals(8, game.fetchAppliedEvents().size());
        assertEquals(8, game.fetchNewEvents().size());
        assertEquals(8, game.fetchNewEvents().get(7).getVersion());
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
        game.bustPlayer(player2Id);
        game.bustPlayer(player1Id);

        assertEquals(9, game.fetchAppliedEvents().size());
        assertEquals(9, game.fetchNewEvents().size());
        assertEquals(9, game.fetchNewEvents().get(8).getVersion());
        assertEquals(PlayerBustedEvent.class, game.fetchAppliedEvents().get(7).getClass());
        assertEquals(PlayerBustedEvent.class, game.fetchAppliedEvents().get(8).getClass());
        assertEquals(player2Id, ((PlayerBustedEvent) game.fetchAppliedEvents().get(7)).getPlayerId());
        assertEquals(player1Id, ((PlayerBustedEvent) game.fetchAppliedEvents().get(8)).getPlayerId());
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
        game.bustPlayer(player4Id);
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
        game.bustPlayer(player2Id);
        game.bustPlayer(player2Id);
    }

}
