package com.flexpoker.table.query.handlers;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.table.command.events.PlayerRaisedEvent;

@Component
public class PlayerRaisedEventHandler implements EventHandler<PlayerRaisedEvent> {

    @Async
    @Override
    public void handle(PlayerRaisedEvent event) {
        // TODO Auto-generated method stub

    }

}
