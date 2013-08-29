package com.flexpoker.repository.api;

import java.util.List;

import com.flexpoker.model.Game;

public interface GameRepository {
    
    Game findById(Integer id);
    
    List<Game> findAll();
    
    void save(Game entity);
}
