package com.flexpoker.game.command.handlers;

import java.util.List;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandHandler;
import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.game.command.aggregate.Game;
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
        List<GameEvent> gameEvents = gameEventRepository.fetchAll(command
                .getAggregateId());
        Game game = gameFactory.createFrom(gameEvents);
        game.joinGame(command.getPlayerId());
        game.fetchNewEvents().forEach(x -> gameEventRepository.save(x));
        game.fetchNewEvents().forEach(x -> eventPublisher.publish(x));
    }
}
