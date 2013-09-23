package com.flexpoker.repository.api;

import java.util.UUID;

import com.flexpoker.model.RealTimeGame;

public interface RealTimeGameRepository {

    RealTimeGame get(UUID gameId);

    RealTimeGame put(UUID gameId, RealTimeGame realTimeGame);

}
