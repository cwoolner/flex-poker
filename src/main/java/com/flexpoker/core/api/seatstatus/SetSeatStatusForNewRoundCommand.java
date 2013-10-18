package com.flexpoker.core.api.seatstatus;

import com.flexpoker.model.Game;
import com.flexpoker.model.Table;

public interface SetSeatStatusForNewRoundCommand {

    void execute(Game game, Table table);

}
