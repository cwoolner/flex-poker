package com.flexpoker.core.api.game;

import java.util.UUID;

import com.flexpoker.model.GameStage;

public interface ChangeGameStageCommand {

    void execute(UUID gameId, GameStage gameStage);

}
