package com.flexpoker.table.command.publish;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.Command;
import com.flexpoker.framework.command.CommandHandler;
import com.flexpoker.framework.command.CommandPublisher;
import com.flexpoker.table.command.commands.CreateTableCommand;
import com.flexpoker.table.command.framework.TableCommandType;

@Component
public class InMemoryAsyncTableCommandPublisher implements
        CommandPublisher<TableCommandType> {

    private final CommandHandler<CreateTableCommand> createTableCommandHandler;

    @Inject
    public InMemoryAsyncTableCommandPublisher(
            CommandHandler<CreateTableCommand> createTableCommandHandler) {
        this.createTableCommandHandler = createTableCommandHandler;
    }

    @Override
    public void publish(Command<TableCommandType> command) {
        switch (command.getType()) {
        case CreateTable:
            createTableCommandHandler.handle((CreateTableCommand) command);
            break;
        default:
            throw new IllegalArgumentException("Command Type cannot be handled: "
                    + command.getType());
        }
    }

}
