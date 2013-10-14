package com.flexpoker.core.scheduling;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.chat.SendGameChatMessageCommand;
import com.flexpoker.core.api.game.InitializeAndStartGameCommand;
import com.flexpoker.core.api.scheduling.ScheduleMoveGameToInProgressCommand;
import com.flexpoker.model.Game;
import com.flexpoker.model.chat.outgoing.GameChatMessage;

@Command
public class ScheduleMoveGameToInProgressJdkTimerCommand implements ScheduleMoveGameToInProgressCommand {

    private final SendGameChatMessageCommand sendGameChatMessageCommand;
    
    private final InitializeAndStartGameCommand initializeAndStartGameCommand;
    
    @Inject
    public ScheduleMoveGameToInProgressJdkTimerCommand(
            SendGameChatMessageCommand sendGameChatMessageCommand,
            InitializeAndStartGameCommand initializeAndStartGameCommand) {
        this.sendGameChatMessageCommand = sendGameChatMessageCommand;
        this.initializeAndStartGameCommand = initializeAndStartGameCommand;
    }
    
    @Override
    public void execute(final Game game) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            
            @Override
            public void run() {
                initializeAndStartGameCommand.execute(game);
                sendGameChatMessageCommand.execute(new GameChatMessage(
                        "Game is starting", null, true, game.getId()));
                timer.cancel();
            }

        }, 5000);
    }

}
