package com.flexpoker.processmanagers;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.table.command.commands.AutoMoveHandForwardCommand;
import com.flexpoker.table.command.events.AutoMoveHandForwardEvent;
import com.flexpoker.table.command.framework.TableCommandType;

@Component
public class AutoMoveHandForwardProcessManager implements
        ProcessManager<AutoMoveHandForwardEvent> {

    private final CommandSender<TableCommandType> tableCommandSender;

    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    @Inject
    public AutoMoveHandForwardProcessManager(
            CommandSender<TableCommandType> tableCommandSender) {
        this.tableCommandSender = tableCommandSender;
        this.scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(16);
    }

    @Async
    @Override
    public void handle(AutoMoveHandForwardEvent event) {
        scheduledThreadPoolExecutor.schedule(() -> {
            AutoMoveHandForwardCommand command = new AutoMoveHandForwardCommand(
                    event.getAggregateId(), event.getGameId());
            tableCommandSender.send(command);
        }, 2, TimeUnit.SECONDS);
    }
}
