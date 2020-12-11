package com.flexpoker.framework.command

interface CommandReceiver<T> {
    fun receive(command: T)
}