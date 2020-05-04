package com.flexpoker.game.command.events;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.exception.FlexPokerException;
import com.flexpoker.framework.event.Event;
import com.flexpoker.util.StringUtils;

/**
 * Base class used to handle some standard methods and fields so that the
 * subclasses can just be specific to what they need.
 */
public abstract class BaseGameEvent implements Event {

    private final UUID aggregateId;

    private int version;

    private final Instant time;

    public BaseGameEvent(UUID aggregateId) {
        this.aggregateId = aggregateId;
        this.time = Instant.now();
    }

    @JsonProperty
    @Override
    public UUID getAggregateId() {
        return aggregateId;
    }

    @JsonProperty
    @Override
    public int getVersion() {
        if (version == 0) {
            throw new FlexPokerException("should be calling getVersion() in situations where it's already been set");
        }
        return version;
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    @JsonProperty
    @Override
    public Instant getTime() {
        return time;
    }

    @Override
    public String toString() {
        return StringUtils.allFieldsToString(this);
    }

}
