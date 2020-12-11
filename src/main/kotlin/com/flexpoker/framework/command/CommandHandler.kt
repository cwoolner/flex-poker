package com.flexpoker.framework.command

interface CommandHandler<T> {
    fun handle(command: T)
}