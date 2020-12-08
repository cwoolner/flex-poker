package com.flexpoker.processmanagers;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.events.TableResumedAfterBalancingEvent;
import com.flexpoker.table.command.commands.ResumeCommand;
import com.flexpoker.table.command.commands.TableCommand;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class ResumeTableProcessManager implements ProcessManager<TableResumedAfterBalancingEvent> {

    private final CommandSender<TableCommand> tableCommandSender;

    @Inject
    public ResumeTableProcessManager(CommandSender<TableCommand> tableCommandSender) {
        this.tableCommandSender = tableCommandSender;
    }

    @Async
    @Override
    public void handle(TableResumedAfterBalancingEvent event) {
        var command = new ResumeCommand(event.getTableId(), event.getAggregateId());
        tableCommandSender.send(command);
    }

}
