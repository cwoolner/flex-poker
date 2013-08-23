package com.flexpoker.core.api.game;

import com.flexpoker.model.GameStage;

public interface ChangeGameStageCommand {

    void execute(Integer gameId, GameStage gameStage);

}
