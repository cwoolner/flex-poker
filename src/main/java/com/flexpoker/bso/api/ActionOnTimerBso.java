package com.flexpoker.bso.api;

import java.util.Map;

import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

public interface ActionOnTimerBso {

    void removeTable(Table table);

    void removeSeat(Table table, Seat seat);

    void addSeat(Table table, Seat seat);

    Map<Table, Map<Seat, Integer>> getActionOnTimerMap();

}
