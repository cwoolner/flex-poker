package com.flexpoker.table.command.aggregate.singlehand.twoplayer

import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils
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
import com.flexpoker.test.util.CommonAssertions.verifyAppliedAndNewEventsForAggregate
import org.junit.jupiter.api.Test
import java.util.UUID

class TwoPlayerSmallBlindAllInBigBlindCalls {

    @Test
    fun test() {
        val tableId = UUID.randomUUID()
        val table = TableTestUtils.createBasicTableAndStartHand(tableId, UUID.randomUUID(), UUID.randomUUID())

        // use the info in action on event to get the player id of the small blind
        val smallBlindAndButtonPlayerId = (table.fetchNewEvents()[4] as ActionOnChangedEvent).playerId
        table.raise(smallBlindAndButtonPlayerId, 1500)
        val bigBlindPlayerId = (table.fetchNewEvents()[6] as ActionOnChangedEvent).playerId
        table.call(bigBlindPlayerId)
        val handId = (table.fetchNewEvents()[2] as HandDealtEvent).handId
        table.autoMoveHandForward()
        table.autoMoveHandForward()
        table.autoMoveHandForward()
        verifyAppliedAndNewEventsForAggregate(
            table,
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