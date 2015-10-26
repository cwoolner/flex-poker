package com.flexpoker.table.command.aggregate.testhelpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.flexpoker.model.HandRanking;
import com.flexpoker.model.card.Card;
import com.flexpoker.model.card.CardRank;
import com.flexpoker.model.card.CardsUsedInHand;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.table.command.aggregate.DefaultTableFactory;
import com.flexpoker.table.command.aggregate.HandEvaluation;
import com.flexpoker.table.command.aggregate.Table;
import com.flexpoker.table.command.commands.CreateTableCommand;
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.test.util.datageneration.CardGenerator;
import com.flexpoker.test.util.datageneration.DeckGenerator;

public class TableTestUtils {

    public static Table createBasicTable(UUID tableId, UUID... playerIdsArray) {
        Set<UUID> playerIds = new HashSet<>(Arrays.asList(playerIdsArray));

        int smallBlind = 10;
        int bigBlind = 20;
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

        CreateTableCommand command = new CreateTableCommand(tableId, UUID.randomUUID(), playerIds, 6);
        Table table = new DefaultTableFactory().createNew(command);
        table.startNewHandForNewGame(smallBlind, bigBlind, shuffledDeckOfCards, cardsUsedInHand,
                handEvaluations);
        return table;
    }

    public static UUID fetchIdForButton(TableCreatedEvent tableCreatedEvent,
            HandDealtEvent handDealtEvent) {
        Map<Integer, UUID> seatPositionToPlayerIdMap = tableCreatedEvent
                .getSeatPositionToPlayerMap();
        return seatPositionToPlayerIdMap.get(handDealtEvent.getButtonOnPosition());
    }

    public static UUID fetchIdForSmallBlind(TableCreatedEvent tableCreatedEvent,
            HandDealtEvent handDealtEvent) {
        Map<Integer, UUID> seatPositionToPlayerIdMap = tableCreatedEvent
                .getSeatPositionToPlayerMap();
        return seatPositionToPlayerIdMap.get(handDealtEvent.getSmallBlindPosition());
    }

    public static UUID fetchIdForBigBlind(TableCreatedEvent tableCreatedEvent,
            HandDealtEvent handDealtEvent) {
        Map<Integer, UUID> seatPositionToPlayerIdMap = tableCreatedEvent
                .getSeatPositionToPlayerMap();
        return seatPositionToPlayerIdMap.get(handDealtEvent.getBigBlindPosition());
    }

}
