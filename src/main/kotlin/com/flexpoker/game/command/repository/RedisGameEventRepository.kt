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
    private val redisTemplate: RedisTemplate<String, GameEvent>,
) : GameEventRepository {

    companion object {
        private const val GAME_EVENT_NAMESPACE = "game-event"
    }

    private fun redisKey(gameId: UUID) = "$GAME_EVENT_NAMESPACE:$gameId"

    override fun fetchAll(id: UUID): List<GameEvent> {
        return redisTemplate.opsForList().range(redisKey(id), 0, Long.MAX_VALUE)!!
    }

    override fun setEventVersionsAndSave(basedOnVersion: Int, events: List<GameEvent>): List<GameEvent> {
        return if (events.isEmpty()) {
            emptyList()
        } else {
            val aggregateId = events[0].aggregateId
            val existingEvents = fetchAll(aggregateId)
            if (existingEvents.size != basedOnVersion) {
                throw FlexPokerException("events to save are based on a different version of the aggregate")
            }
            for (i in events.indices) {
                events[i].version = basedOnVersion + i + 1
            }
            redisTemplate.opsForList().rightPushAll(redisKey(aggregateId), events)
            events
        }
    }

    override fun fetchGameCreatedEvent(gameId: UUID): GameCreatedEvent {
        return fetchAll(gameId).first { it.javaClass === GameCreatedEvent::class.java } as GameCreatedEvent
    }

}