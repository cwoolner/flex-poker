package com.flexpoker.game.command.aggregate

import com.flexpoker.game.command.commands.CreateGameCommand
import com.flexpoker.game.command.events.GameCreatedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.util.UUID

class CreateNewGameTest {

    @Test
    fun testCreateNewGameSuccess() {
        val createGameCommand = CreateGameCommand("test", 2, 2, UUID.randomUUID(), 10, 20)
        val game = DefaultGameFactory().createNew(createGameCommand)
        assertEquals(1, game.fetchAppliedEvents().size)
        assertEquals(1, game.fetchNewEvents().size)
        assertEquals(GameCreatedEvent::class.java, game.fetchNewEvents()[0].javaClass)
        val gameCreatedEvent = game.fetchNewEvents()[0] as GameCreatedEvent
        assertNotNull(gameCreatedEvent.aggregateId)
    }

}