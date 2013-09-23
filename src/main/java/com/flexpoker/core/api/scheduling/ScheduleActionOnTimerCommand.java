package com.flexpoker.core.api.scheduling;

import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

public interface ScheduleActionOnTimerCommand {

    void execute(Table table, Seat seat);

}
