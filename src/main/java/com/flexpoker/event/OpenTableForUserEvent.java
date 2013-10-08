package com.flexpoker.event;

import java.util.UUID;

import org.springframework.context.ApplicationEvent;

public class OpenTableForUserEvent extends ApplicationEvent {

    private static final long serialVersionUID = 6645678581321500557L;

    private final String username;

    private final UUID gameId;

    private final UUID tableId;

    public OpenTableForUserEvent(Object source, String username, UUID gameId,
            UUID tableId) {
        super(source);
        this.username = username;
        this.gameId = gameId;
        this.tableId = tableId;
    }

    public String getUsername() {
        return username;
    }

    public UUID getGameId() {
        return gameId;
    }

    public UUID getTableId() {
        return tableId;
    }
}
