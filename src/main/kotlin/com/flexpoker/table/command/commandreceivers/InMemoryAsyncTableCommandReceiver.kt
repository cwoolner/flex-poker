package com.flexpoker.table.command.commandreceivers

import com.flexpoker.framework.command.CommandHandler
import com.flexpoker.framework.command.CommandReceiver
import com.flexpoker.table.command.commands.AddPlayerCommand
import com.flexpoker.table.command.commands.AutoMoveHandForwardCommand
import com.flexpoker.table.command.commands.CallCommand
import com.flexpoker.table.command.commands.CheckCommand
import com.flexpoker.table.command.commands.CreateTableCommand
import com.flexpoker.table.command.commands.ExpireActionOnTimerCommand
import com.flexpoker.table.command.commands.FoldCommand
import com.flexpoker.table.command.commands.PauseCommand
import com.flexpoker.table.command.commands.RaiseCommand
import com.flexpoker.table.command.commands.RemovePlayerCommand
import com.flexpoker.table.command.commands.ResumeCommand
import com.flexpoker.table.command.commands.StartNewHandForExistingTableCommand
import com.flexpoker.table.command.commands.StartNewHandForNewGameCommand
import com.flexpoker.table.command.commands.TableCommand
import com.flexpoker.table.command.commands.TickActionOnTimerCommand
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component("tableCommandReceiver")
class InMemoryAsyncTableCommandReceiver @Inject constructor(
    private val createTableCommandHandler: CommandHandler<CreateTableCommand>,
    private val startNewHandForNewGameCommandHandler: CommandHandler<StartNewHandForNewGameCommand>,
    private val startNewHandForExistingTableCommandHandler: CommandHandler<StartNewHandForExistingTableCommand>,
    private val checkCommandHandler: CommandHandler<CheckCommand>,
    private val callCommandHandler: CommandHandler<CallCommand>,
    private val foldCommandHandler: CommandHandler<FoldCommand>,
    private val raiseCommandHandler: CommandHandler<RaiseCommand>,
    private val expireActionOnTimerCommandHandler: CommandHandler<ExpireActionOnTimerCommand>,
    private val tickActionOnTimerCommandHandler: CommandHandler<TickActionOnTimerCommand>,
    private val pauseCommandHandler: CommandHandler<PauseCommand>,
    private val resumeCommandHandler: CommandHandler<ResumeCommand>,
    private val addPlayerCommandHandler: CommandHandler<AddPlayerCommand>,
    private val removePlayerCommandHandler: CommandHandler<RemovePlayerCommand>,
    private val autoMoveHandForwardCommandHandler: CommandHandler<AutoMoveHandForwardCommand>
) : CommandReceiver<TableCommand> {

    @Async
    override fun receive(command: TableCommand) {
        when (command) {
            is CreateTableCommand -> createTableCommandHandler.handle(command)
            is StartNewHandForNewGameCommand -> startNewHandForNewGameCommandHandler.handle(command)
            is StartNewHandForExistingTableCommand -> startNewHandForExistingTableCommandHandler.handle(command)
            is CallCommand -> callCommandHandler.handle(command)
            is CheckCommand -> checkCommandHandler.handle(command)
            is FoldCommand -> foldCommandHandler.handle(command)
            is RaiseCommand -> raiseCommandHandler.handle(command)
            is ExpireActionOnTimerCommand -> expireActionOnTimerCommandHandler.handle(command)
            is TickActionOnTimerCommand -> tickActionOnTimerCommandHandler.handle(command)
            is AddPlayerCommand -> addPlayerCommandHandler.handle(command)
            is PauseCommand -> pauseCommandHandler.handle(command)
            is RemovePlayerCommand -> removePlayerCommandHandler.handle(command)
            is ResumeCommand -> resumeCommandHandler.handle(command)
            is AutoMoveHandForwardCommand -> autoMoveHandForwardCommandHandler.handle(command)
        }
    }

}