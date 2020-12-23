package com.flexpoker.game.command.handlers

import com.flexpoker.framework.command.CommandHandler
import com.flexpoker.framework.event.EventPublisher
import com.flexpoker.game.command.aggregate.eventproducers.createGame
import com.flexpoker.game.command.commands.CreateGameCommand
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.repository.GameEventRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class CreateGameCommandHandler @Inject constructor(
    private val eventPublisher: EventPublisher<GameEvent>,
    private val gameEventRepository: GameEventRepository
) : CommandHandler<CreateGameCommand> {

    @Async
    override fun handle(command: CreateGameCommand) {
        val newEvents = createGame(command.gameName, command.numberOfPlayers, command.numberOfPlayersPerTable,
            command.createdByPlayerId, command.numberOfMinutesBetweenBlindLevels, command.numberOfSecondsForActionOnTimer)
        val eventsWithVersions = gameEventRepository.setEventVersionsAndSave(0, newEvents)
        eventsWithVersions.forEach { eventPublisher.publish(it) }
    }

}