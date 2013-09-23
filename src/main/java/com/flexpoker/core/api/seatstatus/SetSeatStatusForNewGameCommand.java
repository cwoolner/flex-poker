package com.flexpoker.core.api.seatstatus;

import com.flexpoker.model.Table;

public interface SetSeatStatusForNewGameCommand {

    void execute(Table table);

}
