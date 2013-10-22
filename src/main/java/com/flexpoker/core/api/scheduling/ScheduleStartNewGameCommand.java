package com.flexpoker.core.api.scheduling;

import com.flexpoker.model.Game;

public interface ScheduleStartNewGameCommand {

    void execute(Game game);

}
