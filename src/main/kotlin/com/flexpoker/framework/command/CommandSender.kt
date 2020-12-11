package com.flexpoker.framework.command

interface CommandSender<T> {
    fun send(command: T)
}