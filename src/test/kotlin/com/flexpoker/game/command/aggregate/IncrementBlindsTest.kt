package com.flexpoker.game.command.aggregate

import com.flexpoker.game.command.aggregate.eventproducers.increaseBlinds
import com.flexpoker.game.command.events.GameCreatedEvent
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.events.GameJoinedEvent
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent
import com.flexpoker.game.command.events.GameStartedEvent
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent
import com.flexpoker.test.util.EventProducerApplierBuilder
import com.flexpoker.test.util.UnitTestClass
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.pcollections.HashTreePMap
import org.pcollections.PSet
import java.util.UUID

@UnitTestClass
class IncrementBlindsTest {

    @Test
    fun testIncrementBlindsCreatesNewEvent() {
        val gameId = UUID.randomUUID()
        val tableIdToPlayerIdsMap = HashTreePMap.empty<UUID, PSet<UUID>>()
        val tableIds: Set<UUID> = tableIdToPlayerIdsMap.keys
        val events = listOf(
            GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(), 10, 20),
            GameJoinedEvent(gameId, UUID.randomUUID()),
            GameJoinedEvent(gameId, UUID.randomUUID()),
            GameMovedToStartingStageEvent(gameId),
            GameTablesCreatedAndPlayersAssociatedEvent(gameId, tableIdToPlayerIdsMap, 2),
            GameStartedEvent(gameId, tableIds, BlindSchedule.init(10))
        )
        val state = applyEvents(events)
        val newEvents = increaseBlinds(state)
        assertEquals(1, newEvents.size)
    }

    @Test
    fun testIncrementBlindsAlreadyAtMaxLevel() {
        val gameId = UUID.randomUUID()
        val tableIdToPlayerIdsMap = HashTreePMap.empty<UUID, PSet<UUID>>()
        val tableIds: Set<UUID> = tableIdToPlayerIdsMap.keys
        val events = listOf(
            GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(), 10, 20),
            GameJoinedEvent(gameId, UUID.randomUUID()),
            GameJoinedEvent(gameId, UUID.randomUUID()),
            GameMovedToStartingStageEvent(gameId),
            GameTablesCreatedAndPlayersAssociatedEvent(gameId, tableIdToPlayerIdsMap, 2),
            GameStartedEvent(gameId, tableIds, BlindSchedule.init(10))
        )
        val (_, newEvents) = EventProducerApplierBuilder<GameState, GameEvent>()
            .initState(applyEvents(events))
            .andRun { increaseBlinds(it) }
            .andRun { increaseBlinds(it) }
            .andRun { increaseBlinds(it) }
            .andRun { increaseBlinds(it) }
            .andRun { increaseBlinds(it) }
            .run()
        assertEquals(4, newEvents.size)
    }

    @Test
    fun testFailureWhenNotInTheCorrectStage() {
        val gameId = UUID.randomUUID()
        val tableIdToPlayerIdsMap = HashTreePMap.empty<UUID, PSet<UUID>>()
        val events = listOf(
            GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(), 10, 20),
            GameJoinedEvent(gameId, UUID.randomUUID()),
            GameJoinedEvent(gameId, UUID.randomUUID()),
            GameMovedToStartingStageEvent(gameId),
            GameTablesCreatedAndPlayersAssociatedEvent(gameId, tableIdToPlayerIdsMap, 2)
        )
        val (_, _) = EventProducerApplierBuilder<GameState, GameEvent>()
            .initState(applyEvents(events))
            .andRunThrows(IllegalArgumentException::class.java) { increaseBlinds(it) }
            .run()
    }
}