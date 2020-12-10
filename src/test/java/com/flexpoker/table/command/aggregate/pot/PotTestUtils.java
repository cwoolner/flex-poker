package com.flexpoker.table.command.aggregate.pot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.flexpoker.table.command.HandRanking;
import com.flexpoker.table.command.CardRank;
import com.flexpoker.table.command.aggregate.HandEvaluation;

public class PotTestUtils {

    static PotHandler createBasicPotHandler(UUID player1, UUID player2) {
        var handEvaluation1 = new HandEvaluation();
        handEvaluation1.setPlayerId(player1);
        handEvaluation1.setHandRanking(HandRanking.FLUSH);
        handEvaluation1.setPrimaryCardRank(CardRank.EIGHT);
        handEvaluation1.setFirstKicker(CardRank.SEVEN);
        handEvaluation1.setSecondKicker(CardRank.FOUR);
        handEvaluation1.setThirdKicker(CardRank.THREE);
        handEvaluation1.setFourthKicker(CardRank.TWO);
        var handEvaluation2 = new HandEvaluation();
        handEvaluation2.setPlayerId(player2);
        handEvaluation2.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation2.setPrimaryCardRank(CardRank.KING);

        var winningHands = new ArrayList<HandEvaluation>();
        winningHands.add(handEvaluation1);
        winningHands.add(handEvaluation2);

        return new PotHandler(UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), winningHands);
    }

    static Set<UUID> createSetOfPlayers(UUID... players) {
        return new HashSet<>(Arrays.asList(players));
    }

}
