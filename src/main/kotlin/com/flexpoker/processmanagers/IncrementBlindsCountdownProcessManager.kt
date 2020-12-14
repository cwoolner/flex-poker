package com.flexpoker.processmanagers

import com.flexpoker.framework.command.CommandSender
import com.flexpoker.framework.processmanager.ProcessManager
import com.flexpoker.game.command.commands.GameCommand
import com.flexpoker.game.command.commands.IncrementBlindsCommand
import com.flexpoker.game.command.events.GameStartedEvent
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@Component
class IncrementBlindsCountdownProcessManager @Inject constructor(
    private val gameCommandSender: CommandSender<GameCommand>
) : ProcessManager<GameStartedEvent> {

    @Async
    override fun handle(event: GameStartedEvent) {
        val actionOnTimer = Timer()
        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                val command = IncrementBlindsCommand(event.aggregateId)
                gameCommandSender.send(command)
            }
        }
        val blindIncrementInMilliseconds = event.blindScheduleDTO.numberOfMinutesBetweenLevels * 60000
        actionOnTimer.scheduleAtFixedRate(
            timerTask,
            blindIncrementInMilliseconds.toLong(),
            blindIncrementInMilliseconds.toLong()
        )
    }

}