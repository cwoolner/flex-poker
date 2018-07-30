package com.flexpoker.framework.event;

import java.time.Instant;
import java.util.UUID;

public interface Event {

    UUID getAggregateId();

    int getVersion();

    void setVersion(int version);

    Instant getTime();

}
