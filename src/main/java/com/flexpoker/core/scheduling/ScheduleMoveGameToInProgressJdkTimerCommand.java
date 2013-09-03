package com.flexpoker.core.scheduling;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.chat.SendGameChatMessageCommand;
import com.flexpoker.core.api.scheduling.ScheduleMoveGameToInProgressCommand;
import com.flexpoker.model.chat.outgoing.GameChatMessage;

@Command
public class ScheduleMoveGameToInProgressJdkTimerCommand implements ScheduleMoveGameToInProgressCommand {

    private final SendGameChatMessageCommand sendGameChatMessageCommand;
    
    @Inject
    public ScheduleMoveGameToInProgressJdkTimerCommand(SendGameChatMessageCommand sendGameChatMessageCommand) {
        this.sendGameChatMessageCommand = sendGameChatMessageCommand;
    }
    
    @Override
    public void execute(final Integer gameId) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            
            @Override
            public void run() {
                sendGameChatMessageCommand.execute(new GameChatMessage(
                        "Game is starting", null, true, gameId));
                timer.cancel();
            }

        }, 5000);
    }

}
