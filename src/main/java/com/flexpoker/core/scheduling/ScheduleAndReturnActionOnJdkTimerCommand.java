package com.flexpoker.core.scheduling;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import org.springframework.context.annotation.Lazy;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.handaction.CheckHandActionCommand;
import com.flexpoker.core.api.handaction.FoldHandActionCommand;
import com.flexpoker.core.api.scheduling.ScheduleAndReturnActionOnTimerCommand;
import com.flexpoker.core.timertask.ActionOnTimerTask;
import com.flexpoker.model.Game;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

@Command
public class ScheduleAndReturnActionOnJdkTimerCommand implements
        ScheduleAndReturnActionOnTimerCommand {

    private final CheckHandActionCommand checkHandActionCommand;

    private final FoldHandActionCommand foldHandActionCommand;

    @Inject
    @Lazy
    public ScheduleAndReturnActionOnJdkTimerCommand(
            CheckHandActionCommand checkHandActionCommand,
            FoldHandActionCommand foldHandActionCommand) {
        this.checkHandActionCommand = checkHandActionCommand;
        this.foldHandActionCommand = foldHandActionCommand;
    }

    @Override
    public Timer execute(final Game game, final Table table, final Seat seat) {
        final Timer timer = new Timer();
        final TimerTask timerTask = new ActionOnTimerTask(checkHandActionCommand,
                foldHandActionCommand, game, table, seat);
        timer.schedule(timerTask, 20000);
        return timer;
    }

}
