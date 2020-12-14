package com.flexpoker.framework.event.subscriber

import com.flexpoker.framework.event.Event

fun interface EventSubscriber<T : Event> {
    fun receive(event: T)
}