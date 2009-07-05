package com.flexpoker.bso;

import java.util.Date;
import java.util.List;

import org.springframework.flex.messaging.MessageTemplate;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexpoker.dao.GameDao;
import com.flexpoker.dao.GameTypeDao;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameType;
import com.flexpoker.model.User;

import flex.messaging.FlexContext;

@Transactional
@Service("gameBso")
@RemotingDestination(channels = { "my-amf" })
public class GameBsoImpl implements GameBso {

    private MessageTemplate messageTemplate;

    private GameDao gameDao;
    
    private GameTypeDao gameTypeDao;

    @Override
    public List<Game> fetchAllGames() {
        return gameDao.findAll();
    }

    @Override
    public void createGame() {
        GameType gameType = gameTypeDao.findById(1);
        
        Game game = new Game();
        game.setCreatedByUser((User) FlexContext.getUserPrincipal());
        game.setCreatedOn(new Date());
        game.setGameType(gameType);
        
        gameDao.save(game.getId(), game);
    }

    public MessageTemplate getMessageTemplate() {
        return messageTemplate;
    }

    public void setMessageTemplate(MessageTemplate messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public GameDao getGameDao() {
        return gameDao;
    }

    public void setGameDao(GameDao gameDao) {
        this.gameDao = gameDao;
    }

}
