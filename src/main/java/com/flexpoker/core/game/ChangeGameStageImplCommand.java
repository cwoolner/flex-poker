package com.flexpoker.core.game;

import javax.inject.Inject;

import com.flexpoker.config.Command;
import com.flexpoker.core.api.game.ChangeGameStageCommand;
import com.flexpoker.model.Game;
import com.flexpoker.model.GameStage;
import com.flexpoker.repository.api.GameRepository;

@Command
public class ChangeGameStageImplCommand implements ChangeGameStageCommand {

    private final GameRepository gameDao;
    
    @Inject
    public ChangeGameStageImplCommand(GameRepository gameDao) {
        this.gameDao = gameDao;
    }
    
    @Override
    public void execute(Integer gameId, GameStage gameStage) {
        Game game = gameDao.findById(gameId);
        game.setGameStage(gameStage);
    }

}
