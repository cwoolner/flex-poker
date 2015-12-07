package com.flexpoker.game.command.aggregate;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.flexpoker.game.command.commands.CreateGameCommand;
import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.factory.GameFactory;
import com.flexpoker.game.command.framework.GameEvent;

@Component
public class DefaultGameFactory implements GameFactory {

    @Override
    public Game createNew(CreateGameCommand command) {
        UUID aggregateId = UUID.randomUUID();
        return createGame(false, aggregateId, command.getGameName(),
                command.getNumberOfPlayers(),
                command.getNumberOfPlayersPerTable(),
                command.getCreatedByPlayerId());
    }

    @Override
    public Game createFrom(List<GameEvent> events) {
        GameCreatedEvent gameCreatedEvent = (GameCreatedEvent) events.get(0);
        Game game = createGame(true, gameCreatedEvent.getAggregateId(),
                gameCreatedEvent.getGameName(),
                gameCreatedEvent.getNumberOfPlayers(),
                gameCreatedEvent.getNumberOfPlayersPerTable(),
                gameCreatedEvent.getCreatedByPlayerId());
        game.applyAllHistoricalEvents(events);
        return game;
    }

    private Game createGame(boolean creatingFromEvents, UUID aggregateId,
            String gameName, int maxNumberOfPlayers,
            int numberOfPlayersPerTable, UUID createdById) {
        return new Game(creatingFromEvents, aggregateId, gameName,
                maxNumberOfPlayers, numberOfPlayersPerTable, createdById);
    }

}
