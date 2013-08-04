package com.flexpoker.dao;

import java.util.List;

import com.flexpoker.model.Game;

public interface GameDao {
    
    Game findById(Integer id);
    
    List<Game> findAll();
    
    void save(Game entity);
}
