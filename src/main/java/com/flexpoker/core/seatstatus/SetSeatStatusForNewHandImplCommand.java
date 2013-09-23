package com.flexpoker.core.seatstatus;

import javax.inject.Inject;

import com.flexpoker.bso.api.ActionOnTimerBso;
import com.flexpoker.config.Command;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForNewHandCommand;
import com.flexpoker.model.Table;

@Command
public class SetSeatStatusForNewHandImplCommand extends BaseSeatStatusCommand
    implements SetSeatStatusForNewHandCommand {

    @Inject
    public SetSeatStatusForNewHandImplCommand(ActionOnTimerBso actionOnTimerBso) {
        this.actionOnTimerBso = actionOnTimerBso;
    }

    @Override
    public void execute(Table table) {
        table.validateTable();
        table.resetStillInHand();
        table.resetShowCards();
        assignNewHandBigBlind(table);
        assignNewHandSmallBlind(table);
        assignNewHandButton(table);
        assignNewHandActionOn(table);
    }

}
