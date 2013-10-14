package com.flexpoker.core.api.scheduling;

import com.flexpoker.model.Game;

public interface ScheduleMoveGameToInProgressCommand {

    void execute(Game game);

}
