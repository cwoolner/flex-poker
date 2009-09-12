package com.flexpoker.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RealTimeGame {

    private List<User> users;

    private Map<String, Integer> eventVerificationMap =
            new HashMap<String, Integer>();

    private Blinds currentBlinds;

    private Set<Table> tables = new HashSet<Table>();

    public RealTimeGame(List<User> users) {
        this.users = users;
        currentBlinds = new Blinds(10, 20);
    }

    public boolean isEventVerified(String event) {
        synchronized (this) {
            Integer numberOfVerified = eventVerificationMap.get(event);

            if (numberOfVerified == null) {
                return false;
            }

            return numberOfVerified == users.size();
        }
    }

    public void verifyEvent(User user, String string) {
        synchronized (this) {
            Integer numberOfVerified = eventVerificationMap.get(string);
            if (numberOfVerified == null) {
                numberOfVerified = 0;
            }
            eventVerificationMap.put(string, ++numberOfVerified);
        }
    }

    public Blinds getCurrentBlinds() {
        return currentBlinds;
    }

    public Set<Table> getTables() {
        return tables ;
    }

    public void addTable(Table table) {
        tables.add(table);
    }

    public void removeTable(Table table) {
        tables.remove(table);
    }

}
