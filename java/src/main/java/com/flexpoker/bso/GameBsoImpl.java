package com.flexpoker.bso;

import java.util.Date;
import java.util.List;

import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexpoker.dao.GameDao;
import com.flexpoker.dao.GameStageDao;
import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;

@Transactional
@Service("gameBso")
public class GameBsoImpl implements GameBso {

    private GameDao gameDao;

    private UserBso userBso;

    private GameStageDao gameStageDao;

    @Override
    public List<Game> fetchAllGames() {
        return gameDao.findAll();
    }

    @Override
    public void createGame(User user, Game game) {
        game.setCreatedByUser(user);
        game.setCreatedOn(new Date());
        game.setGameStage(gameStageDao.findByName(GameStage.REGISTERING));

        gameDao.save(game.getId(), game);
    }

    @Override
    public Game fetchById(Integer id) {
        return gameDao.findById(id);
    }

    @Override
    public void changeGameStage(Game game, String gameStageName) {
        game = fetchById(game.getId());
        game.setGameStage(gameStageDao.findByName(gameStageName));
        gameDao.save(game.getId(), game);
    }

    @Override
    public Table fetchPlayersCurrentTable(User user, Game game) {
        // TODO: this should be done in a SQL query for performance reasons
        for (Table table : game.getTables()) {
            for (Seat seat : table.getSeats()) {
                if (seat.getUser().equals(user)) {
                    return table;
                }
            }
        }

        throw new FlexPokerException("Player is not at any table.");
    }

    public GameDao getGameDao() {
        return gameDao;
    }

    public void setGameDao(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public UserBso getUserBso() {
        return userBso;
    }

    public void setUserBso(UserBso userBso) {
        this.userBso = userBso;
    }

    public GameStageDao getGameStageDao() {
        return gameStageDao;
    }

    public void setGameStageDao(GameStageDao gameStageDao) {
        this.gameStageDao = gameStageDao;
    }

}
