package com.flexpoker.table.command.aggregate.pot;

import com.flexpoker.exception.FlexPokerException;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.UUID;

import static com.flexpoker.table.command.aggregate.pot.PotTestUtils.createBasicPotHandler;
import static com.flexpoker.table.command.aggregate.pot.PotTestUtils.createSetOfPlayers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BasicPotHandlerTest {

    @Test
    void testTwoPlayersRequiredToShowCardsAndChipsWon() {
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

        var playersRequiredToShowCards = potHandler.fetchPlayersRequiredToShowCards(playersInvolved);
        var fetchChipsWon = potHandler.fetchChipsWon(playersInvolved);

        assertTrue(playersRequiredToShowCards.contains(player1));
        assertFalse(playersRequiredToShowCards.contains(player2));
        assertEquals(30, fetchChipsWon.get(player1).intValue());
    }

    @Test
    void testAddNewPotWithPlayersThatDontExist() {
        var player1 = UUID.randomUUID();
        var player2 = UUID.randomUUID();
        var player3 = UUID.randomUUID();

        var potHandler = createBasicPotHandler(player1, player2);
        
        var potId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class,
                () -> potHandler.addNewPot(potId, createSetOfPlayers(player3)));
    }

    @Test
    void testClosePot() {
        var player1 = UUID.randomUUID();
        var player2 = UUID.randomUUID();

        var potHandler = createBasicPotHandler(player1, player2);
        
        var potId = UUID.randomUUID();
        potHandler.addNewPot(potId, createSetOfPlayers(player1, player2));
        potHandler.closePot(potId);
    }

    @Test
    void testClosePotThatDoesntExist() {
        PotHandler potHandler = createBasicPotHandler(UUID.randomUUID(), UUID.randomUUID());
        assertThrows(NoSuchElementException.class,
                () -> potHandler.closePot(UUID.randomUUID()));
    }

    @Test
    void testAddChipsToClosedPot() {
        var player1 = UUID.randomUUID();
        var player2 = UUID.randomUUID();

        var potHandler = createBasicPotHandler(player1, player2);
        
        var potId = UUID.randomUUID();
        potHandler.addNewPot(potId, createSetOfPlayers(player1, player2));
        potHandler.closePot(potId);

        assertThrows(FlexPokerException.class,
                () -> potHandler.addToPot(potId, 10));
    }

}
