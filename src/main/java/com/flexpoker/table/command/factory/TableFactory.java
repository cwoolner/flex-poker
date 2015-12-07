package com.flexpoker.table.command.factory;

import java.util.List;

import com.flexpoker.table.command.aggregate.Table;
import com.flexpoker.table.command.commands.CreateTableCommand;
import com.flexpoker.table.command.framework.TableEvent;

public interface TableFactory {

    Table createNew(CreateTableCommand command);

    Table createFrom(List<TableEvent> events);

}
