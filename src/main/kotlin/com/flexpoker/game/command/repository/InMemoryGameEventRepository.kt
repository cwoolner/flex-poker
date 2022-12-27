package com.flexpoker.game.command.repository

import com.flexpoker.config.ProfileNames
import com.flexpoker.exception.FlexPokerException
import com.flexpoker.game.command.events.GameCreatedEvent
import com.flexpoker.game.command.events.GameEvent
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import java.util.UUID

@Profile(ProfileNames.DEFAULT, ProfileNames.TABLE_COMMAND_INMEMORY)
@Repository
class InMemoryGameEventRepository : GameEventRepository {

    private val gameEventMap: MutableMap<UUID, MutableList<GameEvent>> = HashMap()

    override fun fetchAll(id: UUID): List<GameEvent> {
        return gameEventMap[id]!!
    }

    override fun setEventVersionsAndSave(basedOnVersion: Int, events: List<GameEvent>): List<GameEvent> {
        val aggregateId = events[0].aggregateId
        if (!gameEventMap.containsKey(aggregateId)) {
            gameEventMap[aggregateId] = ArrayList()
        }
        val existingEvents: List<GameEvent> = gameEventMap[aggregateId]!!
        if (existingEvents.size != basedOnVersion) {
            throw FlexPokerException("events to save are based on a different version of the aggregate")
        }
        for (i in events.indices) {
            events[i].version = basedOnVersion + i + 1
        }
        gameEventMap[aggregateId]!!.addAll(events)
        return events
    }

    override fun fetchGameCreatedEvent(gameId: UUID): GameCreatedEvent? {
        val gameEvents = fetchAll(gameId)
        for (gameEvent in gameEvents) {
            if (gameEvent.javaClass == GameCreatedEvent::class.java) {
                return gameEvent as GameCreatedEvent
            }
        }
        return null
    }

}