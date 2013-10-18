package com.flexpoker.core.seatstatus;

import javax.inject.Inject;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.actionon.CreateAndStartActionOnTimerCommand;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForNewHandCommand;
import com.flexpoker.model.Game;
import com.flexpoker.model.Table;

@Command
public class SetSeatStatusForNewHandImplCommand extends BaseSeatStatusCommand
    implements SetSeatStatusForNewHandCommand {

    @Inject
    public SetSeatStatusForNewHandImplCommand(CreateAndStartActionOnTimerCommand createAndStartActionOnTimerCommand) {
        this.createAndStartActionOnTimerCommand = createAndStartActionOnTimerCommand;
    }

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
