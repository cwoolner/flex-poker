package com.flexpoker.table.command.repository

import com.flexpoker.config.ProfileNames
import com.flexpoker.exception.FlexPokerException
import com.flexpoker.table.command.events.TableEvent
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.UUID
import javax.inject.Inject

@Profile(ProfileNames.REDIS, ProfileNames.TABLE_COMMAND_REDIS)
@Repository
class RedisTableEventRepository @Inject constructor(
    private val redisTemplate: RedisTemplate<String, TableEvent>
) : TableEventRepository {

    companion object {
        private const val TABLE_EVENT_NAMESPACE = "table-event:"
    }

    override fun fetchAll(id: UUID): List<TableEvent> {
        return redisTemplate.opsForList().range(TABLE_EVENT_NAMESPACE + id, 0, Long.MAX_VALUE)
    }

    override fun setEventVersionsAndSave(basedOnVersion: Int, events: List<TableEvent>): List<TableEvent> {
        val aggregateId = events[0].aggregateId
        val existingEvents = fetchAll(aggregateId)
        if (existingEvents.size != basedOnVersion) {
            throw FlexPokerException("events to save are based on a different version of the aggregate")
        }
        for (i in events.indices) {
            events[i].version = basedOnVersion + i + 1
        }
        redisTemplate.opsForList().rightPushAll(TABLE_EVENT_NAMESPACE + aggregateId, events)
        return events
    }

}