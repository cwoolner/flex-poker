package com.flexpoker.framework.event

interface EventPublisher<T : Event> {
    fun publish(event: T)
}