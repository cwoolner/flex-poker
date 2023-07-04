package com.flexpoker.framework.event

import java.time.Instant
import java.util.UUID

interface Event {
    val aggregateId: UUID
    var version: Int
    val time: Instant
}

fun interface EventHandler<T : Event> {
    fun handle(event: T)
}

fun interface EventPublisher<T : Event> {
    fun publish(event: T)
}