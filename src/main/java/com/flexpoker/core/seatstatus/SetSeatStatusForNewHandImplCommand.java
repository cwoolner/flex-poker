package com.flexpoker.core.seatstatus;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForNewHandCommand;
import com.flexpoker.model.Game;
import com.flexpoker.model.Table;

@Command
public class SetSeatStatusForNewHandImplCommand extends BaseSeatStatusCommand implements
        SetSeatStatusForNewHandCommand {

    @Override
    public void execute(Game game, Table table) {
        table.resetStillInHand();
        table.resetShowCards();
        assignNewHandBigBlind(table);
        assignNewHandSmallBlind(table);
        assignNewHandButton(table);
        assignNewHandActionOn(game, table);
    }

}
