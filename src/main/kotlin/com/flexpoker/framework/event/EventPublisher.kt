package com.flexpoker.framework.event

fun interface EventPublisher<T : Event> {
    fun publish(event: T)
}