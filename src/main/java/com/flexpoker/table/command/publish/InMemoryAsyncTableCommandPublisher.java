package com.flexpoker.table.command.publish;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.Command;
import com.flexpoker.framework.command.CommandHandler;
import com.flexpoker.framework.command.CommandPublisher;
import com.flexpoker.table.command.commands.CreateTableCommand;
import com.flexpoker.table.command.commands.StartNewHandForNewGameCommand;
import com.flexpoker.table.command.framework.TableCommandType;

@Component
public class InMemoryAsyncTableCommandPublisher implements
        CommandPublisher<TableCommandType> {

    private final CommandHandler<CreateTableCommand> createTableCommandHandler;

    private final CommandHandler<StartNewHandForNewGameCommand> startNewHandForNewGameCommandHandler;

    @Inject
    public InMemoryAsyncTableCommandPublisher(
            CommandHandler<CreateTableCommand> createTableCommandHandler,
            CommandHandler<StartNewHandForNewGameCommand> startNewHandForNewGameCommandHandler) {
        this.createTableCommandHandler = createTableCommandHandler;
        this.startNewHandForNewGameCommandHandler = startNewHandForNewGameCommandHandler;
    }

    @Override
    public void publish(Command<TableCommandType> command) {
        switch (command.getType()) {
        case CreateTable:
            createTableCommandHandler.handle((CreateTableCommand) command);
            break;
        case StartNewHandForNewGame:
            startNewHandForNewGameCommandHandler
                    .handle((StartNewHandForNewGameCommand) command);
            break;
        default:
            throw new IllegalArgumentException("Command Type cannot be handled: "
                    + command.getType());
        }
    }

}
