package com.flexpoker.table.command.aggregate.basicevent;

import static com.flexpoker.table.command.framework.TableEventType.ActionOnChanged;
import static com.flexpoker.table.command.framework.TableEventType.CardsShuffled;
import static com.flexpoker.table.command.framework.TableEventType.FlopCardsDealt;
import static com.flexpoker.table.command.framework.TableEventType.HandCompleted;
import static com.flexpoker.table.command.framework.TableEventType.HandDealtEvent;
import static com.flexpoker.table.command.framework.TableEventType.LastToActChanged;
import static com.flexpoker.table.command.framework.TableEventType.PlayerCalled;
import static com.flexpoker.table.command.framework.TableEventType.PlayerChecked;
import static com.flexpoker.table.command.framework.TableEventType.PlayerFolded;
import static com.flexpoker.table.command.framework.TableEventType.PotAmountIncreased;
import static com.flexpoker.table.command.framework.TableEventType.PotCreated;
import static com.flexpoker.table.command.framework.TableEventType.RiverCardDealt;
import static com.flexpoker.table.command.framework.TableEventType.RoundCompleted;
import static com.flexpoker.table.command.framework.TableEventType.TableCreated;
import static com.flexpoker.table.command.framework.TableEventType.TurnCardDealt;
import static com.flexpoker.table.command.framework.TableEventType.WinnersDetermined;
import static com.flexpoker.test.util.CommonAssertions.verifyEventIdsAndVersionNumbers;
import static com.flexpoker.test.util.CommonAssertions.verifyNumberOfEventsAndEntireOrderByType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Test;

import com.flexpoker.table.command.aggregate.Table;
import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.table.command.framework.TableEvent;

public class ThreePersonTableTest {

    @Test
    public void testSeatPositionsAndButtonAndBlinds() {
        UUID tableId = UUID.randomUUID();
        UUID player1Id = UUID.randomUUID();
        UUID player2Id = UUID.randomUUID();
        UUID player3Id = UUID.randomUUID();

        Table table = TableTestUtils.createBasicTable(tableId, player1Id, player2Id,
                player3Id);

        // check seat positions
        Map<Integer, UUID> seatPositionToPlayerIdMap = ((TableCreatedEvent) table
                .fetchNewEvents().get(0)).getSeatPositionToPlayerMap();
        seatPositionToPlayerIdMap = seatPositionToPlayerIdMap.entrySet()
                .stream().filter(x -> x.getValue() != null)
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

        List<UUID> player1MatchList = seatPositionToPlayerIdMap.values().stream()
                .filter(x -> x.equals(player1Id)).collect(Collectors.toList());
        List<UUID> player2MatchList = seatPositionToPlayerIdMap.values().stream()
                .filter(x -> x.equals(player2Id)).collect(Collectors.toList());
        List<UUID> player3MatchList = seatPositionToPlayerIdMap.values().stream()
                .filter(x -> x.equals(player3Id)).collect(Collectors.toList());

        // verify that each player id is only in one seat position and that
        // those are the only three filled-in positions
        assertEquals(1, player1MatchList.size());
        assertEquals(1, player2MatchList.size());
        assertEquals(1, player3MatchList.size());

        long numberOfOtherPlayerPositions = seatPositionToPlayerIdMap.values().stream()
                .filter(x -> !x.equals(player1Id)).filter(x -> !x.equals(player2Id))
                .filter(x -> !x.equals(player3Id)).distinct().count();
        assertEquals(0, numberOfOtherPlayerPositions);

        // check blinds
        int player1Position = seatPositionToPlayerIdMap.entrySet().stream()
                .filter(x -> x.getValue().equals(player1Id)).findAny().get().getKey()
                .intValue();
        int player2Position = seatPositionToPlayerIdMap.entrySet().stream()
                .filter(x -> x.getValue().equals(player2Id)).findAny().get().getKey()
                .intValue();
        int player3Position = seatPositionToPlayerIdMap.entrySet().stream()
                .filter(x -> x.getValue().equals(player3Id)).findAny().get().getKey()
                .intValue();

        HandDealtEvent handDealtEvent = ((HandDealtEvent) table.fetchNewEvents().get(2));

        assertFalse(handDealtEvent.getButtonOnPosition() == handDealtEvent
                .getSmallBlindPosition());
        assertFalse(handDealtEvent.getSmallBlindPosition() == handDealtEvent
                .getBigBlindPosition());
        assertFalse(handDealtEvent.getButtonOnPosition() == handDealtEvent
                .getBigBlindPosition());

        Set<Integer> buttonAndBlindPositions = new HashSet<>();
        buttonAndBlindPositions
                .add(Integer.valueOf(handDealtEvent.getButtonOnPosition()));
        buttonAndBlindPositions.add(Integer.valueOf(handDealtEvent
                .getSmallBlindPosition()));
        buttonAndBlindPositions
                .add(Integer.valueOf(handDealtEvent.getBigBlindPosition()));

        assertTrue(buttonAndBlindPositions.contains(Integer.valueOf(player1Position)));
        assertTrue(buttonAndBlindPositions.contains(Integer.valueOf(player2Position)));
        assertTrue(buttonAndBlindPositions.contains(Integer.valueOf(player3Position)));
    }

