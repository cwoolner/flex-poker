package com.flexpoker.table.command.aggregate.singlehand.twoplayer

import com.flexpoker.table.command.aggregate.testhelpers.TableTestUtils
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
import com.flexpoker.test.util.CommonAssertions.verifyAppliedAndNewEventsForAggregate
import org.junit.jupiter.api.Test
import java.util.UUID

class TwoPlayerRaiseBySmallBlindAndBigBlindFoldsTest {

    @Test
    fun test() {
        val tableId = UUID.randomUUID()
        val table = TableTestUtils.createBasicTableAndStartHand(tableId, UUID.randomUUID(), UUID.randomUUID())

        // use the info in action on event to determine who the small blind/button is on
        val smallBlindAndButtonPlayerId = (table.fetchNewEvents()[4] as ActionOnChangedEvent).playerId
        table.raise(smallBlindAndButtonPlayerId, 40)
        val bigBlindPlayerId = (table.fetchNewEvents()[6] as ActionOnChangedEvent).playerId
        table.fold(bigBlindPlayerId)
        verifyAppliedAndNewEventsForAggregate(
            table,
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