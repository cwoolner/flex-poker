package com.flexpoker.processmanagers;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.table.command.commands.ExpireActionOnTimerCommand;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.framework.TableCommandType;

@Component
public class ActionOnCountdownProcessManager implements
        ProcessManager<ActionOnChangedEvent> {

    private final CommandSender<TableCommandType> tableCommandSender;

    private final Map<UUID, Timer> actionOnPlayerTimerMap;

    @Inject
    public ActionOnCountdownProcessManager(
            CommandSender<TableCommandType> tableCommandSender) {
        this.tableCommandSender = tableCommandSender;
        this.actionOnPlayerTimerMap = new HashMap<>();
    }

    @Async
    @Override
    public void handle(ActionOnChangedEvent event) {
        clearExistingTimer(event);
        addNewActionOnTimer(event);
    }

    private void clearExistingTimer(ActionOnChangedEvent event) {
        Timer timer = actionOnPlayerTimerMap.get(event.getHandId());
        if (timer != null) {
            timer.cancel();
        }
        actionOnPlayerTimerMap.remove(event.getHandId());
    }

    private void addNewActionOnTimer(ActionOnChangedEvent event) {
        final CommandSender<TableCommandType> localTableCommandSender = tableCommandSender;
        final Timer actionOnTimer = new Timer();
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                ExpireActionOnTimerCommand command = new ExpireActionOnTimerCommand(
                        event.getAggregateId(), event.getGameId(), event.getHandId(),
                        event.getPlayerId());
                localTableCommandSender.send(command);
            }
        };
        actionOnTimer.schedule(timerTask, 20000);
        actionOnPlayerTimerMap.put(event.getHandId(), actionOnTimer);
    }
}
