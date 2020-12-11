package com.flexpoker.web.commandsenders

import com.flexpoker.framework.command.CommandReceiver
import com.flexpoker.framework.command.CommandSender
import com.flexpoker.game.command.commands.GameCommand
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class InMemoryAsyncGameCommandSender @Lazy @Inject constructor(
    private val gameCommandReceiver: CommandReceiver<GameCommand>
) : CommandSender<GameCommand> {

    override fun send(command: GameCommand) {
        gameCommandReceiver.receive(command)
    }

}