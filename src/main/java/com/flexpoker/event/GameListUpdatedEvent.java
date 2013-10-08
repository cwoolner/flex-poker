package com.flexpoker.event;

import org.springframework.context.ApplicationEvent;

public class GameListUpdatedEvent extends ApplicationEvent {

    private static final long serialVersionUID = -5959935937213806565L;

    public GameListUpdatedEvent(Object source) {
        super(source);
    }

}
