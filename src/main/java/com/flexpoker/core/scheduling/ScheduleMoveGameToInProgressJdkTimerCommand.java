package com.flexpoker.core.scheduling;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.chat.SendGameChatMessageCommand;
import com.flexpoker.core.api.game.ChangeGameStageCommand;
import com.flexpoker.core.api.scheduling.ScheduleMoveGameToInProgressCommand;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.chat.outgoing.GameChatMessage;

@Command
public class ScheduleMoveGameToInProgressJdkTimerCommand implements ScheduleMoveGameToInProgressCommand {

    private final SendGameChatMessageCommand sendGameChatMessageCommand;
    
    private final ChangeGameStageCommand changeGameStageCommand;
    
    @Inject
    public ScheduleMoveGameToInProgressJdkTimerCommand(
            SendGameChatMessageCommand sendGameChatMessageCommand,
            ChangeGameStageCommand changeGameStageCommand) {
        this.sendGameChatMessageCommand = sendGameChatMessageCommand;
        this.changeGameStageCommand = changeGameStageCommand;
    }
    
    @Override
    public void execute(final Integer gameId) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            
            @Override
            public void run() {
                changeGameStageCommand.execute(gameId, GameStage.INPROGRESS);
                sendGameChatMessageCommand.execute(new GameChatMessage(
                        "Game is starting", null, true, gameId));
                timer.cancel();
            }

        }, 5000);
    }

}
