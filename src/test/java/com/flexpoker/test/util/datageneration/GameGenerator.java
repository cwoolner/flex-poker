package com.flexpoker.test.util.datageneration;

import java.util.Date;

import com.flexpoker.model.Game;

public class GameGenerator {

    public static Game createGame(int totalPlayers, int maxPlayersPerTable) {
        return new Game("test", new Date(), UserGenerator.createUser("test"),
                new Date(), totalPlayers, maxPlayersPerTable, false);
    }
}
