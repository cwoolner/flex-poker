package com.flexpoker.bso;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.PlayerActionsBso;
import com.flexpoker.model.Game;
import com.flexpoker.model.HandState;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

@Service("actionOnTimerBso")
public class ActionOnTimerBsoImpl implements ActionOnTimerBso {

    private Map<Game, Map<Table, Map<Seat, Integer>>> actionOnTimerMap =
            new HashMap<Game, Map<Table, Map<Seat, Integer>>>();

    @Override
    public void removeGame(Game game) {
        actionOnTimerMap.remove(game);
    }

    @Override
    public void removeTable(Game game, Table table) {
        actionOnTimerMap.get(game).remove(table);
    }

    @Override
    public void removeSeat(Game game, Table table, Seat seat) {
        actionOnTimerMap.get(game).get(table).remove(seat);
    }

    @Override
    public void addSeat(Game game, Table table, Seat seat) {
        if (actionOnTimerMap.get(game) == null) {
            actionOnTimerMap.put(game, new HashMap<Table, Map<Seat, Integer>>());
        }
        if (actionOnTimerMap.get(game).get(table) == null) {
            actionOnTimerMap.get(game).put(table, new HashMap<Seat, Integer>());
        }
        actionOnTimerMap.get(game).get(table).put(seat, 28);
    }

    @Override
    public Map<Game, Map<Table, Map<Seat, Integer>>> getActionOnTimerMap() {
        return actionOnTimerMap;
    }

}
