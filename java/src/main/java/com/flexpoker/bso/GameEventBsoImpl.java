package com.flexpoker.bso;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexpoker.dao.GameDao;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.User;
import com.flexpoker.model.UserStatusInGame;

@Transactional
@Service("gameEventBso")
public class GameEventBsoImpl implements GameEventBso {

    private GameDao gameDao;

    @Override
    public void addUserToGame(User user, Game game) {
        game = gameDao.findById(game.getId());

        String gameStage = game.getGameStage().getName();

        if (GameStage.STARTING.equals(gameStage)
            || GameStage.IN_PROGRESS.equals(gameStage)) {
            throw new IllegalStateException("The game has already started");
        }

        if (GameStage.FINISHED.equals(gameStage)) {
            throw new IllegalStateException("The game is already finished.");
        }

        List<UserStatusInGame> userStatuses = game.getUserStatusInGames();
        
        for (UserStatusInGame userStatus : userStatuses) {
            if (user.equals(userStatus.getUser())) {
                throw new IllegalStateException("You are already in this game.");
            }
        }
        
        Integer maximumPlayers = game.getMaximumPlayers();
        Integer currentNumberOfPlayers = game.getUserStatusInGames().size();
        
        if (maximumPlayers <= currentNumberOfPlayers) {
            throw new IllegalStateException("This game is full.");
        }
        
    }

    public GameDao getGameDao() {
        return gameDao;
    }

    public void setGameDao(GameDao gameDao) {
        this.gameDao = gameDao;
    }

}
