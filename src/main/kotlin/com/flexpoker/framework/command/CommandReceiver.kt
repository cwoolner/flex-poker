package com.flexpoker.framework.command

fun interface CommandReceiver<T> {
    fun receive(command: T)
}