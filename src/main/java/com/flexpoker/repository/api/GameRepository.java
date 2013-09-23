package com.flexpoker.repository.api;

import java.util.List;
import java.util.UUID;

import com.flexpoker.model.Game;

public interface GameRepository {
    
    Game findById(UUID id);
    
    List<Game> findAll();
    
    void saveNew(Game game);
    
    void update(Game game);

}
