package com.flexpoker.core.scheduling;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.inject.Inject;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.chat.SendGameChatMessageCommand;
import com.flexpoker.core.api.game.ChangeGameStageCommand;
import com.flexpoker.core.api.game.InitializeAndStartGameCommand;
import com.flexpoker.core.api.scheduling.ScheduleMoveGameToInProgressCommand;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.chat.outgoing.GameChatMessage;

@Command
public class ScheduleMoveGameToInProgressJdkTimerCommand implements ScheduleMoveGameToInProgressCommand {

    private final SendGameChatMessageCommand sendGameChatMessageCommand;
    
    private final ChangeGameStageCommand changeGameStageCommand;
    
    private final InitializeAndStartGameCommand initializeAndStartGameCommand;
    
    @Inject
    public ScheduleMoveGameToInProgressJdkTimerCommand(
            SendGameChatMessageCommand sendGameChatMessageCommand,
            ChangeGameStageCommand changeGameStageCommand,
            InitializeAndStartGameCommand initializeAndStartGameCommand) {
        this.sendGameChatMessageCommand = sendGameChatMessageCommand;
        this.changeGameStageCommand = changeGameStageCommand;
        this.initializeAndStartGameCommand = initializeAndStartGameCommand;
    }
    
    @Override
    public void execute(final UUID gameId) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            
            @Override
            public void run() {
                changeGameStageCommand.execute(gameId, GameStage.INPROGRESS);
                initializeAndStartGameCommand.execute(gameId);
                sendGameChatMessageCommand.execute(new GameChatMessage(
                        "Game is starting", null, true, gameId));
                timer.cancel();
            }

        }, 5000);
    }

}
