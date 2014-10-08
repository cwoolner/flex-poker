package com.flexpoker.framework.command;


public interface CommandPublisher<T extends CommandType> {

    void publish(Command<T> command);

}
