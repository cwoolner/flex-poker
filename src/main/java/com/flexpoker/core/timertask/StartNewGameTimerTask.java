package com.flexpoker.core.timertask;

import java.util.TimerTask;

import com.flexpoker.core.api.chat.SendTableChatMessageCommand;
import com.flexpoker.core.api.game.StartNewHandCommand;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForNewGameCommand;
import com.flexpoker.model.Game;
import com.flexpoker.model.Table;
import com.flexpoker.model.chat.outgoing.TableChatMessage;

public class StartNewGameTimerTask extends TimerTask {

    private final SendTableChatMessageCommand sendTableChatMessageCommand;
    
    private final SetSeatStatusForNewGameCommand setSeatStatusForNewGameCommand;
    
    private final StartNewHandCommand startNewHandCommand;
    
    private final Game game;
    
    public StartNewGameTimerTask(
            SendTableChatMessageCommand sendTableChatMessageCommand,
            SetSeatStatusForNewGameCommand setSeatStatusForNewGameCommand,
            StartNewHandCommand startNewHandCommand,
            Game game) {
        this.sendTableChatMessageCommand = sendTableChatMessageCommand;
        this.setSeatStatusForNewGameCommand = setSeatStatusForNewGameCommand;
        this.startNewHandCommand = startNewHandCommand;
        this.game = game;
    }
    
    @Override
    public void run() {
        for (Table table: game.getTables()) {
            sendTableChatMessageCommand.execute(new TableChatMessage(
                    "Game is starting", null, true, game.getId(), table.getId()));
            setSeatStatusForNewGameCommand.execute(game, table);
            startNewHandCommand.execute(game, table);
        }
        cancel();
    }

}
