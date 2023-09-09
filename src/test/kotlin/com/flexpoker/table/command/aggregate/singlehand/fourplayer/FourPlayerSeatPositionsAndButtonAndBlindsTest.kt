package com.flexpoker.table.command.aggregate.singlehand.fourplayer

import com.flexpoker.table.command.aggregate.testhelpers.createBasicTableAndStartHand
import com.flexpoker.table.command.events.HandDealtEvent
import com.flexpoker.table.command.events.TableCreatedEvent
import com.flexpoker.test.util.UnitTestClass
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.util.UUID
import java.util.stream.Collectors

@UnitTestClass
class FourPlayerSeatPositionsAndButtonAndBlindsTest {

    @Test
    fun test() {
        val tableId = UUID.randomUUID()
        val player1Id = UUID.randomUUID()
        val player2Id = UUID.randomUUID()
        val player3Id = UUID.randomUUID()
        val player4Id = UUID.randomUUID()
        val events = createBasicTableAndStartHand(tableId, player1Id, player2Id, player3Id, player4Id)

        // check seat positions
        val seatPositionToPlayerIdMap = (events[0] as TableCreatedEvent)
            .seatPositionToPlayerMap.filter { it.value != null }
        val player1MatchList = seatPositionToPlayerIdMap.values.filter { it == player1Id }
        val player2MatchList = seatPositionToPlayerIdMap.values.filter { it == player2Id }
        val player3MatchList = seatPositionToPlayerIdMap.values.filter { it == player3Id }
        val player4MatchList = seatPositionToPlayerIdMap.values.filter { it == player4Id }

        // verify that each player id is only in one seat position and that
        // those are the only two filled-in positions
        assertEquals(1, player1MatchList.size)
        assertEquals(1, player2MatchList.size)
        assertEquals(1, player3MatchList.size)
        assertEquals(1, player4MatchList.size)
        val numberOfOtherPlayerPositions = seatPositionToPlayerIdMap.values.stream()
            .filter { x: UUID? -> x != player1Id }.filter { x: UUID? -> x != player2Id }
            .filter { x: UUID? -> x != player3Id }.filter { x: UUID? -> x != player4Id }
            .distinct().count()
        assertEquals(0, numberOfOtherPlayerPositions)
        val (_, _, _, _, _, _, buttonOnPosition, smallBlindPosition, bigBlindPosition) = events[2] as HandDealtEvent
        assertFalse(buttonOnPosition == smallBlindPosition)
        assertFalse(smallBlindPosition == bigBlindPosition)
        assertFalse(buttonOnPosition == bigBlindPosition)
        val buttonAndBlindPositions = HashSet<Any?>()
        buttonAndBlindPositions.add(Integer.valueOf(buttonOnPosition))
        buttonAndBlindPositions.add(Integer.valueOf(smallBlindPosition))
        buttonAndBlindPositions.add(Integer.valueOf(bigBlindPosition))
        val seatPositionsWithAPlayer = seatPositionToPlayerIdMap.entries.stream()
            .filter { x: Map.Entry<Int?, UUID?> -> x.value != null }.map { x: Map.Entry<Int?, UUID?> -> x.key }
            .collect(Collectors.toSet())
        val numberOfPlayersThatAreNotABlindOrButton = seatPositionsWithAPlayer.stream()
            .filter { x: Int? -> !buttonAndBlindPositions.contains(x) }.distinct().count()
        assertEquals(1, numberOfPlayersThatAreNotABlindOrButton)
    }

}