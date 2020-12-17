package com.flexpoker.game.command.factory

import com.flexpoker.game.command.aggregate.DefaultGameFactory
import com.flexpoker.game.command.commands.CreateGameCommand
import com.flexpoker.game.command.events.GameCreatedEvent
import com.flexpoker.game.command.events.GameEvent
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.ArrayList
import java.util.UUID

class GameFactoryTest {

    @Test
    fun testCreateNew() {
        val sut = DefaultGameFactory()
        val createGameCommand = CreateGameCommand("test", 5, 5, UUID.randomUUID(),
            10, 20)
        val game = sut.createNew(createGameCommand)
        assertNotNull(game)
        assertFalse(game.fetchAppliedEvents().isEmpty())
        assertFalse(game.fetchNewEvents().isEmpty())
    }

    @Test
    fun testCreateFrom() {
        val sut = DefaultGameFactory()
        val events = ArrayList<GameEvent>()
        events.add(GameCreatedEvent(UUID.randomUUID(), "test", 0, 0, UUID.randomUUID(),
            10, 20))
        val game = sut.createFrom(events)
        assertNotNull(game)
        assertFalse(game.fetchAppliedEvents().isEmpty())
        assertTrue(game.fetchNewEvents().isEmpty())
    }

}