package com.flexpoker.table.command.commandreceivers;

import com.flexpoker.framework.command.CommandHandler;
import com.flexpoker.framework.command.CommandReceiver;
import com.flexpoker.table.command.commands.AddPlayerCommand;
import com.flexpoker.table.command.commands.AutoMoveHandForwardCommand;
import com.flexpoker.table.command.commands.CallCommand;
import com.flexpoker.table.command.commands.CheckCommand;
import com.flexpoker.table.command.commands.CreateTableCommand;
import com.flexpoker.table.command.commands.ExpireActionOnTimerCommand;
import com.flexpoker.table.command.commands.FoldCommand;
import com.flexpoker.table.command.commands.PauseCommand;
import com.flexpoker.table.command.commands.RaiseCommand;
import com.flexpoker.table.command.commands.RemovePlayerCommand;
import com.flexpoker.table.command.commands.ResumeCommand;
import com.flexpoker.table.command.commands.StartNewHandForExistingTableCommand;
import com.flexpoker.table.command.commands.StartNewHandForNewGameCommand;
import com.flexpoker.table.command.commands.TableCommand;
import com.flexpoker.table.command.commands.TickActionOnTimerCommand;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component("tableCommandReceiver")
public class InMemoryAsyncTableCommandReceiver implements CommandReceiver<TableCommand> {

    private final CommandHandler<CreateTableCommand> createTableCommandHandler;

    private final CommandHandler<StartNewHandForNewGameCommand> startNewHandForNewGameCommandHandler;

    private final CommandHandler<StartNewHandForExistingTableCommand> startNewHandForExistingTableCommandHandler;

    private final CommandHandler<CheckCommand> checkCommandHandler;

    private final CommandHandler<CallCommand> callCommandHandler;

    private final CommandHandler<FoldCommand> foldCommandHandler;

    private final CommandHandler<RaiseCommand> raiseCommandHandler;

    private final CommandHandler<ExpireActionOnTimerCommand> expireActionOnTimerCommandHandler;

    private final CommandHandler<TickActionOnTimerCommand> tickActionOnTimerCommandHandler;

    private final CommandHandler<PauseCommand> pauseCommandHandler;

    private final CommandHandler<ResumeCommand> resumeCommandHandler;

    private final CommandHandler<AddPlayerCommand> addPlayerCommandHandler;

    private final CommandHandler<RemovePlayerCommand> removePlayerCommandHandler;

    private final CommandHandler<AutoMoveHandForwardCommand> autoMoveHandForwardCommandHandler;

    @Inject
    public InMemoryAsyncTableCommandReceiver(
            CommandHandler<CreateTableCommand> createTableCommandHandler,
            CommandHandler<StartNewHandForNewGameCommand> startNewHandForNewGameCommandHandler,
            CommandHandler<StartNewHandForExistingTableCommand> startNewHandForExistingTableCommandHandler,
            CommandHandler<CheckCommand> checkCommandHandler,
            CommandHandler<CallCommand> callCommandHandler,
            CommandHandler<FoldCommand> foldCommandHandler,
            CommandHandler<RaiseCommand> raiseCommandHandler,
            CommandHandler<ExpireActionOnTimerCommand> expireActionOnTimerCommandHandler,
            CommandHandler<TickActionOnTimerCommand> tickActionOnTimerCommandHandler,
            CommandHandler<PauseCommand> pauseCommandHandler,
            CommandHandler<ResumeCommand> resumeCommandHandler,
            CommandHandler<AddPlayerCommand> addPlayerCommandHandler,
            CommandHandler<RemovePlayerCommand> removePlayerCommandHandler,
            CommandHandler<AutoMoveHandForwardCommand> autoMoveHandForwardCommandHandler) {
        this.createTableCommandHandler = createTableCommandHandler;
        this.startNewHandForNewGameCommandHandler = startNewHandForNewGameCommandHandler;
        this.startNewHandForExistingTableCommandHandler = startNewHandForExistingTableCommandHandler;
        this.checkCommandHandler = checkCommandHandler;
        this.callCommandHandler = callCommandHandler;
        this.foldCommandHandler = foldCommandHandler;
        this.raiseCommandHandler = raiseCommandHandler;
        this.expireActionOnTimerCommandHandler = expireActionOnTimerCommandHandler;
        this.tickActionOnTimerCommandHandler = tickActionOnTimerCommandHandler;
        this.pauseCommandHandler = pauseCommandHandler;
        this.resumeCommandHandler = resumeCommandHandler;
        this.addPlayerCommandHandler = addPlayerCommandHandler;
        this.removePlayerCommandHandler = removePlayerCommandHandler;
        this.autoMoveHandForwardCommandHandler = autoMoveHandForwardCommandHandler;
    }

    @Async
    @Override
    public void receive(TableCommand command) {
        switch (command.getClass().getSimpleName()) {
        case "CreateTableCommand":
            createTableCommandHandler.handle((CreateTableCommand) command);
            break;
        case "StartNewHandForNewGameCommand":
            startNewHandForNewGameCommandHandler
                    .handle((StartNewHandForNewGameCommand) command);
            break;
        case "StartNewHandForExistingTableCommand":
            startNewHandForExistingTableCommandHandler
                    .handle((StartNewHandForExistingTableCommand) command);
            break;
        case "CallCommand":
            callCommandHandler.handle((CallCommand) command);
            break;
        case "CheckCommand":
            checkCommandHandler.handle((CheckCommand) command);
            break;
        case "FoldCommand":
            foldCommandHandler.handle((FoldCommand) command);
            break;
        case "RaiseCommand":
            raiseCommandHandler.handle((RaiseCommand) command);
            break;
        case "ExpireActionOnTimerCommand":
            expireActionOnTimerCommandHandler
                    .handle((ExpireActionOnTimerCommand) command);
            break;
        case "TickActionOnTimerCommand":
            tickActionOnTimerCommandHandler
                    .handle((TickActionOnTimerCommand) command);
            break;
        case "AddPlayerCommand":
            addPlayerCommandHandler.handle((AddPlayerCommand) command);
            break;
        case "PauseCommand":
            pauseCommandHandler.handle((PauseCommand) command);
            break;
        case "RemovePlayerCommand":
            removePlayerCommandHandler.handle((RemovePlayerCommand) command);
            break;
        case "ResumeCommand":
            resumeCommandHandler.handle((ResumeCommand) command);
            break;
        case "AutoMoveHandForwardCommand":
            autoMoveHandForwardCommandHandler.handle((AutoMoveHandForwardCommand) command);
            break;
        default:
            throw new IllegalArgumentException(
                    "Command Type cannot be handled: " + command.getClass().getSimpleName());
        }
    }

}
