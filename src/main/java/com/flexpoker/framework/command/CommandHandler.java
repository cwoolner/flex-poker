package com.flexpoker.framework.command;


public interface CommandHandler<T> {

    void handle(T command);

}
