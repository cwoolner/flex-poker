package com.flexpoker.table.command.factory

import com.flexpoker.table.command.aggregate.Table
import com.flexpoker.table.command.commands.CreateTableCommand
import com.flexpoker.table.command.events.TableEvent

interface TableFactory {
    fun createNew(command: CreateTableCommand): Table
    fun createFrom(events: List<TableEvent>): Table
}