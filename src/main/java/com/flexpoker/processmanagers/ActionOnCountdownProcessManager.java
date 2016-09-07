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
import com.flexpoker.table.command.commands.TickActionOnTimerCommand;
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
        clearExistingTimer(event.getAggregateId());
        addNewActionOnTimer(event);
    }

    private void clearExistingTimer(UUID tableId) {
        ScheduledFuture<?> scheduledFuture = actionOnPlayerScheduledFutureMap.get(tableId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        actionOnPlayerScheduledFutureMap.remove(tableId);
    }

    private void addNewActionOnTimer(ActionOnChangedEvent event) {
        ScheduledFuture<?> scheduledFuture = scheduledThreadPoolExecutor
                .scheduleAtFixedRate(new ActionOnCounter(event), 0, 1, TimeUnit.SECONDS);
        actionOnPlayerScheduledFutureMap.put(event.getAggregateId(), scheduledFuture);
    }

    private class ActionOnCounter implements Runnable {

        private int runCount;

        private final UUID gameId;

        private final UUID tableId;

        private final UUID handId;

        private final UUID playerId;

        public ActionOnCounter(ActionOnChangedEvent event) {
            this.gameId = event.getGameId();
            this.tableId = event.getAggregateId();
            this.handId = event.getHandId();
            this.playerId = event.getPlayerId();
        }

        @Override
        public void run() {
            if (runCount == 20) {
                clearExistingTimer(handId);
                tableCommandSender.send(new ExpireActionOnTimerCommand(tableId, gameId, handId, playerId));
            } else {
                runCount++;
                tableCommandSender.send(new TickActionOnTimerCommand(tableId, gameId, handId, 20 - runCount));
            }
        }

    }
}
