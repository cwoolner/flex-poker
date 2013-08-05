package com.flexpoker.bso;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.flexpoker.model.Game;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

@Service
public class ActionOnTimerBsoImpl implements ActionOnTimerBso {

    private Map<Game, Map<Table, Map<Seat, Integer>>> actionOnTimerMap =
            new HashMap<Game, Map<Table, Map<Seat, Integer>>>();

    @Override
    public void removeGame(Game game) {
        synchronized (this) {
            actionOnTimerMap.remove(game);
        }
    }

    @Override
    public void removeTable(Game game, Table table) {
        Map<Table, Map<Seat, Integer>> tableMap = actionOnTimerMap.get(game);
        synchronized (tableMap) {
            tableMap.remove(table);
        }
    }

    @Override
    public void removeSeat(Game game, Table table, Seat seat) {
        Map<Table, Map<Seat, Integer>> tableMap = actionOnTimerMap.get(game);
        synchronized (tableMap) {
            tableMap.get(table).remove(seat);
        }
    }

    @Override
    public void addSeat(Game game, Table table, Seat seat) {
        synchronized (this) {
            if (actionOnTimerMap.get(game) == null) {
                actionOnTimerMap.put(game, new HashMap<Table, Map<Seat, Integer>>());
            }
            if (actionOnTimerMap.get(game).get(table) == null) {
                actionOnTimerMap.get(game).put(table, new HashMap<Seat, Integer>());
            }
        }
        Map<Table, Map<Seat, Integer>> tableMap = actionOnTimerMap.get(game);
        synchronized (tableMap) {
            tableMap.get(table).put(seat, 28);
        }
    }

    @Override
    public Map<Game, Map<Table, Map<Seat, Integer>>> getActionOnTimerMap() {
        return actionOnTimerMap;
    }

}
