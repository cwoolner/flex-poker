package com.flexpoker.processmanagers;

import java.util.UUID;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandPublisher;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.events.GameTablesCreatedAndPlayersAssociatedEvent;
import com.flexpoker.table.command.commands.CreateTableCommand;
import com.flexpoker.table.command.framework.TableCommandType;

@Component
public class CreateInitialTablesForGameProcessManager implements
        ProcessManager<GameTablesCreatedAndPlayersAssociatedEvent> {

    private final CommandPublisher<TableCommandType> tableCommandPublisher;

    @Inject
    public CreateInitialTablesForGameProcessManager(
            CommandPublisher<TableCommandType> tableCommandPublisher) {
        this.tableCommandPublisher = tableCommandPublisher;
    }

    @Async
    @Override
    public void handle(GameTablesCreatedAndPlayersAssociatedEvent event) {
        Consumer<UUID> tableIdConsumer = (UUID tableId) -> {
            CreateTableCommand command = new CreateTableCommand(tableId,
                    event.getAggregateId(),
                    event.getTableIdToPlayerIdsMap().get(tableId),
                    event.getNumberOfPlayersPerTable());
            tableCommandPublisher.publish(command);
        };
        event.getTableIdToPlayerIdsMap().keySet().forEach(tableIdConsumer);
    }
}
