package com.flexpoker.table.command.aggregate.singlehand.twoplayer

import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils
import com.flexpoker.table.command.events.ActionOnChangedEvent
import com.flexpoker.table.command.events.CardsShuffledEvent
import com.flexpoker.table.command.events.FlopCardsDealtEvent
import com.flexpoker.table.command.events.HandCompletedEvent
import com.flexpoker.table.command.events.HandDealtEvent
import com.flexpoker.table.command.events.LastToActChangedEvent
import com.flexpoker.table.command.events.PlayerCalledEvent
import com.flexpoker.table.command.events.PlayerCheckedEvent
import com.flexpoker.table.command.events.PlayerForceCheckedEvent
import com.flexpoker.table.command.events.PotAmountIncreasedEvent
import com.flexpoker.table.command.events.PotCreatedEvent
import com.flexpoker.table.command.events.RiverCardDealtEvent
import com.flexpoker.table.command.events.RoundCompletedEvent
import com.flexpoker.table.command.events.TableCreatedEvent
import com.flexpoker.table.command.events.TurnCardDealtEvent
import com.flexpoker.table.command.events.WinnersDeterminedEvent
import com.flexpoker.test.util.CommonAssertions.verifyAppliedAndNewEventsForAggregate
import org.junit.jupiter.api.Test
import java.util.UUID

class TwoPlayerSmallBlindCallAndBigBlindCheckDueToTimeoutTest {

    @Test
    fun test() {
        val tableId = UUID.randomUUID()
        val table = TableTestUtils.createBasicTableAndStartHand(tableId, UUID.randomUUID(), UUID.randomUUID())

        // use the info in action on event to simulate the expire
        val smallBlindAndButtonPlayerId = (table.fetchNewEvents()[4] as ActionOnChangedEvent).playerId
        table.call(smallBlindAndButtonPlayerId)
        val (_, _, handId, bigBlindPlayerId) = table.fetchNewEvents()[6] as ActionOnChangedEvent
        table.expireActionOn(handId, bigBlindPlayerId)

        // post-flop
        table.check(bigBlindPlayerId)
        table.check(smallBlindAndButtonPlayerId)

        // post-turn
        table.check(bigBlindPlayerId)
        table.check(smallBlindAndButtonPlayerId)

        // post-river
        table.check(bigBlindPlayerId)
        table.check(smallBlindAndButtonPlayerId)
        verifyAppliedAndNewEventsForAggregate(
            table,
            TableCreatedEvent::class.java,
            CardsShuffledEvent::class.java,
            HandDealtEvent::class.java,
            PotCreatedEvent::class.java,
            ActionOnChangedEvent::class.java,  // pre-flop
            PlayerCalledEvent::class.java, ActionOnChangedEvent::class.java,
            PlayerForceCheckedEvent::class.java,
            PotAmountIncreasedEvent::class.java,
            RoundCompletedEvent::class.java, ActionOnChangedEvent::class.java,
            LastToActChangedEvent::class.java, FlopCardsDealtEvent::class.java,  // post-flop
            PlayerCheckedEvent::class.java, ActionOnChangedEvent::class.java,
            PlayerCheckedEvent::class.java, RoundCompletedEvent::class.java,
            ActionOnChangedEvent::class.java, LastToActChangedEvent::class.java,
            TurnCardDealtEvent::class.java,  // post-turn
            PlayerCheckedEvent::class.java, ActionOnChangedEvent::class.java,
            PlayerCheckedEvent::class.java, RoundCompletedEvent::class.java,
            ActionOnChangedEvent::class.java, LastToActChangedEvent::class.java,
            RiverCardDealtEvent::class.java,  // post-river
            PlayerCheckedEvent::class.java, ActionOnChangedEvent::class.java,
            PlayerCheckedEvent::class.java, RoundCompletedEvent::class.java,
            WinnersDeterminedEvent::class.java, HandCompletedEvent::class.java
        )
    }
}