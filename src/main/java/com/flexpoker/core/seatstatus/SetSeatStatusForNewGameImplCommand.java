package com.flexpoker.core.seatstatus;

import javax.inject.Inject;

import com.flexpoker.bso.api.ActionOnTimerBso;
import com.flexpoker.config.Command;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForNewGameCommand;
import com.flexpoker.model.Table;

@Command
public class SetSeatStatusForNewGameImplCommand extends BaseSeatStatusCommand
    implements SetSeatStatusForNewGameCommand {

    @Inject
    public SetSeatStatusForNewGameImplCommand(ActionOnTimerBso actionOnTimerBso) {
        this.actionOnTimerBso = actionOnTimerBso;
    }

    @Override
    public void execute(Table table) {
        table.resetStillInHand();
        assignNewGameButton(table);
        assignNewGameSmallBlind(table);
        assignNewGameBigBlind(table);
        assignNewHandActionOn(table);
    }

}
