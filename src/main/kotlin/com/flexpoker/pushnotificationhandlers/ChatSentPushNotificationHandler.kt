package com.flexpoker.pushnotificationhandlers

import com.flexpoker.framework.pushnotifier.PushNotificationHandler
import com.flexpoker.pushnotifications.ChatSentPushNotification
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class ChatSentPushNotificationHandler @Inject constructor(private val messagingTemplate: SimpMessageSendingOperations) :
    PushNotificationHandler<ChatSentPushNotification> {

    @Async
    override fun handle(pushNotification: ChatSentPushNotification) {
        messagingTemplate.convertAndSend(pushNotification.destination, pushNotification)
    }

}