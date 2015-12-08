package com.flexpoker.framework.command;

public interface CommandReceiver<T extends CommandType> {

    void receive(Command<T> event);

}
