package com.flexpoker.pushnotificationhandlers

import com.flexpoker.framework.pushnotifier.PushNotificationHandler
import com.flexpoker.pushnotifications.TickActionOnTimerPushNotification
import com.flexpoker.util.MessagingConstants
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class TickActionOnTimerPushNotificationHandler @Inject constructor(private val messagingTemplate: SimpMessageSendingOperations) :
    PushNotificationHandler<TickActionOnTimerPushNotification> {

    @Async
    override fun handle(pushNotification: TickActionOnTimerPushNotification) {
        messagingTemplate.convertAndSend(
            String.format(MessagingConstants.TICK_ACTION_ON_TIMER, pushNotification.gameId, pushNotification.tableId),
            pushNotification.number)
    }

}