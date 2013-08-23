package com.flexpoker.core.game;

import javax.inject.Inject;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.game.ChangeGameStageCommand;
import com.flexpoker.dao.api.GameDao;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameStage;

@Command
public class ChangeGameStageImplCommand implements ChangeGameStageCommand {

    private final GameDao gameDao;
    
    @Inject
    public ChangeGameStageImplCommand(GameDao gameDao) {
        this.gameDao = gameDao;
    }
    
    @Override
    public void execute(Integer gameId, GameStage gameStage) {
        Game game = gameDao.findById(gameId);
        game.setGameStage(gameStage);
        gameDao.save(game);
    }

}
