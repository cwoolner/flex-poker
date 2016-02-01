package com.flexpoker.table.command.aggregate.pot;

import static com.flexpoker.table.command.aggregate.pot.PotTestUtils.createBasicPotHandler;
import static com.flexpoker.table.command.aggregate.pot.PotTestUtils.createSetOfPlayers;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.exception.FlexPokerException;

public class BasicPotHandlerTest {

    @Test
    public void testTwoPlayersRequiredToShowCardsAndChipsWon() {
        UUID player1 = UUID.randomUUID();
        UUID player2 = UUID.randomUUID();
        UUID potId1 = UUID.randomUUID();
        UUID potId2 = UUID.randomUUID();
        Set<UUID> playersInvolved = createSetOfPlayers(player1, player2);

        PotHandler potHandler = createBasicPotHandler(player1, player2);
        potHandler.addNewPot(potId1, playersInvolved);
        potHandler.addNewPot(potId2, playersInvolved);
        potHandler.addToPot(potId1, 20);
        potHandler.addToPot(potId2, 10);

        Set<UUID> playersRequriedToShowCards = potHandler
                .fetchPlayersRequriedToShowCards(playersInvolved);
        Map<UUID, Integer> fetchChipsWon = potHandler
                .fetchChipsWon(playersInvolved);

        assertTrue(playersRequriedToShowCards.contains(player1));
        assertFalse(playersRequriedToShowCards.contains(player2));
        assertEquals(30, fetchChipsWon.get(player1).intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNewPotWithPlayersThatDontExist() {
        UUID player1 = UUID.randomUUID();
        UUID player2 = UUID.randomUUID();
        UUID player3 = UUID.randomUUID();

        PotHandler potHandler = createBasicPotHandler(player1, player2);
        
        UUID potId = UUID.randomUUID();
        potHandler.addNewPot(potId, createSetOfPlayers(player3));
    }

    @Test
    public void testClosePot() {
        UUID player1 = UUID.randomUUID();
        UUID player2 = UUID.randomUUID();

        PotHandler potHandler = createBasicPotHandler(player1, player2);
        
        UUID potId = UUID.randomUUID();
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
        UUID player1 = UUID.randomUUID();
        UUID player2 = UUID.randomUUID();

        PotHandler potHandler = createBasicPotHandler(player1, player2);
        
        UUID potId = UUID.randomUUID();
        potHandler.addNewPot(potId, createSetOfPlayers(player1, player2));
        potHandler.closePot(potId);
        potHandler.addToPot(potId, 10);
    }

}
