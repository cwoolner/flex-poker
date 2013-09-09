package com.flexpoker.core.game;

import java.util.Set;

import javax.inject.Inject;

import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.game.InitializeAndStartGameCommand;
import com.flexpoker.core.api.tablebalancer.AssignInitialTablesForNewGame;
import com.flexpoker.model.RealTimeGame;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.UserGameStatus;
import com.flexpoker.repository.api.RealTimeGameRepository;
import com.flexpoker.util.MessagingConstants;

@Command
public class InitializeAndStartGameImplCommand implements InitializeAndStartGameCommand {

    private final AssignInitialTablesForNewGame assignInitialTablesForNewGame;
    
    private final RealTimeGameRepository realTimeGameRepository;

    private final SimpMessageSendingOperations messagingTemplate;
    
    @Inject
    public InitializeAndStartGameImplCommand(
            AssignInitialTablesForNewGame assignInitialTablesForNewGame,
            RealTimeGameRepository realTimeGameRepository,
            SimpMessageSendingOperations messagingTemplate) {
        this.assignInitialTablesForNewGame = assignInitialTablesForNewGame;
        this.realTimeGameRepository = realTimeGameRepository;
        this.messagingTemplate = messagingTemplate;
    }
    
    @Override
    public void execute(Integer gameId) {
        assignInitialTablesForNewGame.execute(gameId);

        RealTimeGame realTimeGame = realTimeGameRepository.get(gameId);
        Set<UserGameStatus> userGameStatuses = realTimeGame.getUserGameStatuses();

        for (UserGameStatus userGameStatus : userGameStatuses) {
            userGameStatus.setChips(1500);
        }
        
        for (Table table : realTimeGame.getTables()) {
            for (Seat seat : table.getSeats()) {
                if (seat.getUserGameStatus() != null) {
                    messagingTemplate.convertAndSendToUser(
                            seat.getUserGameStatus().getUser().getUsername(),
                            MessagingConstants.OPEN_TABLE_FOR_USER,
                            new OpenTableForUserDto(gameId, table.getId()));
                }
            }
        }
    }
    
    private class OpenTableForUserDto {

        private final Integer gameId;
        
        private final Integer tableId;
        
        public OpenTableForUserDto(Integer gameId, Integer tableId) {
            this.gameId = gameId;
            this.tableId = tableId;
        }

        public Integer getGameId() {
            return gameId;
        }

        public Integer getTableId() {
            return tableId;
        }

    }

}
