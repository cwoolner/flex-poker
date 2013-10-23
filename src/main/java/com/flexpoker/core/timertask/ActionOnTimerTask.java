package com.flexpoker.core.timertask;

import java.util.TimerTask;

import com.flexpoker.core.api.handaction.CheckHandActionCommand;
import com.flexpoker.core.api.handaction.FoldHandActionCommand;
import com.flexpoker.model.Game;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

public class ActionOnTimerTask extends TimerTask {

    private final CheckHandActionCommand checkHandActionCommand;
    
    private final FoldHandActionCommand foldHandActionCommand;
    
    private final Game game;
    
    private final Table table;
    
    private final Seat seat;
    
    public ActionOnTimerTask(CheckHandActionCommand checkHandActionCommand,
            FoldHandActionCommand foldHandActionCommand,
            Game game, Table table, Seat seat)
    {
        this.checkHandActionCommand = checkHandActionCommand;
        this.foldHandActionCommand = foldHandActionCommand;
        this.game = game;
        this.table = table;
        this.seat = seat;
    }
    
    @Override
    public void run() {
        if (seat.getCallAmount() == 0) {
            checkHandActionCommand.execute(game.getId(), table.getId(),
                    seat.getUserGameStatus().getUser());
        } else {
            foldHandActionCommand.execute(game.getId(), table.getId(),
                    seat.getUserGameStatus().getUser());
        }
        cancel();
    }

}
