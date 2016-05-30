package com.flexpoker.processmanagers;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.events.TableResumedAfterBalancingEvent;
import com.flexpoker.table.command.commands.ResumeCommand;
import com.flexpoker.table.command.framework.TableCommandType;

@Component
public class ResumeTableProcessManager
        implements ProcessManager<TableResumedAfterBalancingEvent> {

    private final CommandSender<TableCommandType> tableCommandSender;

    @Inject
    public ResumeTableProcessManager(
            CommandSender<TableCommandType> tableCommandSender) {
        this.tableCommandSender = tableCommandSender;
    }

    @Async
    @Override
    public void handle(TableResumedAfterBalancingEvent event) {
        ResumeCommand command = new ResumeCommand(event.getTableId(),
                event.getAggregateId());
        tableCommandSender.send(command);
    }
}
