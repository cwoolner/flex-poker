package com.flexpoker.framework.command;


public interface CommandSender<T> {

    void send(T command);

}
