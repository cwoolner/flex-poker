package com.flexpoker.game.command.events;

import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class GameCreatedEvent extends BaseEvent implements GameEvent {

    private final String gameName;

    private final int numberOfPlayers;

    private final int numberOfPlayersPerTable;

    private final UUID createdByPlayerId;

    private final int numberOfMinutesBetweenBlindLevels;

    private final int numberOfSecondsForBlindTimer;

    public GameCreatedEvent(UUID aggregateId, int version, String gameName,
            int numberOfPlayers, int numberOfPlayersPerTable, UUID createdByPlayerId,
            int numberOfMinutesBetweenBlindLevels, int numberOfSecondsForBlindTimer) {
        super(aggregateId, version);
        this.gameName = gameName;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfPlayersPerTable = numberOfPlayersPerTable;
        this.createdByPlayerId = createdByPlayerId;
        this.numberOfMinutesBetweenBlindLevels = numberOfMinutesBetweenBlindLevels;
        this.numberOfSecondsForBlindTimer = numberOfSecondsForBlindTimer;
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

    public int getNumberOfSecondsForBlindTimer() {
        return numberOfSecondsForBlindTimer;
    }

}
