package com.flexpoker.core.api.seatstatus;

import com.flexpoker.model.Table;

public interface SetSeatStatusForEndOfHandCommand {

    void execute(Table table);

}
