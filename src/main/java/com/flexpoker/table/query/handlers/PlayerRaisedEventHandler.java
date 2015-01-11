package com.flexpoker.table.query.handlers;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.table.command.events.PlayerRaisedEvent;

@Component
public class PlayerRaisedEventHandler implements EventHandler<PlayerRaisedEvent> {

    @Override
    public void handle(PlayerRaisedEvent event) {
        // TODO Auto-generated method stub

    }

}
