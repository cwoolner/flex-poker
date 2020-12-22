package com.flexpoker.game.command.aggregate

import com.flexpoker.game.command.events.GameCreatedEvent
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.events.GameJoinedEvent
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent
import com.flexpoker.game.command.events.GameStartedEvent
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.pcollections.HashTreePMap
import org.pcollections.HashTreePSet
import org.pcollections.PSet
import java.util.ArrayList
import java.util.HashMap
import java.util.UUID

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
        val events = ArrayList<GameEvent>()
        events.add(GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(), 10, 20))
        events.add(GameJoinedEvent(gameId, player1Id))
        events.add(GameJoinedEvent(gameId, player2Id))
        events.add(GameMovedToStartingStageEvent(gameId))
        events.add(GameTablesCreatedAndPlayersAssociatedEvent(gameId, tableToPlayersMap, 2))
        events.add(GameStartedEvent(gameId, tableIds, blindSchedule(10)))
        val game = DefaultGameFactory().createFrom(events)
        val playersToChipsMap = HashMap<UUID, Int>()
        playersToChipsMap[player1Id] = 100
        playersToChipsMap[player2Id] = 100
        game.attemptToStartNewHand(tableId, playersToChipsMap)
        assertEquals(7, game.fetchAppliedEvents().size)
        assertEquals(1, game.fetchNewEvents().size)
    }

}