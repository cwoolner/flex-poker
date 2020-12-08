package com.flexpoker.processmanagers;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.table.command.commands.AutoMoveHandForwardCommand;
import com.flexpoker.table.command.events.AutoMoveHandForwardEvent;
import com.flexpoker.table.command.framework.TableCommand;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class AutoMoveHandForwardProcessManager implements ProcessManager<AutoMoveHandForwardEvent> {

    private final CommandSender<TableCommand> tableCommandSender;

    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    @Inject
    public AutoMoveHandForwardProcessManager(CommandSender<TableCommand> tableCommandSender) {
        this.tableCommandSender = tableCommandSender;
        this.scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(16);
    }

    @Async
    @Override
    public void handle(AutoMoveHandForwardEvent event) {
        scheduledThreadPoolExecutor.schedule(() -> {
            var command = new AutoMoveHandForwardCommand(event.getAggregateId(), event.getGameId());
            tableCommandSender.send(command);
        }, 2, TimeUnit.SECONDS);
    }

}
