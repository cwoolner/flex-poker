package com.flexpoker.table.command.framework;

import java.util.UUID;

import com.flexpoker.framework.event.Event;

public interface TableEvent extends Event<TableEventType> {

    UUID getGameId();

}
