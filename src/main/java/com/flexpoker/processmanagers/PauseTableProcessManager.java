package com.flexpoker.processmanagers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.events.TablePausedForBalancingEvent;
import com.flexpoker.table.command.commands.PauseCommand;
import com.flexpoker.table.command.framework.TableCommandType;

@Component
public class PauseTableProcessManager implements ProcessManager<TablePausedForBalancingEvent> {

    private final CommandSender<TableCommandType> tableCommandSender;

    @Inject
    public PauseTableProcessManager(CommandSender<TableCommandType> tableCommandSender) {
        this.tableCommandSender = tableCommandSender;
    }

    @Async
    @Override
    public void handle(TablePausedForBalancingEvent event) {
        var command = new PauseCommand(event.getTableId(), event.getAggregateId());
        tableCommandSender.send(command);
    }

}
