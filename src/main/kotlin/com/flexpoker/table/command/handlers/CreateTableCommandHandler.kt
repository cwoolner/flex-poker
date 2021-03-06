package com.flexpoker.table.command.handlers

import com.flexpoker.framework.command.CommandHandler
import com.flexpoker.framework.event.EventPublisher
import com.flexpoker.table.command.aggregate.DefaultRandomNumberGenerator
import com.flexpoker.table.command.aggregate.eventproducers.createTable
import com.flexpoker.table.command.commands.CreateTableCommand
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.table.command.repository.TableEventRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class CreateTableCommandHandler @Inject constructor(
    private val eventPublisher: EventPublisher<TableEvent>,
    private val tableEventRepository: TableEventRepository
) : CommandHandler<CreateTableCommand> {

    @Async
    override fun handle(command: CreateTableCommand) {
        val newEvents = createTable(command.tableId, command.gameId, command.numberOfPlayersPerTable,
            command.playerIds, DefaultRandomNumberGenerator())
        val newlySavedEventsWithVersions = tableEventRepository.setEventVersionsAndSave(0, newEvents)
        newlySavedEventsWithVersions.forEach { eventPublisher.publish(it) }
    }

}