package com.flexpoker.table.command.repository

import com.flexpoker.config.ProfileNames
import com.flexpoker.exception.FlexPokerException
import com.flexpoker.table.command.events.TableEvent
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import java.util.ArrayList
import java.util.HashMap
import java.util.UUID

@Profile(ProfileNames.DEFAULT, ProfileNames.TABLE_COMMAND_INMEMORY)
@Repository
class InMemoryTableEventRepository : TableEventRepository {

    private val tableEventMap: MutableMap<UUID, MutableList<TableEvent>> = HashMap()

    override fun fetchAll(id: UUID): List<TableEvent> {
        return tableEventMap[id]!!
    }

    override fun setEventVersionsAndSave(basedOnVersion: Int, events: List<TableEvent>): List<TableEvent> {
        val aggregateId = events[0].aggregateId
        if (!tableEventMap.containsKey(aggregateId)) {
            tableEventMap[aggregateId] = ArrayList()
        }
        val existingEvents: List<TableEvent> = tableEventMap[aggregateId]!!
        if (existingEvents.size != basedOnVersion) {
            throw FlexPokerException("events to save are based on a different version of the aggregate")
        }
        for (i in events.indices) {
            events[i].version = basedOnVersion + i + 1
        }
        tableEventMap[aggregateId]!!.addAll(events)
        return events
    }

}