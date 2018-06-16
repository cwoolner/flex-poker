package com.flexpoker.game.command.handlers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandHandler;
import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.game.command.commands.JoinGameCommand;
import com.flexpoker.game.command.factory.GameFactory;
import com.flexpoker.game.command.framework.GameEvent;
import com.flexpoker.game.command.repository.GameEventRepository;

@Component
public class JoinGameCommandHandler implements CommandHandler<JoinGameCommand> {

    private final GameFactory gameFactory;

    private final EventPublisher<GameEvent> eventPublisher;

    private final GameEventRepository gameEventRepository;

    @Inject
    public JoinGameCommandHandler(GameFactory gameFactory,
            EventPublisher<GameEvent> eventPublisher,
            GameEventRepository gameEventRepository) {
        this.gameFactory = gameFactory;
        this.eventPublisher = eventPublisher;
        this.gameEventRepository = gameEventRepository;
    }

    @Async
    @Override
    public void handle(JoinGameCommand command) {
        var gameEvents = gameEventRepository.fetchAll(command.getAggregateId());
        var game = gameFactory.createFrom(gameEvents);
        game.joinGame(command.getPlayerId());
        var eventsWithVersions = gameEventRepository.setEventVersionsAndSave(gameEvents.size(), game.fetchNewEvents());
        eventsWithVersions.forEach(x -> eventPublisher.publish(x));
    }

}
