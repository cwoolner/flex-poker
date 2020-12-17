package com.flexpoker.table.command.aggregate.singlehand.twoplayer

import com.flexpoker.table.command.aggregate.HandDealerState
import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils
import com.flexpoker.table.command.events.ActionOnChangedEvent
import com.flexpoker.table.command.events.CardsShuffledEvent
import com.flexpoker.table.command.events.HandCompletedEvent
import com.flexpoker.table.command.events.HandDealtEvent
import com.flexpoker.table.command.events.PlayerForceFoldedEvent
import com.flexpoker.table.command.events.PotAmountIncreasedEvent
import com.flexpoker.table.command.events.PotCreatedEvent
import com.flexpoker.table.command.events.RoundCompletedEvent
import com.flexpoker.table.command.events.TableCreatedEvent
import com.flexpoker.table.command.events.WinnersDeterminedEvent
import com.flexpoker.test.util.CommonAssertions.verifyAppliedAndNewEventsForAggregate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.UUID

class TwoPlayerButtonFoldsDueToTimeoutTest {

    @Test
    fun test() {
        val tableId = UUID.randomUUID()
        val table = TableTestUtils.createBasicTableAndStartHand(tableId, UUID.randomUUID(), UUID.randomUUID())

        // use the info in action on event to simulate the expire
        val (_, _, handId, playerId) = table.fetchNewEvents()[4] as ActionOnChangedEvent
        table.expireActionOn(handId, playerId)
        val newEvents = table.fetchNewEvents()
        val tableCreatedEvent = newEvents[0] as TableCreatedEvent
        val handDealtEvent = newEvents[2] as HandDealtEvent
        val (_, _, _, _, playersInvolved) = newEvents[3] as PotCreatedEvent
        val (_, _, _, _, amountIncreased) = newEvents[6] as PotAmountIncreasedEvent
        val (_, _, _, _, amountIncreased1) = newEvents[7] as PotAmountIncreasedEvent
        val (_, _, _, nextHandDealerState) = newEvents[8] as RoundCompletedEvent
        val buttonPlayerId = TableTestUtils.fetchIdForButton(tableCreatedEvent, handDealtEvent)
        val bigBlindPlayerId = TableTestUtils.fetchIdForBigBlind(tableCreatedEvent, handDealtEvent)

        assertEquals(10, handDealtEvent.callAmountsMap[buttonPlayerId]!!.toInt())
        assertEquals(0, handDealtEvent.callAmountsMap[bigBlindPlayerId]!!.toInt())
        assertEquals(1490, handDealtEvent.chipsInBack[buttonPlayerId]!!.toInt())
        assertEquals(1480, handDealtEvent.chipsInBack[bigBlindPlayerId]!!.toInt())
        assertEquals(10, handDealtEvent.chipsInFrontMap[buttonPlayerId]!!.toInt())
        assertEquals(20, handDealtEvent.chipsInFrontMap[bigBlindPlayerId]!!.toInt())
        assertEquals(HandDealerState.POCKET_CARDS_DEALT, handDealtEvent.handDealerState)
        assertEquals(2, playersInvolved.size)
        assertTrue(playersInvolved.contains(buttonPlayerId))
        assertTrue(playersInvolved.contains(bigBlindPlayerId))
        assertEquals(20, amountIncreased)
        assertEquals(10, amountIncreased1)
        assertEquals(HandDealerState.COMPLETE, nextHandDealerState)
        val (_, _, _, playersToShowCards, playersToChipsWonMap) = newEvents[9] as WinnersDeterminedEvent
        assertNull(playersToChipsWonMap[buttonPlayerId])
        assertEquals(30, playersToChipsWonMap[bigBlindPlayerId]!!.toInt())
        assertTrue(playersToShowCards.isEmpty())
        verifyAppliedAndNewEventsForAggregate(
            table,
            TableCreatedEvent::class.java, CardsShuffledEvent::class.java,
            HandDealtEvent::class.java, PotCreatedEvent::class.java,
            ActionOnChangedEvent::class.java, PlayerForceFoldedEvent::class.java,
            PotAmountIncreasedEvent::class.java, PotAmountIncreasedEvent::class.java,
            RoundCompletedEvent::class.java, WinnersDeterminedEvent::class.java,
            HandCompletedEvent::class.java
        )
    }

}