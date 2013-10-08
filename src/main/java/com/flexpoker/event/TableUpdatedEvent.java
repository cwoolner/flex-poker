package com.flexpoker.event;

import java.util.UUID;

import org.springframework.context.ApplicationEvent;

import com.flexpoker.model.Table;

public class TableUpdatedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 4843078116921661629L;

    private final UUID gameId;
    
    private final Table table;
    
    public TableUpdatedEvent(Object source, UUID gameId, Table table) {
        super(source);
        this.gameId = gameId;
        this.table = table;
    }

    public UUID getGameId() {
        return gameId;
    }

    public Table getTable() {
        return table;
    }
}
