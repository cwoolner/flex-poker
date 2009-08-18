package com.flexpoker.dao;

import com.flexpoker.model.GameEvent;
import com.flexpoker.model.Table;

public interface GameEventDao extends GenericDao<GameEvent, Integer> {

    GameEvent findLatestTableEvent(Table table);

}
