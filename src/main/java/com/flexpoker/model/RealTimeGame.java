package com.flexpoker.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RealTimeGame {

    private Blinds currentBlinds;

    private List<Table> tables = new ArrayList<Table>();

    private Set<UserGameStatus> userGameStatuses = new HashSet<UserGameStatus>();

    private Map<Table, RealTimeHand> realTimeHands = new HashMap<Table, RealTimeHand>();

    public RealTimeGame() {
        currentBlinds = new Blinds(10, 20);
    }

    public Blinds getCurrentBlinds() {
        return currentBlinds;
    }

    public List<Table> getTables() {
        return tables;
    }

    /**
     * Add a new table to the game.  Assign an id to the table equal to the
     * current number of tables.
     */
    public void addTable(Table table) {
        table.setId(tables.size());
        tables.add(table);
    }

    public void removeTable(Table table) {
        tables.remove(table);
    }

    public Table getTable(Table table) {
        return tables.get(table.getId().intValue());
    }

    public void addUserGameStatus(UserGameStatus userGameStatus) {
        userGameStatuses.add(userGameStatus);
    }

    public Set<UserGameStatus> getUserGameStatuses() {
        return userGameStatuses;
    }

    public RealTimeHand getRealTimeHand(Table table) {
        synchronized (this) {
            return realTimeHands.get(table);
        }
    }

    public void addRealTimeHand(Table table, RealTimeHand realTimeHand) {
        synchronized (this) {
            realTimeHands.put(table, realTimeHand);
        }
    }

    public void removeRealTimeHand(Table table, RealTimeHand realTimeHand) {
        synchronized (this) {
            realTimeHands.remove(table);
        }
    }

}
