package com.flexpoker.table.command.aggregate.singlehand.threeplayer

import com.flexpoker.table.command.aggregate.testhelpers.createBasicTableAndStartHand
import com.flexpoker.table.command.events.HandDealtEvent
import com.flexpoker.table.command.events.TableCreatedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.HashSet
import java.util.UUID

class ThreePlayerSeatPositionsAndButtonAndBlindsTest {

    @Test
    fun test() {
        val tableId = UUID.randomUUID()
        val player1Id = UUID.randomUUID()
        val player2Id = UUID.randomUUID()
        val player3Id = UUID.randomUUID()
        val events = createBasicTableAndStartHand(tableId, player1Id, player2Id, player3Id)

        // check seat positions
        val seatPositionToPlayerIdMap = (events[0] as TableCreatedEvent).seatPositionToPlayerMap.filter { it.value != null }
        val player1MatchList = seatPositionToPlayerIdMap.values.filter { it == player1Id }
        val player2MatchList = seatPositionToPlayerIdMap.values.filter { it == player2Id }
        val player3MatchList = seatPositionToPlayerIdMap.values.filter { it == player3Id }

        // verify that each player id is only in one seat position and that
        // those are the only three filled-in positions
        assertEquals(1, player1MatchList.size)
        assertEquals(1, player2MatchList.size)
        assertEquals(1, player3MatchList.size)
        val numberOfOtherPlayerPositions = seatPositionToPlayerIdMap.values
            .filter { it != player1Id }
            .filter { it != player2Id }
            .filter { it != player3Id }
            .distinct().count()
        assertEquals(0, numberOfOtherPlayerPositions)

        // check blinds
        val player1Position = seatPositionToPlayerIdMap.entries.first { it.value == player1Id }.key
        val player2Position = seatPositionToPlayerIdMap.entries.first { it.value == player2Id }.key
        val player3Position = seatPositionToPlayerIdMap.entries.first { it.value == player3Id }.key
        val (_, _, _, _, _, _, buttonOnPosition, smallBlindPosition, bigBlindPosition) = events[2] as HandDealtEvent
        assertFalse(buttonOnPosition == smallBlindPosition)
        assertFalse(smallBlindPosition == bigBlindPosition)
        assertFalse(buttonOnPosition == bigBlindPosition)
        val buttonAndBlindPositions = HashSet<Any>()
        buttonAndBlindPositions.add(Integer.valueOf(buttonOnPosition))
        buttonAndBlindPositions.add(Integer.valueOf(smallBlindPosition))
        buttonAndBlindPositions.add(Integer.valueOf(bigBlindPosition))
        assertTrue(buttonAndBlindPositions.contains(player1Position))
        assertTrue(buttonAndBlindPositions.contains(player2Position))
        assertTrue(buttonAndBlindPositions.contains(player3Position))
    }

}