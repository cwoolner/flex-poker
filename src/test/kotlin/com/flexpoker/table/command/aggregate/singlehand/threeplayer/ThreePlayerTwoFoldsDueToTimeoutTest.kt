package com.flexpoker.table.command.aggregate.singlehand.threeplayer

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
import org.junit.jupiter.api.Test
import java.util.UUID

class ThreePlayerTwoFoldsDueToTimeoutTest {

    @Test
    fun test() {
        val tableId = UUID.randomUUID()
        val player1Id = UUID.randomUUID()
        val player2Id = UUID.randomUUID()
        val player3Id = UUID.randomUUID()
        val table = TableTestUtils.createBasicTableAndStartHand(tableId, player1Id, player2Id, player3Id)
        val (_, _, handId, playerId) = table.fetchNewEvents()[4] as ActionOnChangedEvent
        table.expireActionOn(handId, playerId)
        val (_, _, handId1, playerId1) = table.fetchNewEvents()[6] as ActionOnChangedEvent
        table.expireActionOn(handId1, playerId1)
        verifyAppliedAndNewEventsForAggregate(
            table,
            TableCreatedEvent::class.java, CardsShuffledEvent::class.java,
            HandDealtEvent::class.java, PotCreatedEvent::class.java,
            ActionOnChangedEvent::class.java, PlayerForceFoldedEvent::class.java,
            ActionOnChangedEvent::class.java, PlayerForceFoldedEvent::class.java,
            PotAmountIncreasedEvent::class.java, PotAmountIncreasedEvent::class.java,
            RoundCompletedEvent::class.java, WinnersDeterminedEvent::class.java,
            HandCompletedEvent::class.java
        )
    }

}