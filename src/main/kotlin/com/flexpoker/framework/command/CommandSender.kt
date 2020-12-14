package com.flexpoker.framework.command

fun interface CommandSender<T> {
    fun send(command: T)
}