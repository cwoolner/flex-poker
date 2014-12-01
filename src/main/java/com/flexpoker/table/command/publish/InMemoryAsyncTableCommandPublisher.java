package com.flexpoker.table.command.publish;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.command.Command;
import com.flexpoker.framework.command.CommandHandler;
import com.flexpoker.framework.command.CommandPublisher;
import com.flexpoker.table.command.commands.CallCommand;
import com.flexpoker.table.command.commands.CheckCommand;
import com.flexpoker.table.command.commands.CreateTableCommand;
import com.flexpoker.table.command.commands.FoldCommand;
import com.flexpoker.table.command.commands.RaiseCommand;
import com.flexpoker.table.command.commands.StartNewHandForNewGameCommand;
import com.flexpoker.table.command.framework.TableCommandType;

@Component
public class InMemoryAsyncTableCommandPublisher implements
        CommandPublisher<TableCommandType> {

    private final CommandHandler<CreateTableCommand> createTableCommandHandler;

    private final CommandHandler<StartNewHandForNewGameCommand> startNewHandForNewGameCommandHandler;

    private final CommandHandler<CheckCommand> checkCommandHandler;

    private final CommandHandler<CallCommand> callCommandHandler;

    private final CommandHandler<FoldCommand> foldCommandHandler;

    private final CommandHandler<RaiseCommand> raiseCommandHandler;

    @Inject
    public InMemoryAsyncTableCommandPublisher(
            CommandHandler<CreateTableCommand> createTableCommandHandler,
            CommandHandler<StartNewHandForNewGameCommand> startNewHandForNewGameCommandHandler,
            CommandHandler<CheckCommand> checkCommandHandler,
            CommandHandler<CallCommand> callCommandHandler,
            CommandHandler<FoldCommand> foldCommandHandler,
            CommandHandler<RaiseCommand> raiseCommandHandler) {
        this.createTableCommandHandler = createTableCommandHandler;
        this.startNewHandForNewGameCommandHandler = startNewHandForNewGameCommandHandler;
        this.checkCommandHandler = checkCommandHandler;
        this.callCommandHandler = callCommandHandler;
        this.foldCommandHandler = foldCommandHandler;
        this.raiseCommandHandler = raiseCommandHandler;
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
        case Call:
            callCommandHandler.handle((CallCommand) command);
            break;
        case Check:
            checkCommandHandler.handle((CheckCommand) command);
            break;
        case Fold:
            foldCommandHandler.handle((FoldCommand) command);
            break;
        case Raise:
            raiseCommandHandler.handle((RaiseCommand) command);
            break;
        default:
            throw new IllegalArgumentException("Command Type cannot be handled: "
                    + command.getType());
        }
    }

}
