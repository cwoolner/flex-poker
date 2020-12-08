package com.flexpoker.processmanagers;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.events.TablePausedForBalancingEvent;
import com.flexpoker.table.command.commands.PauseCommand;
import com.flexpoker.table.command.framework.TableCommand;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class PauseTableProcessManager implements ProcessManager<TablePausedForBalancingEvent> {

    private final CommandSender<TableCommand> tableCommandSender;

    @Inject
    public PauseTableProcessManager(CommandSender<TableCommand> tableCommandSender) {
        this.tableCommandSender = tableCommandSender;
    }

    @Async
    @Override
    public void handle(TablePausedForBalancingEvent event) {
        var command = new PauseCommand(event.getTableId(), event.getAggregateId());
        tableCommandSender.send(command);
    }

}
