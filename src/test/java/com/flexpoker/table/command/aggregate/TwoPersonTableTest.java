package com.flexpoker.table.command.aggregate;

import static com.flexpoker.table.command.framework.TableEventType.ActionOnChanged;
import static com.flexpoker.table.command.framework.TableEventType.CardsShuffled;
import static com.flexpoker.table.command.framework.TableEventType.FlopCardsDealt;
import static com.flexpoker.table.command.framework.TableEventType.HandCompleted;
import static com.flexpoker.table.command.framework.TableEventType.HandDealtEvent;
import static com.flexpoker.table.command.framework.TableEventType.LastToActChanged;
import static com.flexpoker.table.command.framework.TableEventType.PlayerCalled;
import static com.flexpoker.table.command.framework.TableEventType.PlayerChecked;
import static com.flexpoker.table.command.framework.TableEventType.PlayerFolded;
import static com.flexpoker.table.command.framework.TableEventType.RiverCardDealt;
import static com.flexpoker.table.command.framework.TableEventType.TableCreated;
import static com.flexpoker.table.command.framework.TableEventType.TurnCardDealt;
import static com.flexpoker.test.util.CommonAssertions.verifyEventIdsAndVersionNumbers;
import static com.flexpoker.test.util.CommonAssertions.verifyNumberOfEventsAndEntireOrderByType;

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

        verifyNumberOfEventsAndEntireOrderByType(6, newEvents, TableCreated,
                CardsShuffled, HandDealtEvent, ActionOnChanged, PlayerFolded,
                HandCompleted);
        verifyEventIdsAndVersionNumbers(tableId, newEvents);
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

        verifyNumberOfEventsAndEntireOrderByType(26, newEvents, TableCreated,
                CardsShuffled,
                HandDealtEvent,
                ActionOnChanged,
                // pre-flop
                PlayerCalled, ActionOnChanged, PlayerChecked, ActionOnChanged,
                LastToActChanged,
                FlopCardsDealt,
                // post-flop
                PlayerChecked, ActionOnChanged, PlayerChecked, ActionOnChanged,
                LastToActChanged, TurnCardDealt,
                // post-turn
                PlayerChecked, ActionOnChanged, PlayerChecked, ActionOnChanged,
                LastToActChanged, RiverCardDealt,
                // post-river
                PlayerChecked, ActionOnChanged, PlayerChecked, HandCompleted);
        verifyEventIdsAndVersionNumbers(tableId, newEvents);
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

        verifyNumberOfEventsAndEntireOrderByType(26, newEvents, TableCreated,
                CardsShuffled,
                HandDealtEvent,
                ActionOnChanged,
                // pre-flop
                PlayerCalled, ActionOnChanged, PlayerChecked, ActionOnChanged,
                LastToActChanged,
                FlopCardsDealt,
                // post-flop
                PlayerChecked, ActionOnChanged, PlayerChecked, ActionOnChanged,
                LastToActChanged, TurnCardDealt,
                // post-turn
                PlayerChecked, ActionOnChanged, PlayerChecked, ActionOnChanged,
                LastToActChanged, RiverCardDealt,
                // post-river
                PlayerChecked, ActionOnChanged, PlayerChecked, HandCompleted);
        verifyEventIdsAndVersionNumbers(tableId, newEvents);
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
