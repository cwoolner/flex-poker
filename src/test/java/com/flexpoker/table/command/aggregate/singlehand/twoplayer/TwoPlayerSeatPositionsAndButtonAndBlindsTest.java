package com.flexpoker.table.command.aggregate.singlehand.twoplayer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils;
import com.flexpoker.table.command.events.HandDealtEvent;
import com.flexpoker.table.command.events.TableCreatedEvent;

public class TwoPlayerSeatPositionsAndButtonAndBlindsTest {

    @Test
    void test() {
        var tableId = UUID.randomUUID();
        var player1Id = UUID.randomUUID();
        var player2Id = UUID.randomUUID();

        var table = TableTestUtils.createBasicTableAndStartHand(tableId, player1Id, player2Id);

        // check seat positions
        var seatPositionToPlayerIdMap = ((TableCreatedEvent) table.fetchNewEvents().get(0)).getSeatPositionToPlayerMap();
        seatPositionToPlayerIdMap = seatPositionToPlayerIdMap.entrySet().stream()
                .filter(x -> x.getValue() != null)
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

        var player1MatchList = seatPositionToPlayerIdMap.values().stream()
                .filter(x -> x.equals(player1Id)).collect(Collectors.toList());
        var player2MatchList = seatPositionToPlayerIdMap.values().stream()
                .filter(x -> x.equals(player2Id)).collect(Collectors.toList());

        // verify that each player id is only in one seat position and that
        // those are the only two filled-in positions
        assertEquals(1, player1MatchList.size());
        assertEquals(1, player2MatchList.size());

        var numberOfOtherPlayerPositions = seatPositionToPlayerIdMap.values().stream()
                .filter(x -> !x.equals(player1Id)).filter(x -> !x.equals(player2Id))
                .distinct().count();
        assertEquals(0, numberOfOtherPlayerPositions);

        // check blinds
        var player1Position = seatPositionToPlayerIdMap.entrySet().stream()
                .filter(x -> x.getValue().equals(player1Id)).findAny().get().getKey()
                .intValue();
        var player2Position = seatPositionToPlayerIdMap.entrySet().stream()
                .filter(x -> x.getValue().equals(player2Id)).findAny().get().getKey()
                .intValue();

        var handDealtEvent = ((HandDealtEvent) table.fetchNewEvents().get(2));

        if (player1Position == handDealtEvent.getButtonOnPosition()) {
            assertEquals(player1Position, handDealtEvent.getButtonOnPosition());
            assertEquals(player1Position, handDealtEvent.getSmallBlindPosition());
            assertEquals(player2Position, handDealtEvent.getBigBlindPosition());
        } else if (player2Position == handDealtEvent.getButtonOnPosition()) {
            assertEquals(player2Position, handDealtEvent.getButtonOnPosition());
            assertEquals(player2Position, handDealtEvent.getSmallBlindPosition());
            assertEquals(player1Position, handDealtEvent.getBigBlindPosition());
        } else {
            throw new IllegalStateException("for a new two-player hand, one of the players must be the button");
        }
    }

}
