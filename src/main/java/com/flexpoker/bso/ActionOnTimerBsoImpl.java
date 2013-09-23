package com.flexpoker.bso;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.api.ActionOnTimerBso;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

@Service
public class ActionOnTimerBsoImpl implements ActionOnTimerBso {

    private Map<Table, Map<Seat, Integer>> actionOnTimerMap = new HashMap<>();

    @Override
    public void removeTable(Table table) {
        actionOnTimerMap.remove(table);
    }

    @Override
    public void removeSeat(Table table, Seat seat) {
        Map<Seat, Integer> tableMap = actionOnTimerMap.get(table);
        tableMap.remove(seat);
    }

    @Override
    public void addSeat(Table table, Seat seat) {
        if (actionOnTimerMap.get(table) == null) {
            actionOnTimerMap.put(table, new HashMap<Seat, Integer>());
        }
        actionOnTimerMap.get(table).put(seat, 28);
    }

    @Override
    public Map<Table, Map<Seat, Integer>> getActionOnTimerMap() {
        return actionOnTimerMap;
    }

}
