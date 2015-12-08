package com.flexpoker.framework.command;


public interface CommandSender<T extends CommandType> {

    void send(Command<T> command);

}
