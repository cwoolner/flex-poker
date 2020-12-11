package com.flexpoker.table.command.handlers

import com.flexpoker.framework.command.CommandHandler
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher
import com.flexpoker.pushnotifications.TickActionOnTimerPushNotification
import com.flexpoker.table.command.commands.TickActionOnTimerCommand
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class TickActionOnTimerCommandHandler @Inject constructor(
    private val pushNotificationPublisher: PushNotificationPublisher
) : CommandHandler<TickActionOnTimerCommand> {

    @Async
    override fun handle(command: TickActionOnTimerCommand) {
        val pushNotification = TickActionOnTimerPushNotification(command.gameId, command.tableId, command.number)
        pushNotificationPublisher.publish(pushNotification)
    }

}