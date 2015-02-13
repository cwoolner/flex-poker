package com.flexpoker.table.command.aggregate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.HandRanking;
import com.flexpoker.model.Pot;
import com.flexpoker.model.card.CardRank;

public class PotTest {

    @Test
    public void testBasicTwoPersonPotHasSingleWinner() {
        UUID player1 = UUID.randomUUID();
        UUID player2 = UUID.randomUUID();

        HandEvaluation handEvaluation1 = new HandEvaluation();
        handEvaluation1.setPlayerId(player1);
        handEvaluation1.setHandRanking(HandRanking.FLUSH);
        handEvaluation1.setPrimaryCardRank(CardRank.EIGHT);
        handEvaluation1.setFirstKicker(CardRank.SEVEN);
        handEvaluation1.setSecondKicker(CardRank.FOUR);
        handEvaluation1.setThirdKicker(CardRank.THREE);
        handEvaluation1.setFourthKicker(CardRank.TWO);
        HandEvaluation handEvaluation2 = new HandEvaluation();
        handEvaluation2.setPlayerId(player2);
        handEvaluation2.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation2.setPrimaryCardRank(CardRank.KING);

        List<HandEvaluation> winningHands = new ArrayList<>();
        winningHands.add(handEvaluation1);
        winningHands.add(handEvaluation2);

        Pot pot = new Pot(UUID.randomUUID(), new HashSet<>(Arrays.asList(handEvaluation1,
                handEvaluation2)));
        pot.addChips(60);

        assertTrue(pot.forcePlayerToShowCards(player1));
        assertFalse(pot.forcePlayerToShowCards(player2));
        assertEquals(60, pot.getChipsWon(player1));
        assertEquals(0, pot.getChipsWon(player2));
    }

    @Test
    public void testThreePersonPotHasTwoWinners() {
        UUID player1 = UUID.randomUUID();
        UUID player2 = UUID.randomUUID();
        UUID player3 = UUID.randomUUID();

        HandEvaluation handEvaluation1 = new HandEvaluation();
        handEvaluation1.setPlayerId(player1);
        handEvaluation1.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation1.setPrimaryCardRank(CardRank.EIGHT);
        HandEvaluation handEvaluation2 = new HandEvaluation();
        handEvaluation2.setPlayerId(player2);
        handEvaluation2.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation2.setPrimaryCardRank(CardRank.KING);
        HandEvaluation handEvaluation3 = new HandEvaluation();
        handEvaluation3.setPlayerId(player3);
        handEvaluation3.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation3.setPrimaryCardRank(CardRank.KING);

        List<HandEvaluation> winningHands = new ArrayList<>();
        winningHands.add(handEvaluation1);
        winningHands.add(handEvaluation2);
        winningHands.add(handEvaluation3);

        Pot pot = new Pot(UUID.randomUUID(), new HashSet<>(Arrays.asList(handEvaluation1,
                handEvaluation2, handEvaluation3)));
        pot.addChips(60);

        assertFalse(pot.forcePlayerToShowCards(player1));
        assertTrue(pot.forcePlayerToShowCards(player2));
        assertTrue(pot.forcePlayerToShowCards(player3));
        assertEquals(0, pot.getChipsWon(player1));
        assertEquals(30, pot.getChipsWon(player2));
        assertEquals(30, pot.getChipsWon(player3));
    }

    @Test
    public void testThreePersonPotHasTwoWinnersWithBonus() {
        UUID player1 = UUID.randomUUID();
        UUID player2 = UUID.randomUUID();
        UUID player3 = UUID.randomUUID();

        HandEvaluation handEvaluation1 = new HandEvaluation();
        handEvaluation1.setPlayerId(player1);
        handEvaluation1.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation1.setPrimaryCardRank(CardRank.EIGHT);
        HandEvaluation handEvaluation2 = new HandEvaluation();
        handEvaluation2.setPlayerId(player2);
        handEvaluation2.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation2.setPrimaryCardRank(CardRank.KING);
        HandEvaluation handEvaluation3 = new HandEvaluation();
        handEvaluation3.setPlayerId(player3);
        handEvaluation3.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation3.setPrimaryCardRank(CardRank.KING);

        List<HandEvaluation> winningHands = new ArrayList<>();
        winningHands.add(handEvaluation1);
        winningHands.add(handEvaluation2);
        winningHands.add(handEvaluation3);

        Pot pot = new Pot(UUID.randomUUID(), new HashSet<>(Arrays.asList(handEvaluation1,
                handEvaluation2, handEvaluation3)));
        pot.addChips(61);

        assertFalse(pot.forcePlayerToShowCards(player1));
        assertTrue(pot.forcePlayerToShowCards(player2));
        assertTrue(pot.forcePlayerToShowCards(player3));
        assertEquals(0, pot.getChipsWon(player1));

        if (pot.getChipsWon(player2) == 30) {
            assertEquals(30, pot.getChipsWon(player2));
            assertEquals(31, pot.getChipsWon(player3));
        } else if (pot.getChipsWon(player3) == 30) {
            assertEquals(31, pot.getChipsWon(player2));
            assertEquals(30, pot.getChipsWon(player3));
        } else {
            throw new IllegalStateException(
                    "one of the pots should be 30 and the other should be 31");
        }
    }

    @Test
    public void testThreePersonPotHasTwoWinnersButOneOfTheWinnersFolds() {
        UUID player1 = UUID.randomUUID();
        UUID player2 = UUID.randomUUID();
        UUID player3 = UUID.randomUUID();

        HandEvaluation handEvaluation1 = new HandEvaluation();
        handEvaluation1.setPlayerId(player1);
        handEvaluation1.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation1.setPrimaryCardRank(CardRank.EIGHT);
        HandEvaluation handEvaluation2 = new HandEvaluation();
        handEvaluation2.setPlayerId(player2);
        handEvaluation2.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation2.setPrimaryCardRank(CardRank.KING);
        HandEvaluation handEvaluation3 = new HandEvaluation();
        handEvaluation3.setPlayerId(player3);
        handEvaluation3.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation3.setPrimaryCardRank(CardRank.KING);

        List<HandEvaluation> winningHands = new ArrayList<>();
        winningHands.add(handEvaluation1);
        winningHands.add(handEvaluation2);
        winningHands.add(handEvaluation3);

        Pot pot = new Pot(UUID.randomUUID(), new HashSet<>(Arrays.asList(handEvaluation1,
                handEvaluation2, handEvaluation3)));
        pot.addChips(60);
        pot.removePlayer(player3);

        assertFalse(pot.forcePlayerToShowCards(player1));
        assertTrue(pot.forcePlayerToShowCards(player2));
        assertFalse(pot.forcePlayerToShowCards(player3));
        assertEquals(0, pot.getChipsWon(player1));
        assertEquals(60, pot.getChipsWon(player2));
        assertEquals(0, pot.getChipsWon(player3));
    }

}
