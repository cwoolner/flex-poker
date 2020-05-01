package com.flexpoker.processmanagers;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.commands.IncrementBlindsCommand;
import com.flexpoker.game.command.events.GameStartedEvent;
import com.flexpoker.game.command.framework.GameCommandType;

@Component
public class IncrementBlindsCountdownProcessManager implements ProcessManager<GameStartedEvent> {

    private final CommandSender<GameCommandType> gameCommandSender;

    @Inject
    public IncrementBlindsCountdownProcessManager(CommandSender<GameCommandType> gameCommandSender) {
        this.gameCommandSender = gameCommandSender;
    }

    @Async
    @Override
    public void handle(GameStartedEvent event) {
        addNewBlindIncrementTimer(event);
    }

    private void addNewBlindIncrementTimer(GameStartedEvent event) {
        var actionOnTimer = new Timer();
        var timerTask = new TimerTask() {
            @Override
            public void run() {
                var command = new IncrementBlindsCommand(event.getAggregateId());
                gameCommandSender.send(command);
            }
        };

        var blindIncrementInMilliseconds = event.getBlindScheduleDTO().getNumberOfMinutesBetweenLevels() * 60000;
        actionOnTimer.scheduleAtFixedRate(timerTask, blindIncrementInMilliseconds, blindIncrementInMilliseconds);
    }

}
