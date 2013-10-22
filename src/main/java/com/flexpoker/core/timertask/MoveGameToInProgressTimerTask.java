package com.flexpoker.core.timertask;

import java.util.TimerTask;

import com.flexpoker.core.api.chat.SendGameChatMessageCommand;
import com.flexpoker.core.api.game.InitializeAndStartGameCommand;
import com.flexpoker.model.Game;
import com.flexpoker.model.chat.outgoing.GameChatMessage;

public class MoveGameToInProgressTimerTask extends TimerTask {

    private final SendGameChatMessageCommand sendGameChatMessageCommand;
    
    private final InitializeAndStartGameCommand initializeAndStartGameCommand;
    
    private final Game game;
    
    public MoveGameToInProgressTimerTask(
            SendGameChatMessageCommand sendGameChatMessageCommand,
            InitializeAndStartGameCommand initializeAndStartGameCommand,
            Game game) {
        this.sendGameChatMessageCommand = sendGameChatMessageCommand;
        this.initializeAndStartGameCommand = initializeAndStartGameCommand;
        this.game = game;
    }

    
    @Override
    public void run() {
        initializeAndStartGameCommand.execute(game);
        sendGameChatMessageCommand.execute(new GameChatMessage(
                "Game is starting", null, true, game.getId()));
        cancel();
    }

}
