package com.flexpoker.game.command.repository

import com.flexpoker.game.command.events.GameCreatedEvent
import com.flexpoker.game.command.events.GameEvent
import java.util.UUID

interface GameEventRepository {
    fun fetchAll(id: UUID): List<GameEvent>
    fun setEventVersionsAndSave(basedOnVersion: Int, events: List<GameEvent>): List<GameEvent>
    fun fetchGameCreatedEvent(aggregateId: UUID): GameCreatedEvent?
}