package com.flexpoker.framework.command

import com.flexpoker.framework.event.Event

fun interface EventApplier<T : Event?> {
    fun applyEvent(event: T)
}