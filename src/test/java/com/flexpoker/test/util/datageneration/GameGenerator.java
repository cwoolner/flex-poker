package com.flexpoker.test.util.datageneration;

import java.util.Date;
import java.util.UUID;

import com.flexpoker.model.Game;
import com.flexpoker.model.User;

public class GameGenerator {

    public static Game createGame(int totalPlayers, int maxPlayersPerTable) {
        return new Game("test", new Date(), new User(UUID.randomUUID(), "test"),
                new Date(), totalPlayers, maxPlayersPerTable, false);
    }
}
