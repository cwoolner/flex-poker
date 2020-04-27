package com.flexpoker.game.command.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.framework.event.BaseEvent;
import com.flexpoker.game.command.framework.GameEvent;

public class BlindsIncreasedEvent extends BaseEvent implements GameEvent {

    @JsonCreator
    public BlindsIncreasedEvent(@JsonProperty(value = "gameId") UUID gameId) {
        super(gameId);
    }

}
