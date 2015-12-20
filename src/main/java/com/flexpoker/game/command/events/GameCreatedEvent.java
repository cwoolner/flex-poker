package com.flexpoker.game.command.events;

import java.util.UUID;

import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class GameCreatedEvent extends BaseEvent implements GameEvent {

    private final String gameName;

    private final int numberOfPlayers;

    private final int numberOfPlayersPerTable;

    private final UUID createdByPlayerId;

    public GameCreatedEvent(UUID aggregateId, int version, String gameName,
            int numberOfPlayers, int numberOfPlayersPerTable, UUID createdByPlayerId) {
        super(aggregateId, version);
        this.gameName = gameName;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfPlayersPerTable = numberOfPlayersPerTable;
        this.createdByPlayerId = createdByPlayerId;
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

}
