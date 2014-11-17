package com.flexpoker.table.command.factory;

import java.util.List;
import java.util.UUID;

import com.flexpoker.table.command.aggregate.Table;
import com.flexpoker.table.command.framework.TableEvent;

public interface TableFactory {

    Table createNew(UUID aggregateId, UUID gameId, int numberOfPlayersPerTable);

    Table createFrom(List<TableEvent> events);

}
