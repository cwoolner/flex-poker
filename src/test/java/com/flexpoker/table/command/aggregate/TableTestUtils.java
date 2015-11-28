package com.flexpoker.table.command.aggregate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.flexpoker.model.Blinds;
import com.flexpoker.model.HandRanking;
import com.flexpoker.model.card.Card;
import com.flexpoker.model.card.CardRank;
import com.flexpoker.model.card.CardsUsedInHand;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.test.util.datageneration.CardGenerator;
import com.flexpoker.test.util.datageneration.DeckGenerator;

public class TableTestUtils {

    static Table createBasicTable(UUID tableId, UUID... playerIdsArray) {
        Set<UUID> playerIds = new HashSet<>();
        Collections.addAll(playerIds, playerIdsArray);

        Blinds blinds = new Blinds(10, 20);
        List<Card> shuffledDeckOfCards = new ArrayList<>();
        CardsUsedInHand cardsUsedInHand = DeckGenerator.createDeck();

        HandEvaluation handEvaluation1 = new HandEvaluation();
        handEvaluation1.setPlayerId(playerIdsArray[0]);
        handEvaluation1.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation1.setPrimaryCardRank(CardRank.KING);

        HandEvaluation handEvaluation2 = new HandEvaluation();
        handEvaluation2.setPlayerId(playerIdsArray[1]);
        handEvaluation2.setHandRanking(HandRanking.STRAIGHT);
        handEvaluation2.setPrimaryCardRank(CardRank.KING);

        Map<PocketCards, HandEvaluation> handEvaluations = new HashMap<>();
        handEvaluations.put(CardGenerator.createPocketCards1(), handEvaluation1);
        handEvaluations.put(CardGenerator.createPocketCards2(), handEvaluation2);

        Table table = new DefaultTableFactory().createNew(tableId, UUID.randomUUID(), 6);
        table.createNewTable(playerIds);
        table.startNewHandForNewGame(blinds, shuffledDeckOfCards, cardsUsedInHand,
                handEvaluations);
        return table;
    }
}
