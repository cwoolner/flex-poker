package com.flexpoker.core.seatstatus;

import javax.inject.Inject;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.actionon.CreateAndStartActionOnTimerCommand;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForNewRoundCommand;
import com.flexpoker.model.Game;
import com.flexpoker.model.Table;

@Command
public class SetSeatStatusForNewRoundImplCommand extends BaseSeatStatusCommand
    implements SetSeatStatusForNewRoundCommand {

    @Inject
    public SetSeatStatusForNewRoundImplCommand(CreateAndStartActionOnTimerCommand createAndStartActionOnTimerCommand) {
        this.createAndStartActionOnTimerCommand = createAndStartActionOnTimerCommand;
    }

    @Override
    public void execute(Game game, Table table) {
        table.resetChipsInFront();
        assignNewRoundActionOn(game, table);
    }

}
