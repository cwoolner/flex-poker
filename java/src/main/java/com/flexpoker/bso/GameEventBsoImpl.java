package com.flexpoker.bso;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexpoker.dao.GameDao;
import com.flexpoker.dao.UserStatusInGameDao;
import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.User;
import com.flexpoker.model.UserStatusInGame;

@Transactional
@Service("gameEventBso")
public class GameEventBsoImpl implements GameEventBso {

    private GameDao gameDao;

    private UserStatusInGameDao userStatusInGameDao;

    @Override
    public void addUserToGame(User user, Game game) {
        game = gameDao.findById(game.getId());

        String gameStage = game.getGameStage().getName();

        if (GameStage.STARTING.equals(gameStage)
            || GameStage.IN_PROGRESS.equals(gameStage)) {
            throw new FlexPokerException("The game has already started");
        }

        if (GameStage.FINISHED.equals(gameStage)) {
            throw new FlexPokerException("The game is already finished.");
        }

        List<UserStatusInGame> userStatuses = game.getUserStatusInGames();
        
        for (UserStatusInGame userStatus : userStatuses) {
            if (user.equals(userStatus.getUser())) {
                throw new FlexPokerException("You are already in this game.");
            }
        }
        
        Integer maximumPlayers = game.getMaximumPlayers();
        Integer currentNumberOfPlayers = game.getUserStatusInGames().size();
        
        if (maximumPlayers <= currentNumberOfPlayers) {
            throw new FlexPokerException("This game is full.");
        }

        UserStatusInGame userStatusInGame = new UserStatusInGame();
        userStatusInGame.setEnterTime(new Date());
        userStatusInGame.setGame(game);
        userStatusInGame.setUser(user);
        userStatusInGameDao.save(userStatusInGame.getId(), userStatusInGame);
    }

    public GameDao getGameDao() {
        return gameDao;
    }

    public void setGameDao(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    
    public UserStatusInGameDao getUserStatusInGameDao() {
        return userStatusInGameDao;
    }

    
    public void setUserStatusInGameDao(UserStatusInGameDao userStatusInGameDao) {
        this.userStatusInGameDao = userStatusInGameDao;
    }

}
