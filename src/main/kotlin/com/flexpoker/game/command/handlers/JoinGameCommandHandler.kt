package com.flexpoker.game.command.handlers

import com.flexpoker.framework.command.CommandHandler
import com.flexpoker.framework.event.EventPublisher
import com.flexpoker.game.command.commands.JoinGameCommand
import com.flexpoker.game.command.factory.GameFactory
import com.flexpoker.game.command.events.GameEvent
import com.flexpoker.game.command.repository.GameEventRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class JoinGameCommandHandler @Inject constructor(
    private val gameFactory: GameFactory,
    private val eventPublisher: EventPublisher<GameEvent>,
    private val gameEventRepository: GameEventRepository
) : CommandHandler<JoinGameCommand> {

    @Async
    override fun handle(command: JoinGameCommand) {
        val gameEvents = gameEventRepository.fetchAll(command.aggregateId)
        val game = gameFactory.createFrom(gameEvents)
        game.joinGame(command.playerId)
        val eventsWithVersions = gameEventRepository.setEventVersionsAndSave(gameEvents.size, game.fetchNewEvents())
        eventsWithVersions.forEach { eventPublisher.publish(it) }
    }

}