package com.flexpoker.core.api.seatstatus;

import com.flexpoker.model.Table;

public interface SetSeatStatusForNewRoundCommand {

    void execute(Table table);

}
