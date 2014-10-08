package com.flexpoker.framework.command;

import java.time.Instant;
import java.util.UUID;

public abstract class BaseCommand<T extends CommandType> implements Command<T> {

    private final T type;

    private final UUID id;

    private final Instant time;

    public BaseCommand(T type) {
        this.type = type;
        this.id = UUID.randomUUID();
        this.time = Instant.now();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public T getType() {
        return type;
    }

    @Override
    public Instant getTime() {
        return time;
    }

}
