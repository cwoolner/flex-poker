package com.flexpoker.core.scheduling;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.chat.SendTableChatMessageCommand;
import com.flexpoker.core.api.game.StartNewHandCommand;
import com.flexpoker.core.api.scheduling.ScheduleStartNewGameCommand;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForNewGameCommand;
import com.flexpoker.core.timertask.StartNewGameTimerTask;
import com.flexpoker.model.Game;

@Command
public class ScheduleStartNewGameJdkTimerCommand implements ScheduleStartNewGameCommand {

    private final SendTableChatMessageCommand sendTableChatMessageCommand;
    
    private final SetSeatStatusForNewGameCommand setSeatStatusForNewGameCommand;
    
    private final StartNewHandCommand startNewHandCommand;
    
    @Inject
    public ScheduleStartNewGameJdkTimerCommand(
            SendTableChatMessageCommand sendTableChatMessageCommand,
            SetSeatStatusForNewGameCommand setSeatStatusForNewGameCommand,
            StartNewHandCommand startNewHandCommand) {
        this.sendTableChatMessageCommand = sendTableChatMessageCommand;
        this.setSeatStatusForNewGameCommand = setSeatStatusForNewGameCommand;
        this.startNewHandCommand = startNewHandCommand;
    }
    
    @Override
    public void execute(Game game) {
        final Timer timer = new Timer();
        final TimerTask timerTask = new StartNewGameTimerTask(
                sendTableChatMessageCommand, setSeatStatusForNewGameCommand,
                startNewHandCommand, game);
        timer.schedule(timerTask, 5000);
    }
}
