package com.flexpoker.framework.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Base class used to handle some standard methods and fields so that the
 * subclasses can just be specific to what they need.
 */
public abstract class BaseEvent<T extends EventType> implements Event<T> {

    private final T type;

    private final UUID aggregateId;

    private final int version;

    private final Instant time;

    public BaseEvent(UUID aggregateId, int version, T type) {
        this.aggregateId = aggregateId;
        this.version = version;
        this.type = type;
        this.time = Instant.now();
    }

    @Override
    public T getType() {
        return type;
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

}
