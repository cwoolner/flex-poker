package com.flexpoker.framework.command;

import com.flexpoker.framework.event.Event;

@FunctionalInterface
public interface EventApplier<T extends Event> {

    void applyEvent(T event);

}
