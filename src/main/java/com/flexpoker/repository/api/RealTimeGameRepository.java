package com.flexpoker.repository.api;

import com.flexpoker.model.Game;
import com.flexpoker.model.RealTimeGame;

public interface RealTimeGameRepository {

    RealTimeGame get(Game game);

    RealTimeGame put(Game game, RealTimeGame realTimeGame);

}
