package com.flexpoker.bso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexpoker.dao.TableDao;
import com.flexpoker.dao.UserGameStatusDao;
import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.RealTimeGame;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.model.UserGameStatus;

@Transactional
@Service("gameEventBso")
public class GameEventBsoImpl implements GameEventBso {

    private GameBso gameBso;

    private UserGameStatusDao userGameStatusDao;

    private DeckBso deckBso;

    private TableDao tableDao;

    private RealTimeGameBso realTimeGameBso;

    @Override
    public void addUserToGame(User user, Game game) {
        game = gameBso.fetchById(game.getId());

        String gameStage = game.getGameStage().getName();

        if (GameStage.STARTING.equals(gameStage)
            || GameStage.IN_PROGRESS.equals(gameStage)) {
            throw new FlexPokerException("The game has already started");
        }

        if (GameStage.FINISHED.equals(gameStage)) {
            throw new FlexPokerException("The game is already finished.");
        }

        Set<UserGameStatus> userGameStatuses = game.getUserGameStatuses();
        
        for (UserGameStatus userGameStatus : userGameStatuses) {
            if (user.equals(userGameStatus.getUser())) {
                throw new FlexPokerException("You are already in this game.");
            }
        }
        
        Integer totalPlayers = game.getTotalPlayers();
        Integer currentNumberOfPlayers = game.getUserGameStatuses().size();
        
        if (totalPlayers <= currentNumberOfPlayers) {
            throw new FlexPokerException("This game is full.");
        }

        UserGameStatus userGameStatus = new UserGameStatus();
        userGameStatus.setEnterTime(new Date());
        userGameStatus.setGame(game);
        userGameStatus.setUser(user);
        userGameStatusDao.save(userGameStatus.getId(), userGameStatus);
    }

    @Override
    public boolean isGameAtMaxPlayers(Game game) {
        game = gameBso.fetchById(game.getId());
        return game.getUserGameStatuses().size() == game.getTotalPlayers();
    }

    @Override
    public void verifyRegistration(User user, Game game) {
        game = gameBso.fetchById(game.getId());

        Set<UserGameStatus> userGameStatusList = game.getUserGameStatuses();

        for (UserGameStatus userGameStatus : userGameStatusList) {
            if (userGameStatus.getUser().equals(user)) {
                userGameStatus.setVerified(true);
                userGameStatusDao.save(userGameStatus.getId(), userGameStatus);
                break;
            }
        }
    }

    @Override
    public boolean areAllPlayerRegistrationsVerified(Game game) {
        game = gameBso.fetchById(game.getId());

        List<UserGameStatus> verifiedUserGameStatuses = new ArrayList<UserGameStatus>();
        Set<UserGameStatus> allUserGameStatuses = game.getUserGameStatuses();

        for (UserGameStatus userInGame : allUserGameStatuses) {
            if (BooleanUtils.isTrue(userInGame.getVerified())) {
                verifiedUserGameStatuses.add(userInGame);
            }
        }

        return verifiedUserGameStatuses.size() == allUserGameStatuses.size();
    }

    @Override
    public PocketCards fetchPocketCards(User user, Table table) {
        table = tableDao.findById(table.getId());
        return deckBso.fetchPocketCards(user, table);
    }

    @Override
    public void startNewHand(Table table) {
        if (table.getButton() == null) {
            assignNewButton(table);
        } else {
            moveButton(table);
        }

        deckBso.shuffleDeck(table);
    }

    private void moveButton(Table table) {
        // TODO Auto-generated method stub
    }

    private void assignNewButton(Table table) {
        int numberOfPlayersAtTable = table.getSeats().size();
        int dealerPosition = new Random().nextInt(numberOfPlayersAtTable) + 1;

        for (Seat seat : table.getSeats()) {
            if (seat.getPosition().equals(dealerPosition)) {
                table.setButton(seat);
                break;
            }
        }

        tableDao.save(table.getId(), table);
    }

    @Override
    public void startNewHandForAllTables(Game game) {
        game = gameBso.fetchById(game.getId());

        for (Table table : game.getTables()) {
            startNewHand(table);
        }

    }

    @Override
    public boolean haveAllPlayersVerifiedGameInProgress(Game game) {
        RealTimeGame realTimeGame = realTimeGameBso.get(game);
        return realTimeGame.isEventVerified("gameInProgress");
    }

    @Override
    public void verifyGameInProgress(User user, Game game) {
        RealTimeGame realTimeGame = realTimeGameBso.get(game);
        realTimeGame.verifyEvent(user, "gameInProgress");
    }

    public UserGameStatusDao getUserGameStatusDao() {
        return userGameStatusDao;
    }

    public void setUserGameStatusDao(UserGameStatusDao userGameStatusDao) {
        this.userGameStatusDao = userGameStatusDao;
    }

    public GameBso getGameBso() {
        return gameBso;
    }

    public void setGameBso(GameBso gameBso) {
        this.gameBso = gameBso;
    }

    public DeckBso getDeckBso() {
        return deckBso;
    }

    public void setDeckBso(DeckBso deckBso) {
        this.deckBso = deckBso;
    }

    public TableDao getTableDao() {
        return tableDao;
    }

    public void setTableDao(TableDao tableDao) {
        this.tableDao = tableDao;
    }

    public RealTimeGameBso getRealTimeGameBso() {
        return realTimeGameBso;
    }

    public void setRealTimeGameBso(RealTimeGameBso realTimeGameBso) {
        this.realTimeGameBso = realTimeGameBso;
    }

}
