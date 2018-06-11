package com.flexpoker.table.command.aggregate.singlehand.fourplayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils;
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.command.events.TableCreatedEvent;

public class FourPlayerSeatPositionsAndButtonAndBlindsTest {

    @Test
    public void test() {
        var tableId = UUID.randomUUID();
        var player1Id = UUID.randomUUID();
        var player2Id = UUID.randomUUID();
        var player3Id = UUID.randomUUID();
        var player4Id = UUID.randomUUID();

        var table = TableTestUtils.createBasicTableAndStartHand(tableId, player1Id, player2Id, player3Id, player4Id);

        // check seat positions
        var seatPositionToPlayerIdMap = ((TableCreatedEvent) table.fetchNewEvents().get(0)).getSeatPositionToPlayerMap();
        seatPositionToPlayerIdMap = seatPositionToPlayerIdMap.entrySet().stream()
                .filter(x -> x.getValue() != null)
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

        var player1MatchList = seatPositionToPlayerIdMap.values().stream()
                .filter(x -> x.equals(player1Id)).collect(Collectors.toList());
        var player2MatchList = seatPositionToPlayerIdMap.values().stream()
                .filter(x -> x.equals(player2Id)).collect(Collectors.toList());
        var player3MatchList = seatPositionToPlayerIdMap.values().stream()
                .filter(x -> x.equals(player3Id)).collect(Collectors.toList());
        var player4MatchList = seatPositionToPlayerIdMap.values().stream()
                .filter(x -> x.equals(player4Id)).collect(Collectors.toList());

        // verify that each player id is only in one seat position and that
        // those are the only two filled-in positions
        assertEquals(1, player1MatchList.size());
        assertEquals(1, player2MatchList.size());
        assertEquals(1, player3MatchList.size());
        assertEquals(1, player4MatchList.size());

        var numberOfOtherPlayerPositions = seatPositionToPlayerIdMap.values().stream()
                .filter(x -> !x.equals(player1Id)).filter(x -> !x.equals(player2Id))
                .filter(x -> !x.equals(player3Id)).filter(x -> !x.equals(player4Id))
                .distinct().count();
        assertEquals(0, numberOfOtherPlayerPositions);

        var handDealtEvent = ((HandDealtEvent) table.fetchNewEvents().get(2));

        assertFalse(handDealtEvent.getButtonOnPosition() == handDealtEvent.getSmallBlindPosition());
        assertFalse(handDealtEvent.getSmallBlindPosition() == handDealtEvent.getBigBlindPosition());
        assertFalse(handDealtEvent.getButtonOnPosition() == handDealtEvent.getBigBlindPosition());

        var buttonAndBlindPositions = new HashSet<>();
        buttonAndBlindPositions.add(Integer.valueOf(handDealtEvent.getButtonOnPosition()));
        buttonAndBlindPositions.add(Integer.valueOf(handDealtEvent.getSmallBlindPosition()));
        buttonAndBlindPositions.add(Integer.valueOf(handDealtEvent.getBigBlindPosition()));

        var seatPositionsWithAPlayer = seatPositionToPlayerIdMap.entrySet().stream()
                .filter(x -> x.getValue() != null).map(x -> x.getKey())
                .collect(Collectors.toSet());

        var numberOfPlayersThatAreNotABlindOrButton = seatPositionsWithAPlayer.stream()
                .filter(x -> !buttonAndBlindPositions.contains(x)).distinct().count();

        assertEquals(1, numberOfPlayersThatAreNotABlindOrButton);
    }

}
