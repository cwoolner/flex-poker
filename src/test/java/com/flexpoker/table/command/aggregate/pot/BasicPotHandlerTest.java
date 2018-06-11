package com.flexpoker.table.command.aggregate.pot;

import static com.flexpoker.table.command.aggregate.pot.PotTestUtils.createBasicPotHandler;
import static com.flexpoker.table.command.aggregate.pot.PotTestUtils.createSetOfPlayers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.flexpoker.exception.FlexPokerException;

public class BasicPotHandlerTest {

    @Test
    public void testTwoPlayersRequiredToShowCardsAndChipsWon() {
        var player1 = UUID.randomUUID();
        var player2 = UUID.randomUUID();
        var potId1 = UUID.randomUUID();
        var potId2 = UUID.randomUUID();
        var playersInvolved = createSetOfPlayers(player1, player2);

        var potHandler = createBasicPotHandler(player1, player2);
        potHandler.addNewPot(potId1, playersInvolved);
        potHandler.addNewPot(potId2, playersInvolved);
        potHandler.addToPot(potId1, 20);
        potHandler.addToPot(potId2, 10);

        var playersRequriedToShowCards = potHandler.fetchPlayersRequriedToShowCards(playersInvolved);
        var fetchChipsWon = potHandler.fetchChipsWon(playersInvolved);

        assertTrue(playersRequriedToShowCards.contains(player1));
        assertFalse(playersRequriedToShowCards.contains(player2));
        assertEquals(30, fetchChipsWon.get(player1).intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNewPotWithPlayersThatDontExist() {
        var player1 = UUID.randomUUID();
        var player2 = UUID.randomUUID();
        var player3 = UUID.randomUUID();

        var potHandler = createBasicPotHandler(player1, player2);
        
        var potId = UUID.randomUUID();
        potHandler.addNewPot(potId, createSetOfPlayers(player3));
    }

    @Test
    public void testClosePot() {
        var player1 = UUID.randomUUID();
        var player2 = UUID.randomUUID();

        var potHandler = createBasicPotHandler(player1, player2);
        
        var potId = UUID.randomUUID();
        potHandler.addNewPot(potId, createSetOfPlayers(player1, player2));
        potHandler.closePot(potId);
    }

    @Test(expected = NoSuchElementException.class)
    public void testClosePotThatDoesntExist() {
        PotHandler potHandler = createBasicPotHandler(UUID.randomUUID(),
                UUID.randomUUID());
        potHandler.closePot(UUID.randomUUID());
    }

    @Test(expected = FlexPokerException.class)
    public void testAddChipsToClosedPot() {
        var player1 = UUID.randomUUID();
        var player2 = UUID.randomUUID();

        var potHandler = createBasicPotHandler(player1, player2);
        
        var potId = UUID.randomUUID();
        potHandler.addNewPot(potId, createSetOfPlayers(player1, player2));
        potHandler.closePot(potId);
        potHandler.addToPot(potId, 10);
    }

}
