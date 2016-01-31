package com.flexpoker.table.command.aggregate.pot;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.HandRanking;
import com.flexpoker.model.card.CardRank;
import com.flexpoker.table.command.aggregate.HandEvaluation;

public class BasicPotTest {

    @Test
    public void testPotIsOpenOnCreate() {
        Pot pot = createGenericPot();
        assertTrue(pot.isOpen());
    }

    @Test
    public void testPotClosesAfterClosing() {
        Pot pot = createGenericPot();
        pot.closePot();
        assertFalse(pot.isOpen());
    }

    @Test(expected = FlexPokerException.class)
    public void testPotCannotBeAddedToAfterClosing() {
        Pot pot = createGenericPot();
        pot.closePot();
        pot.addChips(60);
    }

    @Test(expected = FlexPokerException.class)
    public void testPotCannotHaveAPlayerRemovedAfterClosing() {
        Pot pot = createGenericPot();
        pot.closePot();
        pot.removePlayer(UUID.randomUUID());
    }

    private Pot createGenericPot() {
        UUID player1 = UUID.randomUUID();
        UUID player2 = UUID.randomUUID();

        HandEvaluation handEvaluation1 = new HandEvaluation();
        handEvaluation1.setPlayerId(player1);
        handEvaluation1.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation1.setPrimaryCardRank(CardRank.EIGHT);
        HandEvaluation handEvaluation2 = new HandEvaluation();
        handEvaluation2.setPlayerId(player2);
        handEvaluation2.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation2.setPrimaryCardRank(CardRank.KING);

        Pot pot = new Pot(UUID.randomUUID(), new HashSet<>(Arrays.asList(handEvaluation1,
                handEvaluation2)));
        return pot;
    }

}
