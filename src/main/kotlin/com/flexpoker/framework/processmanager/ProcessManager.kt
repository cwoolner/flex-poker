package com.flexpoker.framework.processmanager

import com.flexpoker.framework.event.Event

interface ProcessManager<T : Event> {
    fun handle(event: T)
}