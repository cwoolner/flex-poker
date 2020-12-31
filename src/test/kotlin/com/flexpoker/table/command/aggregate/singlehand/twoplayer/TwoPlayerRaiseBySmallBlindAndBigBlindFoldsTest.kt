package com.flexpoker.table.command.aggregate.singlehand.twoplayer

import com.flexpoker.table.command.aggregate.applyEvents
import com.flexpoker.table.command.aggregate.eventproducers.fold
import com.flexpoker.table.command.aggregate.eventproducers.raise
import com.flexpoker.table.command.aggregate.testhelpers.blindPlayerIds
import com.flexpoker.table.command.aggregate.testhelpers.createBasicTableAndStartHand
import com.flexpoker.table.command.events.ActionOnChangedEvent
import com.flexpoker.table.command.events.CardsShuffledEvent
import com.flexpoker.table.command.events.HandCompletedEvent
import com.flexpoker.table.command.events.HandDealtEvent
import com.flexpoker.table.command.events.LastToActChangedEvent
import com.flexpoker.table.command.events.PlayerFoldedEvent
import com.flexpoker.table.command.events.PlayerRaisedEvent
import com.flexpoker.table.command.events.PotAmountIncreasedEvent
import com.flexpoker.table.command.events.PotCreatedEvent
import com.flexpoker.table.command.events.RoundCompletedEvent
import com.flexpoker.table.command.events.TableCreatedEvent
import com.flexpoker.table.command.events.WinnersDeterminedEvent
import com.flexpoker.test.util.CommonAssertions.verifyNewEvents
import com.flexpoker.test.util.TableEventProducerApplierBuilder
import org.junit.jupiter.api.Test
import java.util.UUID

class TwoPlayerRaiseBySmallBlindAndBigBlindFoldsTest {

    @Test
    fun test() {
        val tableId = UUID.randomUUID()
        val events = createBasicTableAndStartHand(tableId, UUID.randomUUID(), UUID.randomUUID())
        val initState = applyEvents(events)

        val (buttonOnPlayerId, smallBlindPlayerId, bigBlindPlayerId) = blindPlayerIds(initState)

        val (_, newEvents) = TableEventProducerApplierBuilder()
            .initState(initState)
            .andRun { raise(it, smallBlindPlayerId, 40) }
            .andRun { fold(it, bigBlindPlayerId) }
            .run()

        verifyNewEvents(tableId, events + newEvents,
            TableCreatedEvent::class.java, CardsShuffledEvent::class.java,
            HandDealtEvent::class.java, PotCreatedEvent::class.java,
            ActionOnChangedEvent::class.java, PlayerRaisedEvent::class.java,
            ActionOnChangedEvent::class.java, LastToActChangedEvent::class.java,
            PlayerFoldedEvent::class.java, PotAmountIncreasedEvent::class.java,
            PotAmountIncreasedEvent::class.java, RoundCompletedEvent::class.java,
            WinnersDeterminedEvent::class.java, HandCompletedEvent::class.java
        )
    }

}