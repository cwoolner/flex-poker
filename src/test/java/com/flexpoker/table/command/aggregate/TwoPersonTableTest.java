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
import static com.flexpoker.table.command.framework.TableEventType.PlayerRaised;
import static com.flexpoker.table.command.framework.TableEventType.RiverCardDealt;
import static com.flexpoker.table.command.framework.TableEventType.TableCreated;
import static com.flexpoker.table.command.framework.TableEventType.TurnCardDealt;
import static com.flexpoker.test.util.CommonAssertions.verifyEventIdsAndVersionNumbers;
import static com.flexpoker.test.util.CommonAssertions.verifyNumberOfEventsAndEntireOrderByType;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Test;

import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class TwoPersonTableTest {

    @Test
    public void testSeatPositionsAndButtonAndBlinds() {
        UUID tableId = UUID.randomUUID();
        Set<UUID> playerIds = new HashSet<>();
        UUID player1Id = UUID.randomUUID();
        UUID player2Id = UUID.randomUUID();
        playerIds.add(player1Id);
        playerIds.add(player2Id);

        Table table = TableTestUtils.createBasicTable(tableId, playerIds);

        // check seat positions
        Map<Integer, UUID> seatPositionToPlayerIdMap = ((TableCreatedEvent) table
                .fetchNewEvents().get(0)).getSeatPositionToPlayerMap();

        List<UUID> player1MatchList = seatPositionToPlayerIdMap.values().stream()
                .filter(x -> x.equals(player1Id)).collect(Collectors.toList());
        List<UUID> player2MatchList = seatPositionToPlayerIdMap.values().stream()
                .filter(x -> x.equals(player2Id)).collect(Collectors.toList());

        // verify that each player id is only in one seat position and that
        // those are the only two filled-in positions
        assertEquals(1, player1MatchList.size());
        assertEquals(1, player2MatchList.size());

        long numberOfOtherPlayerPositions = seatPositionToPlayerIdMap.values().stream()
                .filter(x -> !x.equals(player1Id)).filter(x -> !x.equals(player2Id))
                .distinct().count();
        assertEquals(0, numberOfOtherPlayerPositions);

        // check blinds
        int player1Position = seatPositionToPlayerIdMap.entrySet().stream()
                .filter(x -> x.getValue().equals(player1Id)).findAny().get().getKey()
                .intValue();
        int player2Position = seatPositionToPlayerIdMap.entrySet().stream()
                .filter(x -> x.getValue().equals(player2Id)).findAny().get().getKey()
                .intValue();

        HandDealtEvent handDealtEvent = ((HandDealtEvent) table.fetchNewEvents().get(2));

        if (player1Position == handDealtEvent.getButtonOnPosition()) {
            assertEquals(player1Position, handDealtEvent.getButtonOnPosition());
            assertEquals(player1Position, handDealtEvent.getSmallBlindPosition());
            assertEquals(player2Position, handDealtEvent.getBigBlindPosition());
        } else if (player2Position == handDealtEvent.getButtonOnPosition()) {
            assertEquals(player2Position, handDealtEvent.getButtonOnPosition());
            assertEquals(player2Position, handDealtEvent.getSmallBlindPosition());
            assertEquals(player1Position, handDealtEvent.getBigBlindPosition());
        } else {
            throw new IllegalStateException(
                    "for a new two-player hand, one of the players must be the button");
        }

        List<TableEvent> newEvents = table.fetchNewEvents();

        verifyNumberOfEventsAndEntireOrderByType(4, newEvents, TableCreated,
                CardsShuffled, HandDealtEvent, ActionOnChanged);
        verifyEventIdsAndVersionNumbers(tableId, newEvents);
    }

    @Test
    public void testFoldDueToTimeoutSuccess() {
        UUID tableId = UUID.randomUUID();
        Set<UUID> playerIds = new HashSet<>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        Table table = TableTestUtils.createBasicTable(tableId, playerIds);

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

        Table table = TableTestUtils.createBasicTable(tableId, playerIds);

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

        Table table = TableTestUtils.createBasicTable(tableId, playerIds);

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

    @Test
    public void testRaiseBySmallBlindAndBigBlindFoldsSuccess() {
        UUID tableId = UUID.randomUUID();
        Set<UUID> playerIds = new HashSet<>();
        playerIds.add(UUID.randomUUID());
        playerIds.add(UUID.randomUUID());

        Table table = TableTestUtils.createBasicTable(tableId, playerIds);

        // use the info in action on event to determine who the small
        // blind/button is on
        UUID smallBlindAndButtonPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents()
                .get(3)).getPlayerId();
        table.raise(smallBlindAndButtonPlayerId, 40);

        UUID bigBlindPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents().get(5))
                .getPlayerId();
        table.fold(bigBlindPlayerId);

        List<TableEvent> newEvents = table.fetchNewEvents();

        verifyNumberOfEventsAndEntireOrderByType(9, newEvents, TableCreated,
                CardsShuffled, HandDealtEvent, ActionOnChanged, PlayerRaised,
                ActionOnChanged, LastToActChanged, PlayerFolded, HandCompleted);
        verifyEventIdsAndVersionNumbers(tableId, newEvents);
    }

}
