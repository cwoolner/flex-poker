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
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.card.Card;
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
        Map<PocketCards, HandEvaluation> handEvaluations = new HashMap<>();
        handEvaluations.put(CardGenerator.createPocketCards1(), new HandEvaluation());
        handEvaluations.put(CardGenerator.createPocketCards2(), new HandEvaluation());

        Table table = new DefaultTableFactory().createNew(tableId, UUID.randomUUID(), 6);
        table.createNewTable(playerIds);
        table.startNewHandForNewGame(blinds, shuffledDeckOfCards, cardsUsedInHand,
                handEvaluations);
        return table;
    }
}
