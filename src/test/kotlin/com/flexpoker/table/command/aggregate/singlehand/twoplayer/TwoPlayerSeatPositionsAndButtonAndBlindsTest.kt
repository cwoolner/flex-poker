package com.flexpoker.table.command.aggregate.singlehand.twoplayer

import com.flexpoker.table.command.aggregate.testhelpers.createBasicTableAndStartHand
import com.flexpoker.table.command.events.HandDealtEvent
import com.flexpoker.table.command.events.TableCreatedEvent
import com.flexpoker.test.util.UnitTestClass
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.UUID

@UnitTestClass
class TwoPlayerSeatPositionsAndButtonAndBlindsTest {

    @Test
    fun test() {
        val tableId = UUID.randomUUID()
        val player1Id = UUID.randomUUID()
        val player2Id = UUID.randomUUID()
        val events = createBasicTableAndStartHand(tableId, player1Id, player2Id)

        // check seat positions
        val seatPositionToPlayerIdMap = (events[0] as TableCreatedEvent)
            .seatPositionToPlayerMap.filter { it.value != null }
        val player1MatchList = seatPositionToPlayerIdMap.values.filter { it == player1Id }
        val player2MatchList = seatPositionToPlayerIdMap.values.filter { it == player2Id }

        // verify that each player id is only in one seat position and that
        // those are the only two filled-in positions
        assertEquals(1, player1MatchList.size)
        assertEquals(1, player2MatchList.size)
        val numberOfOtherPlayerPositions = seatPositionToPlayerIdMap.values
            .filter { it != player1Id }.filter { it != player2Id }.distinct().count()
        assertEquals(0, numberOfOtherPlayerPositions)

        // check blinds
        val player1Position = seatPositionToPlayerIdMap.entries.first { it.value == player1Id }.key
        val player2Position = seatPositionToPlayerIdMap.entries.first { it.value == player2Id }.key
        val (_, _, _, _, _, _, buttonOnPosition, smallBlindPosition, bigBlindPosition) = events[2] as HandDealtEvent
        when {
            player1Position == buttonOnPosition -> {
                assertEquals(player1Position, buttonOnPosition)
                assertEquals(player1Position, smallBlindPosition)
                assertEquals(player2Position, bigBlindPosition)
            }
            player2Position == buttonOnPosition -> {
                assertEquals(player2Position, buttonOnPosition)
                assertEquals(player2Position, smallBlindPosition)
                assertEquals(player1Position, bigBlindPosition)
            }
            else -> {
                throw IllegalStateException("for a new two-player hand, one of the players must be the button")
            }
        }
    }

}