package com.flexpoker.processmanagers;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent;
import com.flexpoker.table.command.commands.CreateTableCommand;
import com.flexpoker.table.command.framework.TableCommand;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.UUID;
import java.util.function.Consumer;

@Component
public class CreateInitialTablesForGameProcessManager
        implements ProcessManager<GameTablesCreatedAndPlayersAssociatedEvent> {

    private final CommandSender<TableCommand> tableCommandSender;

    @Inject
    public CreateInitialTablesForGameProcessManager(CommandSender<TableCommand> tableCommandSender) {
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
