package com.flexpoker.game.command.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.game.command.framework.GameEvent;

public class GameCreatedEvent extends BaseGameEvent implements GameEvent {

    private final String gameName;

    private final int numberOfPlayers;

    private final int numberOfPlayersPerTable;

    private final UUID createdByPlayerId;

    private final int numberOfMinutesBetweenBlindLevels;

    private final int numberOfSecondsForActionOnTimer;

    @JsonCreator
    public GameCreatedEvent(
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "gameName") String gameName,
            @JsonProperty(value = "numberOfPlayers") int numberOfPlayers,
            @JsonProperty(value = "numberOfPlayersPerTable") int numberOfPlayersPerTable,
            @JsonProperty(value = "createdByPlayerId") UUID createdByPlayerId,
            @JsonProperty(value = "numberOfMinutesBetweenBlindLevels") int numberOfMinutesBetweenBlindLevels,
            @JsonProperty(value = "numberOfSecondsForActionOnTimer") int numberOfSecondsForActionOnTimer) {
        super(gameId);
        this.gameName = gameName;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfPlayersPerTable = numberOfPlayersPerTable;
        this.createdByPlayerId = createdByPlayerId;
        this.numberOfMinutesBetweenBlindLevels = numberOfMinutesBetweenBlindLevels;
        this.numberOfSecondsForActionOnTimer = numberOfSecondsForActionOnTimer;
    }

    public String getGameName() {
        return gameName;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public int getNumberOfPlayersPerTable() {
        return numberOfPlayersPerTable;
    }

    public UUID getCreatedByPlayerId() {
        return createdByPlayerId;
    }

    public int getNumberOfMinutesBetweenBlindLevels() {
        return numberOfMinutesBetweenBlindLevels;
    }

    public int getNumberOfSecondsForActionOnTimer() {
        return numberOfSecondsForActionOnTimer;
    }

}
