package com.flexpoker.core.game;

import javax.inject.Inject;

import org.springframework.context.ApplicationEventPublisher;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.game.InitializeAndStartGameCommand;
import com.flexpoker.core.api.scheduling.ScheduleStartNewGameCommand;
import com.flexpoker.core.api.tablebalancer.AssignInitialTablesForNewGame;
import com.flexpoker.event.OpenTableForUserEvent;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

@Command
public class InitializeAndStartGameImplCommand implements InitializeAndStartGameCommand {

    private final AssignInitialTablesForNewGame assignInitialTablesForNewGame;
    
    private final ApplicationEventPublisher applicationEventPublisher;
    
    private final ScheduleStartNewGameCommand scheduleStartNewGameCommand;
    
    @Inject
    public InitializeAndStartGameImplCommand(
            AssignInitialTablesForNewGame assignInitialTablesForNewGame,
            ApplicationEventPublisher applicationEventPublisher,
            ScheduleStartNewGameCommand scheduleStartNewGameCommand) {
        this.assignInitialTablesForNewGame = assignInitialTablesForNewGame;
        this.applicationEventPublisher = applicationEventPublisher;
        this.scheduleStartNewGameCommand = scheduleStartNewGameCommand;
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
        
        scheduleStartNewGameCommand.execute(game);
    }
    

}
