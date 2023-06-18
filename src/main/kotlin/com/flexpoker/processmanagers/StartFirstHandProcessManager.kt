package com.flexpoker.processmanagers

import com.flexpoker.framework.command.CommandSender
import com.flexpoker.framework.processmanager.ProcessManager
import com.flexpoker.game.command.events.GameStartedEvent
import com.flexpoker.table.command.commands.StartNewHandForNewGameCommand
import com.flexpoker.table.command.commands.TableCommand
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class StartFirstHandProcessManager @Inject constructor(
    private val tableCommandSender: CommandSender<TableCommand>
) : ProcessManager<GameStartedEvent> {

    @Async
    override fun handle(event: GameStartedEvent) {
        val blinds = event.blindSchedule.currentBlinds()
        event.tableIds.forEach {
            val startNewHandForNewGameCommand = StartNewHandForNewGameCommand(
                it, event.aggregateId, blinds.smallBlind, blinds.bigBlind)
            tableCommandSender.send(startNewHandForNewGameCommand)
        }
    }

}