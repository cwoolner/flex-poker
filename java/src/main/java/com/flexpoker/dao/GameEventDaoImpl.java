package com.flexpoker.dao;

import org.springframework.stereotype.Repository;

import com.flexpoker.model.GameEvent;

@Repository("gameEventDao")
public class GameEventDaoImpl extends GenericDaoImpl<GameEvent, Integer>
        implements GameEventDao {

}
