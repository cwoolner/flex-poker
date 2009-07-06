package com.flexpoker.dao;

import org.springframework.stereotype.Repository;

import com.flexpoker.model.GameType;

@Repository("gameTypeDao")
public class GameTypeDaoImpl extends GenericDaoImpl<GameType, Integer> implements GameTypeDao {

}
