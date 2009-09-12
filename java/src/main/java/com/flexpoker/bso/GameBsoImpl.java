package com.flexpoker.bso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexpoker.dao.GameDao;
import com.flexpoker.dao.GameStageDao;
import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.RealTimeGame;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.model.UserGameStatus;

@Transactional
@Service("gameBso")
public class GameBsoImpl implements GameBso {

    private GameDao gameDao;

    private UserBso userBso;

    private GameStageDao gameStageDao;

    private RealTimeGameBso realTimeGameBso;

    @Override
    public List<Game> fetchAllGames() {
        return gameDao.findAll();
    }

    @Override
    public void createGame(User user, Game game) {
        // the id is set to 0 when it comes from Flex, explicitly set it to null
        game.setId(null);
        game.setCreatedByUser(user);
        game.setCreatedOn(new Date());
        game.setGameStage(gameStageDao.findByName(GameStage.REGISTERING));
        game.setTotalPlayers(2);
        game.setAllowRebuys(false);
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
        game = fetchById(game.getId());

        for (Table table : game.getTables()) {
            for (Seat seat : table.getSeats()) {
                if (seat.getUserGameStatus().getUser().equals(user)) {
                    return table;
                }
            }
        }

        throw new FlexPokerException("Player is not at any table.");
    }

    @Override
    public void intializePlayersAndTables(Game game) {
        game = fetchById(game.getId());

        List<Integer> randomPlayerPositions = new ArrayList<Integer>();

        for (int i = 1; i <= game.getTotalPlayers(); i++) {
            randomPlayerPositions.add(i);
        }

        Collections.shuffle(randomPlayerPositions, new Random());

        Table table = new Table();
        table.setGame(game);
        // TODO: TableDao work.
        // tableDao.save(table.getId(), table);

        int i = 0;
        for (UserGameStatus userGameStatus : game.getUserGameStatuses()) {
            Seat seat = new Seat();
            seat.setPosition(randomPlayerPositions.get(i));
            seat.setTable(table);
            seat.setUserGameStatus(userGameStatus);
            // TODO: Save the seat information in some way.

            userGameStatus.setChips(1000);

            i++;
        }
    }

    @Override
    public void createRealTimeGame(Game game) {
        game = gameDao.findById(game.getId());

        Set<UserGameStatus> userGameStatuses = game.getUserGameStatuses();

        List<User> userList = new ArrayList<User>();
        for (UserGameStatus userGameStatus : userGameStatuses) {
            userList.add(userGameStatus.getUser());
        }
        realTimeGameBso.put(game, new RealTimeGame(userList));
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

    public RealTimeGameBso getRealTimeGameBso() {
        return realTimeGameBso;
    }

    public void setRealTimeGameBso(RealTimeGameBso realTimeGameBso) {
        this.realTimeGameBso = realTimeGameBso;
    }

}
