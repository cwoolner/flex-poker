package com.flexpoker.core.scheduling;

import java.util.Timer;
import java.util.TimerTask;

import com.flexpoker.core.api.scheduling.ScheduleActionOnTimerCommand;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

public class ScheduleActionOnJdkTimerCommand implements
        ScheduleActionOnTimerCommand {

    @Override
    public void execute(final Table table, final Seat seat) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                timer.cancel();
            }

        }, 30000);
    }

}
