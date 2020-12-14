package com.flexpoker.processmanagers

import com.flexpoker.framework.command.CommandSender
import com.flexpoker.framework.processmanager.ProcessManager
import com.flexpoker.game.command.events.PlayerMovedToNewTableEvent
import com.flexpoker.table.command.commands.AddPlayerCommand
import com.flexpoker.table.command.commands.RemovePlayerCommand
import com.flexpoker.table.command.commands.TableCommand
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class MovePlayerBetweenTablesProcessManager @Inject constructor(
    private val tableCommandSender: CommandSender<TableCommand>
) : ProcessManager<PlayerMovedToNewTableEvent> {

    @Async
    override fun handle(event: PlayerMovedToNewTableEvent) {
        val addPlayerTableCommand = AddPlayerCommand(event.toTableId, event.aggregateId, event.playerId, event.chips)
        tableCommandSender.send(addPlayerTableCommand)
        val removePlayerTableCommand = RemovePlayerCommand(event.fromTableId, event.aggregateId, event.playerId)
        tableCommandSender.send(removePlayerTableCommand)
    }

}