package com.flexpoker.game.command.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.game.command.framework.GameEvent;

public class GameJoinedEvent extends BaseGameEvent implements GameEvent {

    private final UUID playerId;

    @JsonCreator
    public GameJoinedEvent(
            @JsonProperty(value = "gameId") UUID gameId,
            @JsonProperty(value = "playerId") UUID playerId) {
        super(gameId);
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

}
