package com.flexpoker.processmanagers;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.events.NewHandIsClearedToStartEvent;
import com.flexpoker.table.command.commands.StartNewHandForExistingTableCommand;
import com.flexpoker.table.command.commands.TableCommand;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class StartNewHandForExistingTableProcessManager implements ProcessManager<NewHandIsClearedToStartEvent> {

    private final CommandSender<TableCommand> tableCommandSender;

    @Inject
    public StartNewHandForExistingTableProcessManager(CommandSender<TableCommand> tableCommandSender) {
        this.tableCommandSender = tableCommandSender;
    }

    @Async
    @Override
    public void handle(NewHandIsClearedToStartEvent event) {
        var command = new StartNewHandForExistingTableCommand(event.getTableId(), event.getAggregateId(),
                event.getBlindAmountsDTO().getSmallBlind(), event.getBlindAmountsDTO().getBigBlind());
        tableCommandSender.send(command);
    }

}
