package com.flexpoker.table.query.handlers;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.table.command.events.PlayerCalledEvent;

@Component
public class PlayerCalledEventHandler implements EventHandler<PlayerCalledEvent> {

    @Async
    @Override
    public void handle(PlayerCalledEvent event) {
        // TODO Auto-generated method stub

    }

}
