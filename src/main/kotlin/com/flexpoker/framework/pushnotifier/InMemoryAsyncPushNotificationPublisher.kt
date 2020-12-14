package com.flexpoker.framework.pushnotifier

import com.flexpoker.pushnotifications.ChatSentPushNotification
import com.flexpoker.pushnotifications.GameListUpdatedPushNotification
import com.flexpoker.pushnotifications.OpenGamesForPlayerUpdatedPushNotification
import com.flexpoker.pushnotifications.OpenTableForUserPushNotification
import com.flexpoker.pushnotifications.PushNotification
import com.flexpoker.pushnotifications.SendUserPocketCardsPushNotification
import com.flexpoker.pushnotifications.TableUpdatedPushNotification
import com.flexpoker.pushnotifications.TickActionOnTimerPushNotification
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class InMemoryAsyncPushNotificationPublisher @Inject constructor(
    private val chatPushNotificationHandler: PushNotificationHandler<ChatSentPushNotification>,
    private val gameListUpdatedPushNotificationHandler: PushNotificationHandler<GameListUpdatedPushNotification>,
    private val openGamesForPlayerUpdatedPushNotificationHandler: PushNotificationHandler<OpenGamesForPlayerUpdatedPushNotification>,
    private val openTableForUserPushNotificationHandler: PushNotificationHandler<OpenTableForUserPushNotification>,
    private val sendUserPocketCardsPushNotificationHandler: PushNotificationHandler<SendUserPocketCardsPushNotification>,
    private val tableUpdatedPushNotificationHandler: PushNotificationHandler<TableUpdatedPushNotification>,
    private val tickActionOnTimerPushNotificationHandler: PushNotificationHandler<TickActionOnTimerPushNotification>
) : PushNotificationPublisher {

    @Async
    override fun publish(pushNotification: PushNotification) {
        when (pushNotification) {
            is ChatSentPushNotification -> chatPushNotificationHandler.handle(pushNotification)
            is GameListUpdatedPushNotification -> gameListUpdatedPushNotificationHandler.handle(pushNotification)
            is OpenGamesForPlayerUpdatedPushNotification -> openGamesForPlayerUpdatedPushNotificationHandler.handle(pushNotification)
            is OpenTableForUserPushNotification -> openTableForUserPushNotificationHandler.handle(pushNotification)
            is SendUserPocketCardsPushNotification -> sendUserPocketCardsPushNotificationHandler.handle(pushNotification)
            is TableUpdatedPushNotification -> tableUpdatedPushNotificationHandler.handle(pushNotification)
            is TickActionOnTimerPushNotification -> tickActionOnTimerPushNotificationHandler.handle(pushNotification)
        }
    }

}