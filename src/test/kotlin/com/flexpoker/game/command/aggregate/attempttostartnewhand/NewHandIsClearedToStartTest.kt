package com.flexpoker.game.command.aggregate.attempttostartnewhand

import com.flexpoker.game.command.aggregate.BlindSchedule
import com.flexpoker.game.command.aggregate.GameState
import com.flexpoker.game.command.aggregate.applyEvents
import com.flexpoker.game.command.aggregate.eventproducers.attemptToStartNewHand
import com.flexpoker.game.command.events.GameCreatedEvent
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.events.GameJoinedEvent
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent
import com.flexpoker.game.command.events.GameStartedEvent
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent
import com.flexpoker.game.command.events.NewHandIsClearedToStartEvent
import com.flexpoker.test.util.EventProducerApplierBuilder
import com.flexpoker.test.util.UnitTestClass
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.pcollections.HashTreePMap
import org.pcollections.HashTreePSet
import org.pcollections.PSet
import java.util.UUID

@UnitTestClass
class NewHandIsClearedToStartTest {

    @Test
    fun testJoinGameSuccessFirstPlayerJoins() {
        val gameId = UUID.randomUUID()
        val tableId = UUID.randomUUID()
        val player1Id = UUID.randomUUID()
        val player2Id = UUID.randomUUID()
        val playerIds = HashTreePSet.singleton(player1Id).plus(player2Id)
        val tableToPlayersMap = HashTreePMap.singleton<UUID, PSet<UUID>>(tableId, playerIds)
        val tableIds = tableToPlayersMap.keys
        val events = listOf(
            GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(), 10, 20),
            GameJoinedEvent(gameId, player1Id),
            GameJoinedEvent(gameId, player2Id),
            GameMovedToStartingStageEvent(gameId),
            GameTablesCreatedAndPlayersAssociatedEvent(gameId, tableToPlayersMap, 2),
            GameStartedEvent(gameId, tableIds, BlindSchedule.init(10))
        )
        val playersToChipsMap = mapOf(player1Id to 100, player2Id to 100)

        val (_, newEvents) = EventProducerApplierBuilder<GameState, GameEvent>()
            .initState(applyEvents(events))
            .andRun { attemptToStartNewHand(it, tableId, playersToChipsMap) }
            .run()
        assertEquals(NewHandIsClearedToStartEvent::class.java, newEvents[0].javaClass)
        assertEquals(1, newEvents.size)
    }

}