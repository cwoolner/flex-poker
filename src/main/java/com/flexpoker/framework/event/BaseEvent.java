package com.flexpoker.framework.event;

import java.time.Instant;
import java.util.UUID;

import com.flexpoker.util.StringUtils;

/**
 * Base class used to handle some standard methods and fields so that the
 * subclasses can just be specific to what they need.
 */
public abstract class BaseEvent implements Event {

    private final UUID aggregateId;

    private final int version;

    private final Instant time;

    public BaseEvent(UUID aggregateId, int version) {
        this.aggregateId = aggregateId;
        this.version = version;
        this.time = Instant.now();
    }

    @Override
    public UUID getAggregateId() {
        return aggregateId;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public Instant getTime() {
        return time;
    }

    @Override
    public String toString() {
        return StringUtils.allFieldsToString(this);
    }

}
