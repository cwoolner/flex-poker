package com.flexpoker.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.flexpoker.model.RealTimeGame;
import com.flexpoker.repository.api.RealTimeGameRepository;

@Repository
public class RealTimeGameHashMapRepository implements RealTimeGameRepository {

    private final Map<UUID, RealTimeGame> realTimeGameMap;
    
    public RealTimeGameHashMapRepository() {
        realTimeGameMap = new HashMap<>();
    }
    
    @Override
    public RealTimeGame get(UUID gameId) {
        return realTimeGameMap.get(gameId);
    }

    @Override
    public RealTimeGame put(UUID gameId, RealTimeGame realTimeGame) {
        return realTimeGameMap.put(gameId, realTimeGame);
    }

}
