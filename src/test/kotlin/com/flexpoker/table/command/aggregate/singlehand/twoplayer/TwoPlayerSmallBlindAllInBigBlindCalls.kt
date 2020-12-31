package com.flexpoker.table.command.aggregate.singlehand.twoplayer

import com.flexpoker.table.command.aggregate.applyEvents
import com.flexpoker.table.command.aggregate.eventproducers.autoMoveHandForward
import com.flexpoker.table.command.aggregate.eventproducers.call
import com.flexpoker.table.command.aggregate.eventproducers.raise
import com.flexpoker.table.command.aggregate.testhelpers.blindPlayerIds
import com.flexpoker.table.command.aggregate.testhelpers.createBasicTableAndStartHand
import com.flexpoker.table.command.events.ActionOnChangedEvent
import com.flexpoker.table.command.events.AutoMoveHandForwardEvent
import com.flexpoker.table.command.events.CardsShuffledEvent
import com.flexpoker.table.command.events.FlopCardsDealtEvent
import com.flexpoker.table.command.events.HandCompletedEvent
import com.flexpoker.table.command.events.HandDealtEvent
import com.flexpoker.table.command.events.LastToActChangedEvent
import com.flexpoker.table.command.events.PlayerCalledEvent
import com.flexpoker.table.command.events.PlayerRaisedEvent
import com.flexpoker.table.command.events.PotAmountIncreasedEvent
import com.flexpoker.table.command.events.PotClosedEvent
import com.flexpoker.table.command.events.PotCreatedEvent
import com.flexpoker.table.command.events.RiverCardDealtEvent
import com.flexpoker.table.command.events.RoundCompletedEvent
import com.flexpoker.table.command.events.TableCreatedEvent
import com.flexpoker.table.command.events.TurnCardDealtEvent
import com.flexpoker.table.command.events.WinnersDeterminedEvent
import com.flexpoker.test.util.CommonAssertions.verifyNewEvents
import com.flexpoker.test.util.TableEventProducerApplierBuilder
import org.junit.jupiter.api.Test
import java.util.UUID

class TwoPlayerSmallBlindAllInBigBlindCalls {

    @Test
    fun test() {
        val tableId = UUID.randomUUID()
        val events = createBasicTableAndStartHand(tableId, UUID.randomUUID(), UUID.randomUUID())
        val initState = applyEvents(events)

        val (_, smallBlindPlayerId, bigBlindPlayerId) = blindPlayerIds(initState)

        val (_, newEvents) = TableEventProducerApplierBuilder()
            .initState(initState)
            .andRun { raise(it, smallBlindPlayerId, 1500) }
            .andRun { call(it, bigBlindPlayerId) }
            .andRun { autoMoveHandForward(it) }
            .andRun { autoMoveHandForward(it) }
            .andRun { autoMoveHandForward(it) }
            .run()

        verifyNewEvents(tableId, events + newEvents,
            TableCreatedEvent::class.java,
            CardsShuffledEvent::class.java,
            HandDealtEvent::class.java,
            PotCreatedEvent::class.java,
            ActionOnChangedEvent::class.java,  // pre-flop
            PlayerRaisedEvent::class.java,
            ActionOnChangedEvent::class.java,
            LastToActChangedEvent::class.java,
            PlayerCalledEvent::class.java,
            PotAmountIncreasedEvent::class.java,
            PotClosedEvent::class.java,
            RoundCompletedEvent::class.java,
            FlopCardsDealtEvent::class.java,
            AutoMoveHandForwardEvent::class.java,
            RoundCompletedEvent::class.java,  // post-flop
            TurnCardDealtEvent::class.java,
            AutoMoveHandForwardEvent::class.java,
            RoundCompletedEvent::class.java,  // post-turn
            RiverCardDealtEvent::class.java,
            AutoMoveHandForwardEvent::class.java,
            RoundCompletedEvent::class.java,  // post-river
            WinnersDeterminedEvent::class.java,
            HandCompletedEvent::class.java
        )
    }
}