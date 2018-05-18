package com.flexpoker.processmanagers;

import java.util.UUID;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent;
import com.flexpoker.table.command.commands.CreateTableCommand;
import com.flexpoker.table.command.framework.TableCommandType;

@Component
public class CreateInitialTablesForGameProcessManager
        implements ProcessManager<GameTablesCreatedAndPlayersAssociatedEvent> {

    private final CommandSender<TableCommandType> tableCommandSender;

    @Inject
    public CreateInitialTablesForGameProcessManager(CommandSender<TableCommandType> tableCommandSender) {
        this.tableCommandSender = tableCommandSender;
    }

    @Async
    @Override
    public void handle(GameTablesCreatedAndPlayersAssociatedEvent event) {
        Consumer<UUID> tableIdConsumer = tableId -> {
            var command = new CreateTableCommand(tableId, event.getAggregateId(),
                    event.getTableIdToPlayerIdsMap().get(tableId), event.getNumberOfPlayersPerTable());
            tableCommandSender.send(command);
        };
        event.getTableIdToPlayerIdsMap().keySet().forEach(tableIdConsumer);
    }

}
