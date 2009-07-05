package com.flexpoker.dao;

import org.springframework.stereotype.Repository;

import com.flexpoker.model.Game;

@Repository("gameDao")
public class GameDaoImpl extends GenericDaoImpl<Game, Integer> implements GameDao {

}
