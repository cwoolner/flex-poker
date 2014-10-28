package com.flexpoker.repository.api;

import java.util.UUID;

import com.flexpoker.model.Game;

public interface GameRepository {

    Game findById(UUID id);

}
