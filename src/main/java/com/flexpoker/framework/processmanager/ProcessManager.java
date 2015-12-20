package com.flexpoker.framework.processmanager;

import com.flexpoker.framework.event.Event;

public interface ProcessManager<T extends Event> {

    void handle(T event);

}
