package com.flexpoker.framework.event

fun interface EventHandler<T : Event> {
    fun handle(event: T)
}