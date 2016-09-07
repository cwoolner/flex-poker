package com.flexpoker.processmanagers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.table.command.commands.ExpireActionOnTimerCommand;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.command.framework.TableCommandType;

@Component
public class ActionOnCountdownProcessManager implements ProcessManager<ActionOnChangedEvent> {

    private final CommandSender<TableCommandType> tableCommandSender;

    private final Map<UUID, ScheduledFuture<?>> actionOnPlayerScheduledFutureMap;

    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    @Inject
    public ActionOnCountdownProcessManager(CommandSender<TableCommandType> tableCommandSender) {
        this.tableCommandSender = tableCommandSender;
        this.actionOnPlayerScheduledFutureMap = new HashMap<>();
        this.scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(16);
        scheduledThreadPoolExecutor.setRemoveOnCancelPolicy(true);
    }

    @Async
    @Override
    public void handle(ActionOnChangedEvent event) {
        clearExistingTimer(event.getHandId());
        addNewActionOnTimer(event);
    }

    private void clearExistingTimer(UUID handId) {
        ScheduledFuture<?> scheduledFuture = actionOnPlayerScheduledFutureMap.get(handId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        actionOnPlayerScheduledFutureMap.remove(handId);
    }

    private void addNewActionOnTimer(ActionOnChangedEvent event) {
        ScheduledFuture<?> scheduledFuture = scheduledThreadPoolExecutor
                .scheduleAtFixedRate(new ActionOnCounter(event), 0, 1, TimeUnit.SECONDS);
        actionOnPlayerScheduledFutureMap.put(event.getHandId(), scheduledFuture);
    }

    private class ActionOnCounter implements Runnable {

        private int runCount;

        private final UUID handId;

        private final ExpireActionOnTimerCommand expireCommand;

        public ActionOnCounter(ActionOnChangedEvent event) {
            this.handId = event.getHandId();
            this.expireCommand = new ExpireActionOnTimerCommand(event.getAggregateId(), event.getGameId(),
                    event.getHandId(), event.getPlayerId());
        }

        @Override
        public void run() {
            if (runCount == 20) {
                clearExistingTimer(handId);
                tableCommandSender.send(expireCommand);
            }
            runCount++;
        }

    }
}
