package com.flexpoker.game.command.handlers

import com.flexpoker.framework.command.CommandHandler
import com.flexpoker.framework.event.EventPublisher
import com.flexpoker.game.command.aggregate.applyEvents
import com.flexpoker.game.command.aggregate.eventproducers.attemptToStartNewHand
import com.flexpoker.game.command.commands.AttemptToStartNewHandCommand
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.repository.GameEventRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class AttemptToStartNewHandCommandHandler @Inject constructor(
    private val eventPublisher: EventPublisher<GameEvent>,
    private val gameEventRepository: GameEventRepository
) : CommandHandler<AttemptToStartNewHandCommand> {

    @Async
    override fun handle(command: AttemptToStartNewHandCommand) {
        val gameEvents = gameEventRepository.fetchAll(command.aggregateId)
        val state = applyEvents(gameEvents)
        val newEvents = attemptToStartNewHand(state, command.tableId, command.playerToChipsAtTableMap)
        val eventsWithVersions = gameEventRepository.setEventVersionsAndSave(gameEvents.size, newEvents)
        eventsWithVersions.forEach { eventPublisher.publish(it) }
    }

}