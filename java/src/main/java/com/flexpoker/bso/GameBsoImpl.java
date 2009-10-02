package com.flexpoker.bso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexpoker.dao.GameDao;
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

    private RealTimeGameBso realTimeGameBso;

    private TableBalancerBso tableBalancerBso;

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
        game.setGameStage(GameStage.REGISTERING);
        game.setTotalPlayers(2);
        game.setMaxPlayersPerTable(2);
        game.setAllowRebuys(false);
        gameDao.save(game.getId(), game);

        createRealTimeGame(game);
    }

    @Override
    public Game fetchById(Integer id) {
        return gameDao.findById(id);
    }

    @Override
    public void changeGameStage(Game game, GameStage gameStage) {
        game = fetchById(game.getId());
        game.setGameStage(gameStage);
        gameDao.save(game.getId(), game);
    }

    @Override
    public Table fetchPlayersCurrentTable(User user, Game game) {
        List<Table> tables = fetchTables(game);

        for (Table table : tables) {
            for (Seat seat : table.getSeats()) {
                if (seat.getUserGameStatus().getUser().equals(user)) {
                    return table;
                }
            }
        }

        throw new FlexPokerException("Player is not at any table.");
    }

    @Override
    public void initializePlayersAndTables(Game game) {
        RealTimeGame realTimeGame = realTimeGameBso.get(game);

        List<Table> tables = tableBalancerBso.assignInitialTablesForNewGame(
                realTimeGame.getUserGameStatuses(), game.getMaxPlayersPerTable());

        for (Table table : tables) {
            realTimeGame.addTable(table);
        }

        Set<UserGameStatus> userGameStatuses = realTimeGame.getUserGameStatuses();

        for (UserGameStatus userGameStatus : userGameStatuses) {
            userGameStatus.setChips(1000);
        }
    }

    private void createRealTimeGame(Game game) {
        game = gameDao.findById(game.getId());
        realTimeGameBso.put(game, new RealTimeGame());
    }

    @Override
    public List<Table> fetchTables(Game game) {
        return realTimeGameBso.get(game).getTables();
    }

    @Override
    public Set<UserGameStatus> fetchUserGameStatuses(Game game) {
        return realTimeGameBso.get(game).getUserGameStatuses();
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

    public RealTimeGameBso getRealTimeGameBso() {
        return realTimeGameBso;
    }

    public void setRealTimeGameBso(RealTimeGameBso realTimeGameBso) {
        this.realTimeGameBso = realTimeGameBso;
    }

    public TableBalancerBso getTableBalancerBso() {
        return tableBalancerBso;
    }

    public void setTableBalancerBso(TableBalancerBso tableBalancerBso) {
        this.tableBalancerBso = tableBalancerBso;
    }

}
