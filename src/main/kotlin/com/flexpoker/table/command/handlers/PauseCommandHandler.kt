package com.flexpoker.table.command.handlers

import com.flexpoker.framework.command.CommandHandler
import com.flexpoker.framework.event.EventPublisher
import com.flexpoker.table.command.aggregate.applyEvents
import com.flexpoker.table.command.aggregate.eventproducers.pause
import com.flexpoker.table.command.commands.PauseCommand
import com.flexpoker.table.command.events.TableEvent
import com.flexpoker.table.command.repository.TableEventRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class PauseCommandHandler @Inject constructor(
    private val eventPublisher: EventPublisher<TableEvent>,
    private val tableEventRepository: TableEventRepository
) : CommandHandler<PauseCommand> {

    @Async
    override fun handle(command: PauseCommand) {
        val existingEvents = tableEventRepository.fetchAll(command.tableId)
        val state = applyEvents(existingEvents)
        val newEvents = pause(state)
        val newlySavedEventsWithVersions = tableEventRepository.setEventVersionsAndSave(existingEvents.size, newEvents)
        newlySavedEventsWithVersions.forEach { eventPublisher.publish(it) }
    }

}