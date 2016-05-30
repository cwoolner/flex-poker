package com.flexpoker.table.command.aggregate.singlehand.threeplayer;

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
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.command.events.TableCreatedEvent;

public class ThreePlayerSeatPositionsAndButtonAndBlindsTest {

    @Test
    public void test() {
        UUID tableId = UUID.randomUUID();
        UUID player1Id = UUID.randomUUID();
        UUID player2Id = UUID.randomUUID();
        UUID player3Id = UUID.randomUUID();

        Table table = TableTestUtils.createBasicTableAndStartHand(tableId, player1Id, player2Id,
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

}
