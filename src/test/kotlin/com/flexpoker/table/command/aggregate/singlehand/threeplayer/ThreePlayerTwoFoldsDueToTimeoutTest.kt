package com.flexpoker.table.command.aggregate.singlehand.threeplayer

import com.flexpoker.table.command.aggregate.TableState
import com.flexpoker.table.command.aggregate.applyEvents
import com.flexpoker.table.command.aggregate.eventproducers.expireActionOn
import com.flexpoker.table.command.aggregate.testhelpers.blindPlayerIds
import com.flexpoker.table.command.aggregate.testhelpers.createBasicTableAndStartHand
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
import org.junit.jupiter.api.Test
import java.util.UUID

class ThreePlayerTwoFoldsDueToTimeoutTest {

    @Test
    fun test() {
        val tableId = UUID.randomUUID()
        val player1Id = UUID.randomUUID()
        val player2Id = UUID.randomUUID()
        val player3Id = UUID.randomUUID()
        val events = createBasicTableAndStartHand(tableId, player1Id, player2Id, player3Id)
        val initState = applyEvents(events)

        val handId = initState.currentHand!!.entityId
        val (buttonOnPlayerId, smallBlindPlayerId, _) = blindPlayerIds(initState)

        val (_, newEvents) = EventProducerApplierBuilder<TableState, TableEvent>()
            .initState(initState)
            .andRun { expireActionOn(it, handId, buttonOnPlayerId) }
            .andRun { expireActionOn(it, handId, smallBlindPlayerId) }
            .run()

        verifyNewEvents(tableId, events + newEvents,
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