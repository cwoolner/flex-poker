package com.flexpoker.game.command.repository

import com.flexpoker.config.ProfileNames
import com.flexpoker.exception.FlexPokerException
import com.flexpoker.game.command.events.GameCreatedEvent
import com.flexpoker.game.command.events.GameEvent
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.UUID
import javax.inject.Inject

@Profile(ProfileNames.REDIS, ProfileNames.TABLE_COMMAND_REDIS)
@Repository
class RedisGameEventRepository @Inject constructor(
    private val redisTemplate: RedisTemplate<String, GameEvent>
) : GameEventRepository {

    companion object {
        private const val GAME_EVENT_NAMESPACE = "game-event:"
    }

    override fun fetchAll(id: UUID): List<GameEvent> {
        return redisTemplate.opsForList().range(GAME_EVENT_NAMESPACE + id, 0, Long.MAX_VALUE)!!
    }

    override fun setEventVersionsAndSave(basedOnVersion: Int, events: List<GameEvent>): List<GameEvent> {
        val aggregateId = events[0].aggregateId
        val existingEvents = fetchAll(aggregateId)
        if (existingEvents.size != basedOnVersion) {
            throw FlexPokerException("events to save are based on a different version of the aggregate")
        }
        for (i in events.indices) {
            events[i].version = basedOnVersion + i + 1
        }
        redisTemplate.opsForList().rightPushAll(GAME_EVENT_NAMESPACE + aggregateId, events)
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