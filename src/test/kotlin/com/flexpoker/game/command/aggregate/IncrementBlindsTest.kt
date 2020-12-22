package com.flexpoker.game.command.aggregate

import com.flexpoker.exception.FlexPokerException
import com.flexpoker.game.command.events.GameCreatedEvent
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.events.GameJoinedEvent
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent
import com.flexpoker.game.command.events.GameStartedEvent
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.pcollections.HashTreePMap
import org.pcollections.PSet
import java.util.ArrayList
import java.util.UUID

class IncrementBlindsTest {

    @Test
    fun testIncrementBlindsCreatesNewEvent() {
        val gameId = UUID.randomUUID()
        val tableIdToPlayerIdsMap = HashTreePMap.empty<UUID, PSet<UUID>>()
        val tableIds: Set<UUID> = tableIdToPlayerIdsMap.keys
        val events = ArrayList<GameEvent>()
        events.add(GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(), 10, 20))
        events.add(GameJoinedEvent(gameId, UUID.randomUUID()))
        events.add(GameJoinedEvent(gameId, UUID.randomUUID()))
        events.add(GameMovedToStartingStageEvent(gameId))
        events.add(GameTablesCreatedAndPlayersAssociatedEvent(gameId, tableIdToPlayerIdsMap, 2))
        events.add(GameStartedEvent(gameId, tableIds, blindSchedule(10)))
        val game = DefaultGameFactory().createFrom(events)
        game.increaseBlinds()
        assertEquals(7, game.fetchAppliedEvents().size)
        assertEquals(1, game.fetchNewEvents().size)
    }

    @Test
    fun testIncrementBlindsAlreadyAtMaxLevel() {
        val gameId = UUID.randomUUID()
        val tableIdToPlayerIdsMap = HashTreePMap.empty<UUID, PSet<UUID>>()
        val tableIds: Set<UUID> = tableIdToPlayerIdsMap.keys
        val events = ArrayList<GameEvent>()
        events.add(GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(), 10, 20))
        events.add(GameJoinedEvent(gameId, UUID.randomUUID()))
        events.add(GameJoinedEvent(gameId, UUID.randomUUID()))
        events.add(GameMovedToStartingStageEvent(gameId))
        events.add(GameTablesCreatedAndPlayersAssociatedEvent(gameId, tableIdToPlayerIdsMap, 2))
        events.add(GameStartedEvent(gameId, tableIds, blindSchedule(10)))
        val game = DefaultGameFactory().createFrom(events)
        game.increaseBlinds()
        game.increaseBlinds()
        game.increaseBlinds()
        game.increaseBlinds()
        game.increaseBlinds()
        assertEquals(10, game.fetchAppliedEvents().size)
        assertEquals(4, game.fetchNewEvents().size)
    }

    @Test
    fun testFailureWhenNotInTheCorrectStage() {
        val gameId = UUID.randomUUID()
        val tableIdToPlayerIdsMap = HashTreePMap.empty<UUID, PSet<UUID>>()
        val events = ArrayList<GameEvent>()
        events.add(GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(), 10, 20))
        events.add(GameJoinedEvent(gameId, UUID.randomUUID()))
        events.add(GameJoinedEvent(gameId, UUID.randomUUID()))
        events.add(GameMovedToStartingStageEvent(gameId))
        events.add(GameTablesCreatedAndPlayersAssociatedEvent(gameId, tableIdToPlayerIdsMap, 2))
        val game = DefaultGameFactory().createFrom(events)
        assertThrows(FlexPokerException::class.java) { game.increaseBlinds() }
    }
}