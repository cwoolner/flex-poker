package com.flexpoker.framework.command;

import java.time.Instant;
import java.util.UUID;

public interface Command<T extends CommandType> {

    UUID getId();

    T getType();

    Instant getTime();

}
