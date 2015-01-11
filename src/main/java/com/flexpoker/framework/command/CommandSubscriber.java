package com.flexpoker.framework.command;

public interface CommandSubscriber<T extends CommandType> {

    void receive(Command<T> event);

}