    @Test
    public void testTwoTimeoutsCauseFolds() {
        UUID tableId = UUID.randomUUID();
        UUID player1Id = UUID.randomUUID();
        UUID player2Id = UUID.randomUUID();
        UUID player3Id = UUID.randomUUID();

        Table table = TableTestUtils.createBasicTable(tableId, player1Id, player2Id,
                player3Id);

        ActionOnChangedEvent buttonActionOnChangedEvent = (ActionOnChangedEvent) table
                .fetchNewEvents().get(3);
        table.expireActionOn(buttonActionOnChangedEvent.getHandId(),
                buttonActionOnChangedEvent.getPlayerId());

        ActionOnChangedEvent smallBlindActionOnChangedEvent = (ActionOnChangedEvent) table
                .fetchNewEvents().get(5);
        table.expireActionOn(smallBlindActionOnChangedEvent.getHandId(),
                smallBlindActionOnChangedEvent.getPlayerId());

        List<TableEvent> newEvents = table.fetchNewEvents();

        verifyNumberOfEventsAndEntireOrderByType(newEvents, TableCreated, CardsShuffled,
                HandDealtEvent, ActionOnChanged, PlayerFolded, ActionOnChanged,
                PlayerFolded, PotCreated, PotAmountIncreased, PotAmountIncreased,
                RoundCompleted, WinnersDetermined, HandCompleted);
        verifyEventIdsAndVersionNumbers(tableId, newEvents);
    }

    @Test
    public void testTwoCallsAndCheckingTheRestOfTheWay() {
        UUID tableId = UUID.randomUUID();
        UUID player1Id = UUID.randomUUID();
        UUID player2Id = UUID.randomUUID();
        UUID player3Id = UUID.randomUUID();

        Table table = TableTestUtils.createBasicTable(tableId, player1Id, player2Id,
                player3Id);

        UUID buttonOnPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents().get(3))
                .getPlayerId();
        table.call(buttonOnPlayerId);

        UUID smallBlindPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents().get(5))
                .getPlayerId();
        table.call(smallBlindPlayerId);

        UUID bigBlindPlayerId = ((ActionOnChangedEvent) table.fetchNewEvents().get(7))
                .getPlayerId();
        table.check(bigBlindPlayerId);

        // post-flop
        table.check(smallBlindPlayerId);
        table.check(bigBlindPlayerId);
        table.check(buttonOnPlayerId);

        // post-turn
        table.check(smallBlindPlayerId);
        table.check(bigBlindPlayerId);
        table.check(buttonOnPlayerId);

        // post-river
        table.check(smallBlindPlayerId);
        table.check(bigBlindPlayerId);
        table.check(buttonOnPlayerId);

        List<TableEvent> newEvents = table.fetchNewEvents();

        verifyNumberOfEventsAndEntireOrderByType(
                newEvents,
                TableCreated,
                CardsShuffled,
                HandDealtEvent,
                ActionOnChanged,
                // pre-flop
                PlayerCalled, ActionOnChanged, PlayerCalled, ActionOnChanged,
                PlayerChecked, PotCreated, PotAmountIncreased,
                PotAmountIncreased,
                RoundCompleted,
                ActionOnChanged,
                LastToActChanged,
                FlopCardsDealt,
                // post-flop
                PlayerChecked, ActionOnChanged, PlayerChecked, ActionOnChanged,
                PlayerChecked, RoundCompleted,
                ActionOnChanged,
                LastToActChanged,
                TurnCardDealt,
                // post-turn
                PlayerChecked, ActionOnChanged, PlayerChecked, ActionOnChanged,
                PlayerChecked, RoundCompleted, ActionOnChanged, LastToActChanged,
                RiverCardDealt,
                // post-river
                PlayerChecked, ActionOnChanged, PlayerChecked, ActionOnChanged,
                PlayerChecked, RoundCompleted, WinnersDetermined, HandCompleted);
        verifyEventIdsAndVersionNumbers(tableId, newEvents);
    }
}
