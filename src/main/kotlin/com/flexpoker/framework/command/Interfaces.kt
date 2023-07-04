package com.flexpoker.framework.command

fun interface CommandHandler<T> {
    fun handle(command: T)
}

fun interface CommandReceiver<T> {
    fun receive(command: T)
}

fun interface CommandSender<T> {
    fun send(command: T)
}
