package com.flexpoker.table.command.repository

import com.flexpoker.table.command.events.TableEvent
import java.util.UUID

interface TableEventRepository {
    fun fetchAll(id: UUID): List<TableEvent>
    fun setEventVersionsAndSave(basedOnVersion: Int, events: List<TableEvent>): List<TableEvent>
}