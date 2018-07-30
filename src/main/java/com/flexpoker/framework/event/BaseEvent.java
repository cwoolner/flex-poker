package com.flexpoker.framework.event;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flexpoker.util.StringUtils;

/**
 * Base class used to handle some standard methods and fields so that the
 * subclasses can just be specific to what they need.
 */
public abstract class BaseEvent implements Event {

    private final UUID aggregateId;

    private int version;

    private final Instant time;

    public BaseEvent(UUID aggregateId) {
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
        return version;
    }

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
