package com.flexpoker.game.command.handlers

import com.flexpoker.framework.command.CommandHandler
import com.flexpoker.framework.event.EventPublisher
import com.flexpoker.game.command.commands.CreateGameCommand
import com.flexpoker.game.command.factory.GameFactory
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.repository.GameEventRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class CreateGameCommandHandler @Inject constructor(
    private val gameFactory: GameFactory,
    private val eventPublisher: EventPublisher<GameEvent>,
    private val gameEventRepository: GameEventRepository
) : CommandHandler<CreateGameCommand> {

    @Async
    override fun handle(command: CreateGameCommand) {
        val game = gameFactory.createNew(command)
        val eventsWithVersions = gameEventRepository.setEventVersionsAndSave(0, game.fetchNewEvents())
        eventsWithVersions.forEach { eventPublisher.publish(it) }
    }

}