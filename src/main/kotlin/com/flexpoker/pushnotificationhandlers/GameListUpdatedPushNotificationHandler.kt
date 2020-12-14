package com.flexpoker.pushnotificationhandlers

import com.flexpoker.framework.pushnotifier.PushNotificationHandler
import com.flexpoker.game.query.repository.GameListRepository
import com.flexpoker.pushnotifications.GameListUpdatedPushNotification
import com.flexpoker.util.MessagingConstants
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class GameListUpdatedPushNotificationHandler @Inject constructor(
    private val gameListRepository: GameListRepository,
    private val messagingTemplate: SimpMessageSendingOperations
) : PushNotificationHandler<GameListUpdatedPushNotification> {

    @Async
    override fun handle(pushNotification: GameListUpdatedPushNotification) {
        val allGames = gameListRepository.fetchAll()
        messagingTemplate.convertAndSend(MessagingConstants.GAMES_UPDATED, allGames)
    }

}