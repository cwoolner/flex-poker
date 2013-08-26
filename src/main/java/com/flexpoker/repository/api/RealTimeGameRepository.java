package com.flexpoker.repository.api;

import com.flexpoker.model.RealTimeGame;

public interface RealTimeGameRepository {

    RealTimeGame get(Integer gameId);

    RealTimeGame put(Integer game, RealTimeGame realTimeGame);

}
