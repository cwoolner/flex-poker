package com.flexpoker.framework.command;


public interface CommandHandler<T extends Command<? extends CommandType>> {

    void handle(T command);

}
