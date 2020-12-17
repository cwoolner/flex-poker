package com.flexpoker.table.command.aggregate.singlehand.fourplayer

import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils
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
import com.flexpoker.table.command.events.TurnCardDealtEvent
import com.flexpoker.table.command.events.WinnersDeterminedEvent
import com.flexpoker.test.util.CommonAssertions.verifyAppliedAndNewEventsForAggregate
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
        val table = TableTestUtils.createBasicTableAndStartHand(tableId, player1Id, player2Id, player3Id, player4Id)
        val rightOfButtonOnPlayerId = (table.fetchNewEvents()[4] as ActionOnChangedEvent).playerId
        table.call(rightOfButtonOnPlayerId)
        val buttonOnPlayerId = (table.fetchNewEvents()[6] as ActionOnChangedEvent).playerId
        table.call(buttonOnPlayerId)
        val smallBlindPlayerId = (table.fetchNewEvents()[8] as ActionOnChangedEvent).playerId
        table.call(smallBlindPlayerId)
        val bigBlindPlayerId = (table.fetchNewEvents()[10] as ActionOnChangedEvent).playerId
        table.check(bigBlindPlayerId)

        // post-flop
        table.check(smallBlindPlayerId)
        table.check(bigBlindPlayerId)
        table.check(rightOfButtonOnPlayerId)
        table.check(buttonOnPlayerId)

        // post-turn
        table.check(smallBlindPlayerId)
        table.check(bigBlindPlayerId)
        table.check(rightOfButtonOnPlayerId)
        table.check(buttonOnPlayerId)

        // post-river
        table.check(smallBlindPlayerId)
        table.check(bigBlindPlayerId)
        table.check(rightOfButtonOnPlayerId)
        table.check(buttonOnPlayerId)
        verifyAppliedAndNewEventsForAggregate(
            table,
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