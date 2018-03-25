package com.flexpoker.game.command.aggregate;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.flexpoker.game.command.commands.CreateGameCommand;
import com.flexpoker.game.command.events.GameCreatedEvent;
import com.flexpoker.game.command.factory.GameFactory;
import com.flexpoker.game.command.framework.GameEvent;
import com.flexpoker.game.query.dto.GameStage;

@Component
public class DefaultGameFactory implements GameFactory {

    @Override
    public Game createNew(CreateGameCommand command) {
        UUID aggregateId = UUID.randomUUID();
        return createGame(false, aggregateId, command.getGameName(),
                command.getNumberOfPlayers(),
                command.getNumberOfPlayersPerTable(),
                command.getCreatedByPlayerId(),
                command.getNumberOfMinutesBetweenBlindLevels(),
                command.getNumberOfSecondsForActionOnTimer());
    }

    @Override
    public Game createFrom(List<GameEvent> events) {
        GameCreatedEvent gameCreatedEvent = (GameCreatedEvent) events.get(0);
        Game game = createGame(true, gameCreatedEvent.getAggregateId(),
                gameCreatedEvent.getGameName(),
                gameCreatedEvent.getNumberOfPlayers(),
                gameCreatedEvent.getNumberOfPlayersPerTable(),
                gameCreatedEvent.getCreatedByPlayerId(),
                gameCreatedEvent.getNumberOfMinutesBetweenBlindLevels(),
                gameCreatedEvent.getNumberOfSecondsForActionOnTimer());
        game.applyAllHistoricalEvents(events);
        return game;
    }

    private Game createGame(boolean creatingFromEvents, UUID aggregateId,
            String gameName, int maxNumberOfPlayers,
            int numberOfPlayersPerTable, UUID createdById,
            int numberOfMinutesBetweenBlindLevels,
            int numberOfSecondsForActionOnTimer) {
        BlindSchedule blindSchedule = new BlindSchedule(numberOfMinutesBetweenBlindLevels);
        TableBalancer tableBalancer = new TableBalancer(aggregateId,
                numberOfPlayersPerTable);
        return new Game(creatingFromEvents, aggregateId, gameName,
                maxNumberOfPlayers, numberOfPlayersPerTable,
                numberOfSecondsForActionOnTimer, createdById,
                GameStage.REGISTERING, blindSchedule, tableBalancer);
    }

}
