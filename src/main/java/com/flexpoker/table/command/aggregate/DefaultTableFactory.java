package com.flexpoker.table.command.aggregate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.table.command.commands.CreateTableCommand;
import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.table.command.factory.TableFactory;
import com.flexpoker.table.command.events.TableEvent;

@Component
public class DefaultTableFactory implements TableFactory {

    @Override
    public Table createNew(CreateTableCommand command) {
        var numberOfPlayersPerTable = command.getNumberOfPlayersPerTable();

        var seatMap = new HashMap<Integer, UUID>();
        for (var i = 0; i < numberOfPlayersPerTable; i++) {
            seatMap.put(i, null);
        }

        var playerIds = command.getPlayerIds();

        if (playerIds.size() < 2) {
            throw new FlexPokerException("must have at least two players");
        }

        if (playerIds.size() > seatMap.size()) {
            throw new FlexPokerException("player list can't be larger than seatMap");
        }

        // TODO: randomize the seat placement
        var playerIdsList = new ArrayList<>(playerIds);
        for (var i = 0; i < playerIds.size(); i++) {
            seatMap.put(Integer.valueOf(i), playerIdsList.get(i));
        }

        // TODO: add starting chips as a parameter, probably once blind scheduling gets introduced
        return createWithGivenInfo(false, command.getTableId(), command.getGameId(), seatMap, 1500);
    }

    @Override
    public Table createFrom(List<TableEvent> events) {
        var tableCreatedEvent = (TableCreatedEvent) events.get(0);
        var table = createWithGivenInfo(true,
                tableCreatedEvent.getAggregateId(),
                tableCreatedEvent.getGameId(),
                tableCreatedEvent.getSeatPositionToPlayerMap(),
                tableCreatedEvent.getStartingNumberOfChips());
        table.applyAllHistoricalEvents(events);
        return table;
    }

    private Table createWithGivenInfo(boolean creatingFromEvents,
            UUID aggregateId, UUID gameId, Map<Integer, UUID> seatMap,
            int startingNumberOfChips) {
        return new Table(creatingFromEvents, aggregateId, gameId, seatMap, startingNumberOfChips);
    }
}
