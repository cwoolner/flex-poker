package com.flexpoker.table.query.handlers;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.table.command.events.PlayerFoldedEvent;

@Component
public class PlayerFoldedEventHandler implements EventHandler<PlayerFoldedEvent> {

    @Async
    @Override
    public void handle(PlayerFoldedEvent event) {
        // TODO Auto-generated method stub

    }

}
