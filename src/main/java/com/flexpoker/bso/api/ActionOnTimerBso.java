package com.flexpoker.bso.api;

import java.util.Map;

import com.flexpoker.model.Game;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;

public interface ActionOnTimerBso {

    void removeGame(Game game);

    void removeTable(Game game, Table table);

    void removeSeat(Game game, Table table, Seat seat);

    void addSeat(Game game, Table table, Seat seat);

    Map<Game, Map<Table, Map<Seat, Integer>>> getActionOnTimerMap();

}
