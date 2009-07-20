package com.flexpoker.bso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexpoker.dao.GameDao;
import com.flexpoker.dao.UserStatusInGameDao;
import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameStage;
import com.flexpoker.model.PocketCards;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.model.UserStatusInGame;

@Transactional
@Service("gameEventBso")
public class GameEventBsoImpl implements GameEventBso {

    private GameBso gameBso;

    private UserStatusInGameDao userStatusInGameDao;

    private DeckBso deckBso;

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

        List<UserStatusInGame> userStatuses = game.getUserStatusInGames();
        
        for (UserStatusInGame userStatus : userStatuses) {
            if (user.equals(userStatus.getUser())) {
                throw new FlexPokerException("You are already in this game.");
            }
        }
        
        Integer totalPlayers = game.getTotalPlayers();
        Integer currentNumberOfPlayers = game.getUserStatusInGames().size();
        
        if (totalPlayers <= currentNumberOfPlayers) {
            throw new FlexPokerException("This game is full.");
        }

        UserStatusInGame userStatusInGame = new UserStatusInGame();
        userStatusInGame.setEnterTime(new Date());
        userStatusInGame.setGame(game);
        userStatusInGame.setUser(user);
        userStatusInGameDao.save(userStatusInGame.getId(), userStatusInGame);
    }

    @Override
    public boolean isGameAtMaxPlayers(Game game) {
        game = gameBso.fetchById(game.getId());
        return game.getUserStatusInGames().size() == game.getTotalPlayers();
    }

    @Override
    public void verifyRegistration(User user, Game game) {
        game = gameBso.fetchById(game.getId());

        List<UserStatusInGame> userList = game.getUserStatusInGames();

        for (UserStatusInGame userInGame : userList) {
            if (userInGame.getUser().equals(user)) {
                userInGame.setVerified(true);
                userStatusInGameDao.save(userInGame.getId(), userInGame);
                break;
            }
        }
    }

    @Override
    public boolean areAllPlayerRegistrationsVerified(Game game) {
        game = gameBso.fetchById(game.getId());

        List<UserStatusInGame> verifiedUsers = new ArrayList<UserStatusInGame>();
        List<UserStatusInGame> usersInGame = game.getUserStatusInGames();

        for (UserStatusInGame userInGame : usersInGame) {
            if (BooleanUtils.isTrue(userInGame.getVerified())) {
                verifiedUsers.add(userInGame);
            }
        }

        return verifiedUsers.size() == usersInGame.size();
    }

    @Override
    public PocketCards fetchPocketCards(User user, Game game) {
        game = gameBso.fetchById(game.getId());
        Table table = gameBso.fetchPlayersCurrentTable(user, game);
        return deckBso.fetchPocketCards(user, table);
    }

    public UserStatusInGameDao getUserStatusInGameDao() {
        return userStatusInGameDao;
    }

    public void setUserStatusInGameDao(UserStatusInGameDao userStatusInGameDao) {
        this.userStatusInGameDao = userStatusInGameDao;
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

}
