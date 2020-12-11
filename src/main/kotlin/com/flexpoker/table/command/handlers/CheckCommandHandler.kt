package com.flexpoker.table.command.handlers

import com.flexpoker.framework.command.CommandHandler
import com.flexpoker.framework.event.EventPublisher
import com.flexpoker.table.command.commands.CheckCommand
import com.flexpoker.table.command.factory.TableFactory
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.table.command.repository.TableEventRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class CheckCommandHandler @Inject constructor(
    private val tableFactory: TableFactory,
    private val eventPublisher: EventPublisher<TableEvent>,
    private val tableEventRepository: TableEventRepository
) : CommandHandler<CheckCommand> {

    @Async
    override fun handle(command: CheckCommand) {
        val existingEvents = tableEventRepository.fetchAll(command.tableId)
        val table = tableFactory.createFrom(existingEvents)
        table.check(command.playerId)
        val newEvents = table.fetchNewEvents()
        val newlySavedEventsWithVersions = tableEventRepository.setEventVersionsAndSave(existingEvents.size, newEvents)
        newlySavedEventsWithVersions.forEach { eventPublisher.publish(it) }
    }

}