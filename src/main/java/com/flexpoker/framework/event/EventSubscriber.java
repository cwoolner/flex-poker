package com.flexpoker.framework.event;

public interface EventSubscriber<T extends EventType> {

    void receive(Event<T> event);

}
