package com.flexpoker.table.command.aggregate.singlehand.twoplayer

import com.flexpoker.table.command.HandDealerState
import com.flexpoker.table.command.aggregate.TableState
import com.flexpoker.table.command.aggregate.applyEvents
import com.flexpoker.table.command.aggregate.eventproducers.expireActionOn
import com.flexpoker.table.command.aggregate.testhelpers.createBasicTableAndStartHand
import com.flexpoker.table.command.aggregate.testhelpers.fetchIdForBigBlind
import com.flexpoker.table.command.aggregate.testhelpers.fetchIdForButton
import com.flexpoker.table.command.events.ActionOnChangedEvent
import com.flexpoker.table.command.events.CardsShuffledEvent
import com.flexpoker.table.command.events.HandCompletedEvent
import com.flexpoker.table.command.events.HandDealtEvent
import com.flexpoker.table.command.events.PlayerForceFoldedEvent
import com.flexpoker.table.command.events.PotAmountIncreasedEvent
import com.flexpoker.table.command.events.PotCreatedEvent
import com.flexpoker.table.command.events.RoundCompletedEvent
import com.flexpoker.table.command.events.TableCreatedEvent
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.table.command.events.WinnersDeterminedEvent
import com.flexpoker.test.util.CommonAssertions.verifyNewEvents
import com.flexpoker.test.util.EventProducerApplierBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.UUID

class TwoPlayerButtonFoldsDueToTimeoutTest {

    @Test
    fun test() {
        val tableId = UUID.randomUUID()
        val events = createBasicTableAndStartHand(tableId, UUID.randomUUID(), UUID.randomUUID())
        val initState = applyEvents(events)

        val (_, _, handId, playerId) = events[4] as ActionOnChangedEvent

        val (_, newEvents) = EventProducerApplierBuilder<TableState, TableEvent>()
            .initState(initState)
            .andRun { expireActionOn(it, handId, playerId) }
            .run()

        val tableCreatedEvent = events[0] as TableCreatedEvent
        val handDealtEvent = events[2] as HandDealtEvent
        val (_, _, _, _, playersInvolved) = events[3] as PotCreatedEvent
        val (_, _, _, _, amountIncreased) = newEvents[1] as PotAmountIncreasedEvent
        val (_, _, _, _, amountIncreased1) = newEvents[2] as PotAmountIncreasedEvent
        val (_, _, _, nextHandDealerState) = newEvents[3] as RoundCompletedEvent
        val buttonPlayerId = fetchIdForButton(tableCreatedEvent, handDealtEvent)
        val bigBlindPlayerId = fetchIdForBigBlind(tableCreatedEvent, handDealtEvent)

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
        val (_, _, _, playersToShowCards, playersToChipsWonMap) = newEvents[4] as WinnersDeterminedEvent
        assertNull(playersToChipsWonMap[buttonPlayerId])
        assertEquals(30, playersToChipsWonMap[bigBlindPlayerId]!!.toInt())
        assertTrue(playersToShowCards.isEmpty())

        verifyNewEvents(tableId, events + newEvents,
            TableCreatedEvent::class.java, CardsShuffledEvent::class.java,
            HandDealtEvent::class.java, PotCreatedEvent::class.java,
            ActionOnChangedEvent::class.java, PlayerForceFoldedEvent::class.java,
            PotAmountIncreasedEvent::class.java, PotAmountIncreasedEvent::class.java,
            RoundCompletedEvent::class.java, WinnersDeterminedEvent::class.java,
            HandCompletedEvent::class.java
        )
    }

}