package com.flexpoker.game.command.aggregate.attempttostartnewhand

import com.flexpoker.game.command.aggregate.GameState
import com.flexpoker.game.command.aggregate.applyEvents
import com.flexpoker.game.command.aggregate.eventproducers.attemptToStartNewHand
import com.flexpoker.game.command.aggregate.eventproducers.createGame
import com.flexpoker.game.command.aggregate.eventproducers.joinGame
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent
import com.flexpoker.test.util.EventProducerApplierBuilder
import com.flexpoker.test.util.UnitTestClass
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.UUID

@UnitTestClass
class TableLevelEventsAppliedTest {

    @Test
    fun testTablePausedAndThenTwoTablesCombinedApplied() {
        val player1Id = UUID.randomUUID()
        val player2Id = UUID.randomUUID()
        val player3Id = UUID.randomUUID()
        val player4Id = UUID.randomUUID()

        val gameCreatedEvent = createGame("test", 4, 2, player1Id,
            1, 20).first()

        val (state, events) = EventProducerApplierBuilder<GameState, GameEvent>()
            .initState(gameCreatedEvent)
            .andRun { joinGame(it, player1Id) }
            .andRun { joinGame(it, player2Id) }
            .andRun { joinGame(it, player3Id) }
            .andRun { joinGame(it, player4Id) }
            .run()

        val (_, tableIdToPlayerIdsMap) = events.first { it.javaClass == GameTablesCreatedAndPlayersAssociatedEvent::class.java }
                as GameTablesCreatedAndPlayersAssociatedEvent
        val (table1, table2) = tableIdToPlayerIdsMap.entries.toList()
        val playerToChipsMap1 = mapOf(
            table1.value.toList().sorted()[0] to 0,
            table2.value.toList().sorted()[0] to 100,
            table1.value.toList().sorted()[1] to 100,
            table2.value.toList().sorted()[1] to 100
        )
        val playerToChipsMap2 = mapOf(
            table2.value.toList().sorted()[0] to 100,
            table1.value.toList().sorted()[1] to 100,
            table2.value.toList().sorted()[1] to 0
        )
        val newEvents1 = attemptToStartNewHand(state, table1.key, playerToChipsMap1)
        val updatedState1 = applyEvents(state, newEvents1)
        assertTrue(updatedState1.tableIdToPlayerIdsMap.containsKey(table1.key))
        assertTrue(updatedState1.tableIdToPlayerIdsMap.containsKey(table2.key))
        assertTrue(updatedState1.pausedTablesForBalancing.contains(table1.key))
        assertFalse(updatedState1.pausedTablesForBalancing.contains(table2.key))

        val newEvents2 = attemptToStartNewHand(updatedState1, table2.key, playerToChipsMap2)
        val updatedState2 = applyEvents(updatedState1, newEvents2)
        assertTrue(updatedState2.tableIdToPlayerIdsMap.containsKey(table1.key))
        assertFalse(updatedState2.tableIdToPlayerIdsMap.containsKey(table2.key))
        assertTrue(updatedState2.pausedTablesForBalancing.isEmpty())
    }

}