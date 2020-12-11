package com.flexpoker.game.command.commandreceivers

import com.flexpoker.framework.command.CommandHandler
import com.flexpoker.framework.command.CommandReceiver
import com.flexpoker.game.command.commands.AttemptToStartNewHandCommand
import com.flexpoker.game.command.commands.CreateGameCommand
import com.flexpoker.game.command.commands.GameCommand
import com.flexpoker.game.command.commands.IncrementBlindsCommand
import com.flexpoker.game.command.commands.JoinGameCommand
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component("gameCommandReceiver")
class InMemoryAsyncGameCommandReceiver @Inject constructor(
    private val createGameCommandHandler: CommandHandler<CreateGameCommand>,
    private val joinGameCommandHandler: CommandHandler<JoinGameCommand>,
    private val attemptToStartNewHandCommandHandler: CommandHandler<AttemptToStartNewHandCommand>,
    private val incrementBlindsCommandHandler: CommandHandler<IncrementBlindsCommand>
) : CommandReceiver<GameCommand> {

    @Async
    override fun receive(command: GameCommand) {
        when (command) {
            is CreateGameCommand -> createGameCommandHandler.handle(command)
            is JoinGameCommand -> joinGameCommandHandler.handle(command)
            is AttemptToStartNewHandCommand -> attemptToStartNewHandCommandHandler
                .handle(command)
            is IncrementBlindsCommand -> incrementBlindsCommandHandler.handle(command)
        }
    }

}