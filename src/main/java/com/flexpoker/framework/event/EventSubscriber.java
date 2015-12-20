package com.flexpoker.framework.event;

public interface EventSubscriber<T extends Event> {

    void receive(T event);

}
