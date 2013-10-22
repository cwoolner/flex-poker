package com.flexpoker.core.scheduling;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.chat.SendGameChatMessageCommand;
import com.flexpoker.core.api.game.InitializeAndStartGameCommand;
import com.flexpoker.core.api.scheduling.ScheduleMoveGameToInProgressCommand;
import com.flexpoker.core.timertask.MoveGameToInProgressTimerTask;
import com.flexpoker.model.Game;

@Command
public class ScheduleMoveGameToInProgressJdkTimerCommand implements ScheduleMoveGameToInProgressCommand {

    private final InitializeAndStartGameCommand initializeAndStartGameCommand;
    
    @Inject
    public ScheduleMoveGameToInProgressJdkTimerCommand(
            SendGameChatMessageCommand sendGameChatMessageCommand,
            InitializeAndStartGameCommand initializeAndStartGameCommand) {
        this.initializeAndStartGameCommand = initializeAndStartGameCommand;
    }
    
    @Override
    public void execute(final Game game) {
        final Timer timer = new Timer();
        final TimerTask timerTask = new MoveGameToInProgressTimerTask(
                initializeAndStartGameCommand, game);
        timer.schedule(timerTask, 5000);
    }

}
