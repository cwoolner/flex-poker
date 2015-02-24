package com.flexpoker.game.command.handlers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandHandler;
import com.flexpoker.framework.event.EventPublisher;
import com.flexpoker.game.command.aggregate.Game;
import com.flexpoker.game.command.commands.CreateGameCommand;
import com.flexpoker.game.command.factory.GameFactory;
import com.flexpoker.game.command.framework.GameEventType;
import com.flexpoker.game.command.repository.GameEventRepository;

@Component
public class CreateGameCommandHandler implements CommandHandler<CreateGameCommand> {

    private final GameFactory gameFactory;

    private final EventPublisher<GameEventType> eventPublisher;

    private final GameEventRepository gameEventRepository;

    @Inject
    public CreateGameCommandHandler(GameFactory gameFactory,
            EventPublisher<GameEventType> eventPublisher,
            GameEventRepository gameEventRepository) {
        this.gameFactory = gameFactory;
        this.eventPublisher = eventPublisher;
        this.gameEventRepository = gameEventRepository;
    }

    @Async
    @Override
    public void handle(CreateGameCommand command) {
        Game game = gameFactory.createNew();
        game.createNewGame(command.getGameName(), command.getNumberOfPlayers(),
                command.getNumberOfPlayersPerTable(), command.getCreatedByPlayerId());
        game.fetchNewEvents().forEach(x -> gameEventRepository.save(x));
        game.fetchNewEvents().forEach(x -> eventPublisher.publish(x));
    }
}
