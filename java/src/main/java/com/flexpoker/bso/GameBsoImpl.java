package com.flexpoker.bso;

import java.util.Date;
import java.util.List;

import org.springframework.flex.messaging.MessageTemplate;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexpoker.dao.GameDao;
import com.flexpoker.model.Game;
import com.flexpoker.model.User;

@Transactional
@Service("gameBso")
@RemotingDestination
public class GameBsoImpl implements GameBso {

    private MessageTemplate defaultMessageTemplate;

    private GameDao gameDao;

    private UserBso userBso;

    @Override
    public List<Game> fetchAllGames() {
        return gameDao.findAll();
    }

    @Override
    public void createGame(Game game) {
        game.setCreatedByUser((User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal());
        game.setCreatedOn(new Date());

        gameDao.save(game.getId(), game);

        defaultMessageTemplate.send("gamesUpdated", true);
    }

    public GameDao getGameDao() {
        return gameDao;
    }

    public void setGameDao(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public MessageTemplate getDefaultMessageTemplate() {
        return defaultMessageTemplate;
    }

    public void setDefaultMessageTemplate(MessageTemplate defaultMessageTemplate) {
        this.defaultMessageTemplate = defaultMessageTemplate;
    }

    public UserBso getUserBso() {
        return userBso;
    }

    
    public void setUserBso(UserBso userBso) {
        this.userBso = userBso;
    }

}
