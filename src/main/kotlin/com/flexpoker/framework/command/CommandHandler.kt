package com.flexpoker.framework.command

fun interface CommandHandler<T> {
    fun handle(command: T)
}