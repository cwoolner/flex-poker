package com.flexpoker.table.command.aggregate;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import com.flexpoker.model.Blinds;
import com.flexpoker.model.HandEvaluation;
import com.flexpoker.model.card.Card;
import com.flexpoker.model.card.CardsUsedInHand;
import com.flexpoker.model.card.PocketCards;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.framework.TableEvent;
import com.flexpoker.table.command.framework.TableEventType;
import com.flexpoker.test.util.CommonAssertions;
import com.flexpoker.test.util.datageneration.CardGenerator;
import com.flexpoker.test.util.datageneration.DeckGenerator;

public class TwoPersonTableTest {

    @Test
    public void testFoldDueToTimeoutSuccess() {
        UUID tableId = UUID.randomUUID();
        Set<UUID> playerIds = new HashSet<>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        Table table = createBasicTable(tableId, playerIds);

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

        CommonAssertions.verifyEventIdsAndVersionNumbers(tableId, newEvents);
    }

    @Test
    public void testSmallBlindCallAndBigBlindCheckDueToTimeoutSuccess() {
        UUID tableId = UUID.randomUUID();
        Set<UUID> playerIds = new HashSet<>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        Table table = createBasicTable(tableId, playerIds);

        // use the info in action on event to simulate the expire
        UUID smallBlindAndButtonPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents()
                .get(3)).getPlayerId();
        table.call(smallBlindAndButtonPlayerId);

        ActionOnChangedEvent actionOnChangedEvent = (ActionOnChangedEvent) table
                .fetchNewEvents().get(5);
        UUID bigBlindPlayerId = actionOnChangedEvent.getPlayerId();
        table.expireActionOn(actionOnChangedEvent.getHandId(), bigBlindPlayerId);

        // post-flop
        table.check(bigBlindPlayerId);
        table.check(smallBlindAndButtonPlayerId);

        // post-turn
        table.check(bigBlindPlayerId);
        table.check(smallBlindAndButtonPlayerId);

        // post-river
        table.check(bigBlindPlayerId);
        table.check(smallBlindAndButtonPlayerId);

        List<TableEvent> newEvents = table.fetchNewEvents();

        assertEquals(26, newEvents.size());
        assertEquals(TableEventType.TableCreated, newEvents.get(0).getType());
        assertEquals(TableEventType.CardsShuffled, newEvents.get(1).getType());
        assertEquals(TableEventType.HandDealtEvent, newEvents.get(2).getType());
        assertEquals(TableEventType.ActionOnChanged, newEvents.get(3).getType());

        // pre-flop
        assertEquals(TableEventType.PlayerCalled, newEvents.get(4).getType());
        assertEquals(TableEventType.ActionOnChanged, newEvents.get(5).getType());
        assertEquals(TableEventType.PlayerChecked, newEvents.get(6).getType());
        assertEquals(TableEventType.ActionOnChanged, newEvents.get(7).getType());
        assertEquals(TableEventType.LastToActChanged, newEvents.get(8).getType());
        assertEquals(TableEventType.FlopCardsDealt, newEvents.get(9).getType());

        // post-flop
        assertEquals(TableEventType.PlayerChecked, newEvents.get(10).getType());
        assertEquals(TableEventType.ActionOnChanged, newEvents.get(11).getType());
        assertEquals(TableEventType.PlayerChecked, newEvents.get(12).getType());
        assertEquals(TableEventType.ActionOnChanged, newEvents.get(13).getType());
        assertEquals(TableEventType.LastToActChanged, newEvents.get(14).getType());
        assertEquals(TableEventType.TurnCardDealt, newEvents.get(15).getType());

        // post-turn
        assertEquals(TableEventType.PlayerChecked, newEvents.get(16).getType());
        assertEquals(TableEventType.ActionOnChanged, newEvents.get(17).getType());
        assertEquals(TableEventType.PlayerChecked, newEvents.get(18).getType());
        assertEquals(TableEventType.ActionOnChanged, newEvents.get(19).getType());
        assertEquals(TableEventType.LastToActChanged, newEvents.get(20).getType());
        assertEquals(TableEventType.RiverCardDealt, newEvents.get(21).getType());

