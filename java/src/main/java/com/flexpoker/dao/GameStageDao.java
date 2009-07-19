package com.flexpoker.dao;

import com.flexpoker.model.GameStage;

public interface GameStageDao extends GenericDao<GameStage, Integer> {

    GameStage findByName(String gameStageName);

}
