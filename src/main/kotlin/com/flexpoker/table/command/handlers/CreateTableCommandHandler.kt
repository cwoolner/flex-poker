package com.flexpoker.table.command.handlers

import com.flexpoker.framework.command.CommandHandler
import com.flexpoker.framework.event.EventPublisher
import com.flexpoker.table.command.commands.CreateTableCommand
import com.flexpoker.table.command.factory.TableFactory
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.table.command.repository.TableEventRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class CreateTableCommandHandler @Inject constructor(
    private val tableFactory: TableFactory,
    private val eventPublisher: EventPublisher<TableEvent>,
    private val tableEventRepository: TableEventRepository
) : CommandHandler<CreateTableCommand> {

    @Async
    override fun handle(command: CreateTableCommand) {
        val table = tableFactory.createNew(command)
        val newEvents = table.fetchNewEvents()
        val newlySavedEventsWithVersions = tableEventRepository.setEventVersionsAndSave(0, newEvents)
        newlySavedEventsWithVersions.forEach { eventPublisher.publish(it) }
    }

}