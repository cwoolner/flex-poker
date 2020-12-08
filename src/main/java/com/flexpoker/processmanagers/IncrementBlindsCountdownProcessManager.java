package com.flexpoker.processmanagers;

import com.flexpoker.framework.command.CommandSender;
import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.game.command.commands.GameCommand;
import com.flexpoker.game.command.commands.IncrementBlindsCommand;
import com.flexpoker.game.command.events.GameStartedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class IncrementBlindsCountdownProcessManager implements ProcessManager<GameStartedEvent> {

    private final CommandSender<GameCommand> gameCommandSender;

    @Inject
    public IncrementBlindsCountdownProcessManager(CommandSender<GameCommand> gameCommandSender) {
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
