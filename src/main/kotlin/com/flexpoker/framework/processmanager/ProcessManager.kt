package com.flexpoker.framework.processmanager

import com.flexpoker.framework.event.Event

fun interface ProcessManager<T : Event> {
    fun handle(event: T)
}