package com.flexpoker.dao;

import com.flexpoker.model.GameEventType;

public interface GameEventTypeDao extends GenericDao<GameEventType, Integer> {

    GameEventType findByName(String name);

}
