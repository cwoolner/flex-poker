package com.flexpoker.core.timertask;

import java.util.TimerTask;

import com.flexpoker.core.api.game.InitializeAndStartGameCommand;
import com.flexpoker.model.Game;

public class MoveGameToInProgressTimerTask extends TimerTask {

    private final InitializeAndStartGameCommand initializeAndStartGameCommand;
    
    private final Game game;
    
    public MoveGameToInProgressTimerTask(
            InitializeAndStartGameCommand initializeAndStartGameCommand,
            Game game) {
        this.initializeAndStartGameCommand = initializeAndStartGameCommand;
        this.game = game;
    }

    
    @Override
    public void run() {
        initializeAndStartGameCommand.execute(game);
        cancel();
    }

}
