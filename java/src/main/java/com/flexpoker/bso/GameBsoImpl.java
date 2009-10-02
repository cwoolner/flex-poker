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

        Table table = new Table();
        realTimeGame.addTable(table);
        List<Seat> seats = new ArrayList<Seat>();

        Set<UserGameStatus> userGameStatuses = realTimeGame.getUserGameStatuses();

        int i = 0;
        for (UserGameStatus userGameStatus : userGameStatuses) {
            userGameStatus.setChips(1000);

            Seat seat = new Seat();
            seat.setUserGameStatus(userGameStatus);
            seat.setPosition(i);
            seat.setStillInHand(true);
            seat.setAllIn(false);

            seats.add(seat);
        }

        table.setSeats(seats);
/*
        List<Integer> randomPlayerPositions = new ArrayList<Integer>();

        if (game.getTotalPlayers() > Constants.MAX_PLAYERS_PER_TABLE) {
            for (int i = 0; i < Constants.MAX_PLAYERS_PER_TABLE; i++) {
                randomPlayerPositions.add(i);
            }
        } else {
            for (int i = 0; i < game.getTotalPlayers(); i++) {
                randomPlayerPositions.add(i);
            }
        }

        List<UserGameStatus> userGameStatuses =
                new ArrayList<UserGameStatus>(fetchUserGameStatuses(game));

        for (int i = 0; i < game.getTotalPlayers(); i += Constants.MAX_PLAYERS_PER_TABLE) {
            Table table = new Table();
            realTimeGame.addTable(table);
        }

        int numberOfTables = realTimeGame.getTables().size();

        List<Integer> numberOfPlayersPerTable = new ArrayList<Integer>();

        // TODO: Assign all seats to one table, then send the tables to the
        //       yet to be created TableBalancerBso class.
        for (int i = 0; i < game.getTotalPlayers(); i++) {
        }

        Collections.shuffle(randomPlayerPositions, new Random());

        Set<Seat> seats = new HashSet<Seat>();

//            for (int j = 0; j < )
//            Seat seat = new Seat();
//            seat.setPosition(randomPlayerPositions.get(j));
//            seat.setUserGameStatus(userGameStatuses.get(i));
//            seats.add(seat);

//            table.setSeats(seats);

//            userGameStatuses.get(i).setChips(1000);
*/
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

}
