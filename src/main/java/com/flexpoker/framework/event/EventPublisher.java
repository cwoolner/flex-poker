package com.flexpoker.framework.event;


public interface EventPublisher<T extends EventType> {

    void publish(Event<T> event);

}
