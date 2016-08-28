package com.flexpoker.framework.event.subscriber;

import com.flexpoker.framework.event.Event;

public interface EventSubscriber<T extends Event> {

    void receive(T event);

}
