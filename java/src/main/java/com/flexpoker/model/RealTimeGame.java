package com.flexpoker.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RealTimeGame {

    private List<User> users;

    private Map<String, Integer> eventVerificationMap =
            new HashMap<String, Integer>();

    public boolean isEventVerified(String event) {
        synchronized (this) {
            return eventVerificationMap.get(event) == users.size();
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

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
