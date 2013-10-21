package com.flexpoker.core.game;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import org.springframework.context.ApplicationEventPublisher;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.game.InitializeAndStartGameCommand;
import com.flexpoker.core.api.game.StartNewHandCommand;
import com.flexpoker.core.api.seatstatus.SetSeatStatusForNewGameCommand;
import com.flexpoker.core.api.tablebalancer.AssignInitialTablesForNewGame;
import com.flexpoker.event.OpenTableForUserEvent;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

@Command
public class InitializeAndStartGameImplCommand implements InitializeAndStartGameCommand {

    private final AssignInitialTablesForNewGame assignInitialTablesForNewGame;
    
    private final SetSeatStatusForNewGameCommand setSeatStatusForNewGameCommand;
    
    private final ApplicationEventPublisher applicationEventPublisher;
    
    private final StartNewHandCommand startNewHandCommand;
    
    @Inject
    public InitializeAndStartGameImplCommand(
            AssignInitialTablesForNewGame assignInitialTablesForNewGame,
            SetSeatStatusForNewGameCommand setSeatStatusForNewGameCommand,
            ApplicationEventPublisher applicationEventPublisher,
            StartNewHandCommand startNewHandCommand) {
        this.assignInitialTablesForNewGame = assignInitialTablesForNewGame;
        this.setSeatStatusForNewGameCommand = setSeatStatusForNewGameCommand;
        this.applicationEventPublisher = applicationEventPublisher;
        this.startNewHandCommand = startNewHandCommand;
    }
    
    @Override
    public void execute(final Game game) {
        game.setGameStage(GameStage.INPROGRESS);

        assignInitialTablesForNewGame.execute(game);

        for (Table table : game.getTables()) {
            for (Seat seat : table.getSeats()) {
                if (seat.getUserGameStatus() != null) {
                    applicationEventPublisher.publishEvent(new OpenTableForUserEvent(this,
                            seat.getUserGameStatus().getUser().getUsername(),
                            game.getId(), table.getId()));
                }
            }
        }

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Table table: game.getTables()) {
                    setSeatStatusForNewGameCommand.execute(game, table);
                    startNewHandCommand.execute(game, table);
                }
                timer.cancel();
            }
        }, 5000);
    }
    

}
