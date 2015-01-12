package com.flexpoker.table.command.aggregate;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.Blinds;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.card.Card;
import com.flexpoker.model.card.CardsUsedInHand;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.factory.TableFactory;
import com.flexpoker.table.command.framework.TableEvent;
import com.flexpoker.table.command.framework.TableEventType;
import com.flexpoker.test.util.datageneration.CardGenerator;
import com.flexpoker.test.util.datageneration.DeckGenerator;

public class TableTest {

    private final TableFactory tableFactory = new DefaultTableFactory();

    @Test
    public void testCreateNewTestSuccess() {
        UUID tableId = UUID.randomUUID();
        Set<UUID> playerIds = new HashSet<>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        Table table = tableFactory.createNew(tableId, UUID.randomUUID(), 6);
        table.createNewTable(playerIds);

        List<TableEvent> newEvents = table.fetchNewEvents();

        assertEquals(1, newEvents.size());
        assertEquals(TableEventType.TableCreated, newEvents.get(0).getType());

        verifyEventIdsAndVersionNumbers(tableId, newEvents);
    }

    @Test(expected = FlexPokerException.class)
    public void testCantCallCreateNewTableTwice() {
        UUID tableId = UUID.randomUUID();
        Set<UUID> playerIds = new HashSet<>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        Table table = tableFactory.createNew(tableId, UUID.randomUUID(), 6);
        table.createNewTable(playerIds);
        table.createNewTable(playerIds);
    }

    @Test(expected = FlexPokerException.class)
    public void testCantCreateATableGreaterThanMaxSize() {
        UUID tableId = UUID.randomUUID();
        Set<UUID> playerIds = new HashSet<>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        Table table = tableFactory.createNew(tableId, UUID.randomUUID(), 2);
        table.createNewTable(playerIds);
    }

    @Test(expected = FlexPokerException.class)
    public void testCantCreateATableWithOnlyOnePlayer() {
        UUID tableId = UUID.randomUUID();
        Set<UUID> playerIds = new HashSet<>();
        playerIds.add(UUID.randomUUID());

        Table table = tableFactory.createNew(tableId, UUID.randomUUID(), 2);
        table.createNewTable(playerIds);
    }

    @Test
    public void testCreateNewTableWithTwoPlayersAndFoldDueToTimeoutSuccess() {
        UUID tableId = UUID.randomUUID();
        Set<UUID> playerIds = new HashSet<>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        Blinds blinds = new Blinds(10, 20);
        List<Card> shuffledDeckOfCards = new ArrayList<>();
        CardsUsedInHand cardsUsedInHand = DeckGenerator.createDeck();
        Map<PocketCards, HandEvaluation> handEvaluations = new HashMap<>();
        handEvaluations.put(CardGenerator.createPocketCards1(), new HandEvaluation());
        handEvaluations.put(CardGenerator.createPocketCards2(), new HandEvaluation());

        Table table = tableFactory.createNew(tableId, UUID.randomUUID(), 6);
        table.createNewTable(playerIds);
        table.startNewHandForNewGame(blinds, shuffledDeckOfCards, cardsUsedInHand,
                handEvaluations);

        // use the info in action on event to simulate the expire
        ActionOnChangedEvent actionOnChangedEvent = (ActionOnChangedEvent) table
                .fetchNewEvents().get(3);
        table.expireActionOn(actionOnChangedEvent.getHandId(),
                actionOnChangedEvent.getPlayerId());

        List<TableEvent> newEvents = table.fetchNewEvents();

        assertEquals(6, newEvents.size());
        assertEquals(TableEventType.TableCreated, newEvents.get(0).getType());
        assertEquals(TableEventType.CardsShuffled, newEvents.get(1).getType());
        assertEquals(TableEventType.HandDealtEvent, newEvents.get(2).getType());
        assertEquals(TableEventType.ActionOnChanged, newEvents.get(3).getType());
        assertEquals(TableEventType.PlayerFolded, newEvents.get(4).getType());
        assertEquals(TableEventType.HandCompleted, newEvents.get(5).getType());

        verifyEventIdsAndVersionNumbers(tableId, newEvents);
    }

    private void verifyEventIdsAndVersionNumbers(UUID tableId, List<TableEvent> newEvents) {
        int version = 0;
        for (TableEvent tableEvent : newEvents) {
            assertEquals(tableId, tableEvent.getAggregateId());
            assertEquals(++version, tableEvent.getVersion());
        }
    }

}
