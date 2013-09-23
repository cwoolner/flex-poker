package com.flexpoker.core.seatstatus;

import javax.inject.Inject;

import com.flexpoker.bso.api.ActionOnTimerBso;
import com.flexpoker.config.Command;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForNewRoundCommand;
import com.flexpoker.model.Table;

@Command
public class SetSeatStatusForNewRoundImplCommand extends BaseSeatStatusCommand
    implements SetSeatStatusForNewRoundCommand {

    @Inject
    public SetSeatStatusForNewRoundImplCommand(ActionOnTimerBso actionOnTimerBso) {
        this.actionOnTimerBso = actionOnTimerBso;
    }

    @Override
    public void execute(Table table) {
        table.validateTable();
        table.resetChipsInFront();
        assignNewRoundActionOn(table);
    }

}
