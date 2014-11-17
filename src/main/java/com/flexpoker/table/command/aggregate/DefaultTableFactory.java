package com.flexpoker.table.command.aggregate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.flexpoker.table.command.events.TableCreatedEvent;
import com.flexpoker.table.command.factory.TableFactory;
import com.flexpoker.table.command.framework.TableEvent;

@Component
public class DefaultTableFactory implements TableFactory {

    @Override
    public Table createNew(UUID aggregateId, UUID gameId, int numberOfPlayersPerTable) {
        Map<Integer, UUID> seatMap = new HashMap<>();
        for (int i = 0; i < numberOfPlayersPerTable; i++) {
            seatMap.put(i, null);
        }
        return new Table(aggregateId, gameId, seatMap);
    }

    @Override
    public Table createFrom(List<TableEvent> events) {
        TableCreatedEvent tableCreatedEvent = (TableCreatedEvent) events.get(0);
        Table table = createNew(tableCreatedEvent.getAggregateId(),
                tableCreatedEvent.getGameId(),
                tableCreatedEvent.getNumberOfPlayersPerTable());
        table.applyAllEvents(events);
        return table;
    }
}
