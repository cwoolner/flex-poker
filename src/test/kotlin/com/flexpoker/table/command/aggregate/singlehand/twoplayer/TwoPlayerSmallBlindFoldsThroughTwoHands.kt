package com.flexpoker.table.command.aggregate.singlehand.twoplayer

import com.flexpoker.table.command.aggregate.TableState
import com.flexpoker.table.command.aggregate.applyEvents
import com.flexpoker.table.command.aggregate.eventproducers.fold
import com.flexpoker.table.command.aggregate.testhelpers.blindPlayerIds
import com.flexpoker.table.command.aggregate.testhelpers.createBasicTableAndStartHand
import com.flexpoker.table.command.aggregate.testhelpers.startHandAtExistingTable
import com.flexpoker.table.command.events.ActionOnChangedEvent
import com.flexpoker.table.command.events.CardsShuffledEvent
import com.flexpoker.table.command.events.HandCompletedEvent
import com.flexpoker.table.command.events.HandDealtEvent
import com.flexpoker.table.command.events.PlayerFoldedEvent
import com.flexpoker.table.command.events.PotAmountIncreasedEvent
import com.flexpoker.table.command.events.PotCreatedEvent
import com.flexpoker.table.command.events.RoundCompletedEvent
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.table.command.events.WinnersDeterminedEvent
import com.flexpoker.test.util.CommonAssertions.verifyNewEvents
import com.flexpoker.test.util.EventProducerApplierBuilder
import org.junit.jupiter.api.Test
import java.util.UUID

class TwoPlayerSmallBlindFoldsThroughTwoHands {

    @Test
    fun test() {
        val tableId = UUID.randomUUID()
        val events = createBasicTableAndStartHand(tableId, UUID.randomUUID(), UUID.randomUUID())
        val initState = applyEvents(events)

        val (_, smallBlindPlayerId, bigBlindPlayerId) = blindPlayerIds(initState)

        val (firstHandState, firstHandEvents) = EventProducerApplierBuilder<TableState, TableEvent>()
            .initState(initState)
            .andRun { fold(it, smallBlindPlayerId) }
            .run()

        verifyNewEvents(tableId, firstHandEvents,
            PlayerFoldedEvent::class.java, PotAmountIncreasedEvent::class.java,
            PotAmountIncreasedEvent::class.java, RoundCompletedEvent::class.java,
            WinnersDeterminedEvent::class.java, HandCompletedEvent::class.java
        )

        val (secondHandState, secondHandEvents) = EventProducerApplierBuilder<TableState, TableEvent>()
            .initState(firstHandState)
            .andRun { startHandAtExistingTable(it) }
            .andRun { fold(it, bigBlindPlayerId) }
            .run()

        println(secondHandEvents)

        verifyNewEvents(tableId, secondHandEvents,
            CardsShuffledEvent::class.java, HandDealtEvent::class.java,
            PotCreatedEvent::class.java, ActionOnChangedEvent::class.java,
            PlayerFoldedEvent::class.java, PotAmountIncreasedEvent::class.java,
            PotAmountIncreasedEvent::class.java, RoundCompletedEvent::class.java,
            WinnersDeterminedEvent::class.java, HandCompletedEvent::class.java)
    }
}