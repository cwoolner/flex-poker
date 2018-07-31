package com.flexpoker.table.command.aggregate.pot;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.HandRanking;
import com.flexpoker.model.card.CardRank;
import com.flexpoker.table.command.aggregate.HandEvaluation;

public class BasicPotTest {

    @Test
    void testPotIsOpenOnCreate() {
        var pot = createGenericPot();
        assertTrue(pot.isOpen());
    }

    @Test
    void testPotClosesAfterClosing() {
        var pot = createGenericPot();
        pot.closePot();
        assertFalse(pot.isOpen());
    }

    @Test
    void testPotCannotBeAddedToAfterClosing() {
        var pot = createGenericPot();
        pot.closePot();
        assertThrows(FlexPokerException.class, () -> pot.addChips(60));
    }

    @Test
    void testPotCannotHaveAPlayerRemovedAfterClosing() {
        var pot = createGenericPot();
        pot.closePot();
        assertThrows(FlexPokerException.class, () -> pot.removePlayer(UUID.randomUUID()));
    }

    private Pot createGenericPot() {
        var player1 = UUID.randomUUID();
        var player2 = UUID.randomUUID();

        var handEvaluation1 = new HandEvaluation();
        handEvaluation1.setPlayerId(player1);
        handEvaluation1.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation1.setPrimaryCardRank(CardRank.EIGHT);
        HandEvaluation handEvaluation2 = new HandEvaluation();
        handEvaluation2.setPlayerId(player2);
        handEvaluation2.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation2.setPrimaryCardRank(CardRank.KING);

        var pot = new Pot(UUID.randomUUID(), new HashSet<>(Arrays.asList(handEvaluation1, handEvaluation2)));
        return pot;
    }

}
