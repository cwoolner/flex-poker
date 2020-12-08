package com.flexpoker.framework.command;

public interface CommandReceiver<T> {

    void receive(T command);

}
