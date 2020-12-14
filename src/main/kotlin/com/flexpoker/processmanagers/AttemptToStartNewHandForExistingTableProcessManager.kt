package com.flexpoker.processmanagers

import com.flexpoker.framework.command.CommandSender
import com.flexpoker.framework.processmanager.ProcessManager
import com.flexpoker.game.command.commands.AttemptToStartNewHandCommand
import com.flexpoker.game.command.commands.GameCommand
import com.flexpoker.table.command.events.HandCompletedEvent
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class AttemptToStartNewHandForExistingTableProcessManager @Inject constructor(
    private val gameCommandSender: CommandSender<GameCommand>
) : ProcessManager<HandCompletedEvent> {

    @Async
    override fun handle(event: HandCompletedEvent) {
        val command = AttemptToStartNewHandCommand(event.gameId, event.aggregateId, event.playerToChipsAtTableMap)
        gameCommandSender.send(command)
    }

}