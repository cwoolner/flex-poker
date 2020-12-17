package com.flexpoker.game.command.aggregate

import com.flexpoker.game.command.events.GameCreatedEvent
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.events.GameJoinedEvent
import com.flexpoker.game.command.events.GameMovedToStartingStageEvent
import com.flexpoker.game.command.events.GameStartedEvent
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet
import java.util.UUID

class NewHandIsClearedToStartTest {

    @Test
    fun testJoinGameSuccessFirstPlayerJoins() {
        val gameId = UUID.randomUUID()
        val tableId = UUID.randomUUID()
        val player1Id = UUID.randomUUID()
        val player2Id = UUID.randomUUID()
        val tableToPlayersMap = HashMap<UUID, Set<UUID>>()
        val playerIds = HashSet<UUID>()
        playerIds.add(player1Id)
        playerIds.add(player2Id)
        tableToPlayersMap[tableId] = playerIds
        val tableIds: Set<UUID> = tableToPlayersMap.keys
        val events = ArrayList<GameEvent>()
        events.add(GameCreatedEvent(gameId, "test", 2, 2, UUID.randomUUID(), 10, 20))
        events.add(GameJoinedEvent(gameId, player1Id))
        events.add(GameJoinedEvent(gameId, player2Id))
        events.add(GameMovedToStartingStageEvent(gameId))
        events.add(GameTablesCreatedAndPlayersAssociatedEvent(gameId, tableToPlayersMap, 2))
        events.add(GameStartedEvent(gameId, tableIds, BlindSchedule(10).blindScheduleDTO))
        val game = DefaultGameFactory().createFrom(events)
        val playersToChipsMap = HashMap<UUID, Int>()
        playersToChipsMap[player1Id] = 100
        playersToChipsMap[player2Id] = 100
        game.attemptToStartNewHand(tableId, playersToChipsMap)
        assertEquals(7, game.fetchAppliedEvents().size)
        assertEquals(1, game.fetchNewEvents().size)
    }

}