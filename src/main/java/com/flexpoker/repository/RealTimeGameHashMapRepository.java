package com.flexpoker.repository;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.flexpoker.model.Game;
import com.flexpoker.model.RealTimeGame;
import com.flexpoker.repository.api.RealTimeGameRepository;

@Repository
public class RealTimeGameHashMapRepository extends HashMap<Game, RealTimeGame>
        implements RealTimeGameRepository {

    private static final long serialVersionUID = 5614346056128935854L;

    @Override
    public RealTimeGame get(Game game) {
        return super.get(game);
    }

}
