package com.flexpoker.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.flexpoker.model.RealTimeGame;
import com.flexpoker.repository.api.RealTimeGameRepository;

@Repository
public class RealTimeGameHashMapRepository implements RealTimeGameRepository {

    private final Map<Integer, RealTimeGame> realTimeGameMap;
    
    public RealTimeGameHashMapRepository() {
        realTimeGameMap = new HashMap<>();
    }
    
    @Override
    public RealTimeGame get(Integer gameId) {
        return realTimeGameMap.get(gameId);
    }

    @Override
    public RealTimeGame put(Integer gameId, RealTimeGame realTimeGame) {
        return realTimeGameMap.put(gameId, realTimeGame);
    }

}
