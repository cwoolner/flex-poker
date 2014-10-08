package com.flexpoker.framework.processmanager;

import com.flexpoker.framework.event.Event;
import com.flexpoker.framework.event.EventType;

public interface ProcessManager<T extends Event<? extends EventType>> {

    void handle(T event);

}
