package com.flexpoker.core.timertask;

import java.util.TimerTask;

import com.flexpoker.core.api.handaction.CallHandActionCommand;
import com.flexpoker.core.api.handaction.FoldHandActionCommand;
import com.flexpoker.model.Game;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

public class ActionOnTimerTask extends TimerTask {

    private final CallHandActionCommand callHandActionCommand;
    
    private final FoldHandActionCommand foldHandActionCommand;
    
    private final Game game;
    
    private final Table table;
    
    private final Seat seat;
    
    public ActionOnTimerTask(CallHandActionCommand callHandActionCommand,
            FoldHandActionCommand foldHandActionCommand,
            Game game, Table table, Seat seat)
    {
        this.callHandActionCommand = callHandActionCommand;
        this.foldHandActionCommand = foldHandActionCommand;
        this.game = game;
        this.table = table;
        this.seat = seat;
    }
    
    @Override
    public void run() {
        if (seat.getCallAmount() == 0) {
            callHandActionCommand.execute(game.getId(), table.getId(),
                    seat.getUserGameStatus().getUser());
        } else {
            foldHandActionCommand.execute(game.getId(), table.getId(),
                    seat.getUserGameStatus().getUser());
        }
        cancel();
    }

}