        // post-river
        assertEquals(TableEventType.PlayerChecked, newEvents.get(22).getType());
        assertEquals(TableEventType.ActionOnChanged, newEvents.get(23).getType());
        assertEquals(TableEventType.PlayerChecked, newEvents.get(24).getType());

        assertEquals(TableEventType.HandCompleted, newEvents.get(25).getType());

        CommonAssertions.verifyEventIdsAndVersionNumbers(tableId, newEvents);
    }

    @Test
    public void testSmallBlindCallAndChecksTillTheEndsSuccess() {
        UUID tableId = UUID.randomUUID();
        Set<UUID> playerIds = new HashSet<>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        Table table = createBasicTable(tableId, playerIds);

        // use the info in action on event to get the player id of the small
        // blind
        UUID smallBlindAndButtonPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents()
                .get(3)).getPlayerId();
        table.call(smallBlindAndButtonPlayerId);

        UUID bigBlindPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents().get(5))
                .getPlayerId();
        table.check(bigBlindPlayerId);

        // post-flop
        table.check(bigBlindPlayerId);
        table.check(smallBlindAndButtonPlayerId);

        // post-turn
        table.check(bigBlindPlayerId);
        table.check(smallBlindAndButtonPlayerId);

        // post-river
        table.check(bigBlindPlayerId);
        table.check(smallBlindAndButtonPlayerId);

        List<TableEvent> newEvents = table.fetchNewEvents();

        assertEquals(26, newEvents.size());
        assertEquals(TableEventType.TableCreated, newEvents.get(0).getType());
        assertEquals(TableEventType.CardsShuffled, newEvents.get(1).getType());
        assertEquals(TableEventType.HandDealtEvent, newEvents.get(2).getType());
        assertEquals(TableEventType.ActionOnChanged, newEvents.get(3).getType());

        // pre-flop
        assertEquals(TableEventType.PlayerCalled, newEvents.get(4).getType());
        assertEquals(TableEventType.ActionOnChanged, newEvents.get(5).getType());
        assertEquals(TableEventType.PlayerChecked, newEvents.get(6).getType());
        assertEquals(TableEventType.ActionOnChanged, newEvents.get(7).getType());
        assertEquals(TableEventType.LastToActChanged, newEvents.get(8).getType());
        assertEquals(TableEventType.FlopCardsDealt, newEvents.get(9).getType());

        // post-flop
        assertEquals(TableEventType.PlayerChecked, newEvents.get(10).getType());
        assertEquals(TableEventType.ActionOnChanged, newEvents.get(11).getType());
        assertEquals(TableEventType.PlayerChecked, newEvents.get(12).getType());
        assertEquals(TableEventType.ActionOnChanged, newEvents.get(13).getType());
        assertEquals(TableEventType.LastToActChanged, newEvents.get(14).getType());
        assertEquals(TableEventType.TurnCardDealt, newEvents.get(15).getType());

        // post-turn
        assertEquals(TableEventType.PlayerChecked, newEvents.get(16).getType());
        assertEquals(TableEventType.ActionOnChanged, newEvents.get(17).getType());
        assertEquals(TableEventType.PlayerChecked, newEvents.get(18).getType());
        assertEquals(TableEventType.ActionOnChanged, newEvents.get(19).getType());
        assertEquals(TableEventType.LastToActChanged, newEvents.get(20).getType());
        assertEquals(TableEventType.RiverCardDealt, newEvents.get(21).getType());

        // post-river
        assertEquals(TableEventType.PlayerChecked, newEvents.get(22).getType());
        assertEquals(TableEventType.ActionOnChanged, newEvents.get(23).getType());
        assertEquals(TableEventType.PlayerChecked, newEvents.get(24).getType());

        assertEquals(TableEventType.HandCompleted, newEvents.get(25).getType());

        CommonAssertions.verifyEventIdsAndVersionNumbers(tableId, newEvents);
    }

    private Table createBasicTable(UUID tableId, Set<UUID> playerIds) {
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
