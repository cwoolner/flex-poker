package com.flexpoker.core.seatstatus;

import javax.inject.Inject;

import com.flexpoker.bso.api.ActionOnTimerBso;
import com.flexpoker.config.Command;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForEndOfHandCommand;
import com.flexpoker.model.Table;

@Command
public class SetSeatStatusForEndOfHandImplCommand extends BaseSeatStatusCommand
    implements SetSeatStatusForEndOfHandCommand {

    @Inject
    public SetSeatStatusForEndOfHandImplCommand(ActionOnTimerBso actionOnTimerBso) {
        this.actionOnTimerBso = actionOnTimerBso;
    }
    
    @Override
    public void execute(Table table) {
        table.resetChipsInFront();
        table.resetCallAmounts();
        table.resetRaiseTo();
        table.resetActionOn();
        resetActionOnTimer(table);
    }

}
