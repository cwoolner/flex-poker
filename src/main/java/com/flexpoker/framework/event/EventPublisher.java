package com.flexpoker.framework.event;


public interface EventPublisher<T extends Event> {

    void publish(T event);

}
