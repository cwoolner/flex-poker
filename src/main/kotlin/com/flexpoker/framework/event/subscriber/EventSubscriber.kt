package com.flexpoker.framework.event.subscriber

import com.flexpoker.framework.event.Event

interface EventSubscriber<T : Event> {
    fun receive(event: T)
}