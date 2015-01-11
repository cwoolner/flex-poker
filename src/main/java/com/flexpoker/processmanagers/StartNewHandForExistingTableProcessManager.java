package com.flexpoker.processmanagers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandPublisher;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.events.NewHandIsClearedToStartEvent;
import com.flexpoker.table.command.commands.StartNewHandForExistingTableCommand;
import com.flexpoker.table.command.framework.TableCommandType;

@Component
public class StartNewHandForExistingTableProcessManager implements
        ProcessManager<NewHandIsClearedToStartEvent> {

    private final CommandPublisher<TableCommandType> tableCommandPublisher;

    @Inject
    public StartNewHandForExistingTableProcessManager(
            CommandPublisher<TableCommandType> tableCommandPublisher) {
        this.tableCommandPublisher = tableCommandPublisher;
    }

    @Async
    @Override
    public void handle(NewHandIsClearedToStartEvent event) {
        StartNewHandForExistingTableCommand command = new StartNewHandForExistingTableCommand(
                event.getTableId(), event.getAggregateId(), event.getBlinds());
        tableCommandPublisher.publish(command);
    }
}
