package com.flexpoker.framework.event;

import java.time.Instant;
import java.util.UUID;

public interface Event<T extends EventType> {

    UUID getAggregateId();

    T getType();

    int getVersion();

    Instant getTime();

}
