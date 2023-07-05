package com.flexpoker.table.command.aggregate.singlehand.fourplayer

import com.flexpoker.table.command.aggregate.TableState
import com.flexpoker.table.command.aggregate.applyEvents
import com.flexpoker.table.command.aggregate.eventproducers.call
import com.flexpoker.table.command.aggregate.eventproducers.check
import com.flexpoker.table.command.aggregate.testhelpers.blindPlayerIds
import com.flexpoker.table.command.aggregate.testhelpers.createBasicTableAndStartHand
import com.flexpoker.table.command.events.ActionOnChangedEvent
import com.flexpoker.table.command.events.CardsShuffledEvent
import com.flexpoker.table.command.events.FlopCardsDealtEvent
import com.flexpoker.table.command.events.HandCompletedEvent
import com.flexpoker.table.command.events.HandDealtEvent
import com.flexpoker.table.command.events.LastToActChangedEvent
import com.flexpoker.table.command.events.PlayerCalledEvent
import com.flexpoker.table.command.events.PlayerCheckedEvent
import com.flexpoker.table.command.events.PotAmountIncreasedEvent
import com.flexpoker.table.command.events.PotCreatedEvent
import com.flexpoker.table.command.events.RiverCardDealtEvent
import com.flexpoker.table.command.events.RoundCompletedEvent
import com.flexpoker.table.command.events.TableCreatedEvent
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.table.command.events.TurnCardDealtEvent
import com.flexpoker.table.command.events.WinnersDeterminedEvent
import com.flexpoker.test.util.CommonAssertions.verifyNewEvents
import com.flexpoker.test.util.EventProducerApplierBuilder
import org.junit.jupiter.api.Test
import java.util.UUID

class FourPlayerThreeCallsAndChecksUntilTheEndTest {

    @Test
    fun test() {
        val tableId = UUID.randomUUID()
        val player1Id = UUID.randomUUID()
        val player2Id = UUID.randomUUID()
        val player3Id = UUID.randomUUID()
        val player4Id = UUID.randomUUID()
        val events = createBasicTableAndStartHand(tableId, player1Id, player2Id, player3Id, player4Id)
        val initState = applyEvents(events)

        val (buttonOnPlayerId, smallBlindPlayerId, bigBlindPlayerId) = blindPlayerIds(initState)
        val rightOfButtonOnPlayerId = setOf(player1Id, player2Id, player3Id, player4Id)
            .subtract(setOf(buttonOnPlayerId, smallBlindPlayerId, bigBlindPlayerId)).first()


        val (_, newEvents) = EventProducerApplierBuilder<TableState, TableEvent>()
            .initState(initState)
            .andRun { call(it, rightOfButtonOnPlayerId) }
            .andRun { call(it, buttonOnPlayerId) }
            .andRun { call(it, smallBlindPlayerId) }
            .andRun { check(it, bigBlindPlayerId) }
            .andRun { check(it, smallBlindPlayerId) }
            .andRun { check(it, bigBlindPlayerId) }
            .andRun { check(it, rightOfButtonOnPlayerId) }
            .andRun { check(it, buttonOnPlayerId) }
            .andRun { check(it, smallBlindPlayerId) }
            .andRun { check(it, bigBlindPlayerId) }
            .andRun { check(it, rightOfButtonOnPlayerId) }
            .andRun { check(it, buttonOnPlayerId) }
            .andRun { check(it, smallBlindPlayerId) }
            .andRun { check(it, bigBlindPlayerId) }
            .andRun { check(it, rightOfButtonOnPlayerId) }
            .andRun { check(it, buttonOnPlayerId) }
            .run()

        verifyNewEvents(tableId, events + newEvents,
            TableCreatedEvent::class.java,
            CardsShuffledEvent::class.java,
            HandDealtEvent::class.java,
            PotCreatedEvent::class.java,
            ActionOnChangedEvent::class.java,  // pre-flop
            PlayerCalledEvent::class.java, ActionOnChangedEvent::class.java,
            PlayerCalledEvent::class.java, ActionOnChangedEvent::class.java,
            PlayerCalledEvent::class.java, ActionOnChangedEvent::class.java,
            PlayerCheckedEvent::class.java,
            PotAmountIncreasedEvent::class.java,
            RoundCompletedEvent::class.java,
            ActionOnChangedEvent::class.java,
            LastToActChangedEvent::class.java,
            FlopCardsDealtEvent::class.java,  // post-flop
            PlayerCheckedEvent::class.java, ActionOnChangedEvent::class.java,
            PlayerCheckedEvent::class.java, ActionOnChangedEvent::class.java,
            PlayerCheckedEvent::class.java, ActionOnChangedEvent::class.java,
            PlayerCheckedEvent::class.java, RoundCompletedEvent::class.java,
            ActionOnChangedEvent::class.java,
            LastToActChangedEvent::class.java,
            TurnCardDealtEvent::class.java,  // post-turn
            PlayerCheckedEvent::class.java, ActionOnChangedEvent::class.java,
            PlayerCheckedEvent::class.java, ActionOnChangedEvent::class.java,
            PlayerCheckedEvent::class.java, ActionOnChangedEvent::class.java,
            PlayerCheckedEvent::class.java, RoundCompletedEvent::class.java,
            ActionOnChangedEvent::class.java, LastToActChangedEvent::class.java,
            RiverCardDealtEvent::class.java,  // post-river
            PlayerCheckedEvent::class.java, ActionOnChangedEvent::class.java,
            PlayerCheckedEvent::class.java, ActionOnChangedEvent::class.java,
            PlayerCheckedEvent::class.java, ActionOnChangedEvent::class.java,
            PlayerCheckedEvent::class.java, RoundCompletedEvent::class.java,
            WinnersDeterminedEvent::class.java, HandCompletedEvent::class.java
        )
    }

}