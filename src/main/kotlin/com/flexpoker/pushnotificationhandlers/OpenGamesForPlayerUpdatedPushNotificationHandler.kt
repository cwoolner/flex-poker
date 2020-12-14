package com.flexpoker.pushnotificationhandlers

import com.flexpoker.framework.pushnotifier.PushNotificationHandler
import com.flexpoker.game.query.repository.OpenGameForPlayerRepository
import com.flexpoker.login.repository.LoginRepository
import com.flexpoker.pushnotifications.OpenGamesForPlayerUpdatedPushNotification
import com.flexpoker.util.MessagingConstants
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
class OpenGamesForPlayerUpdatedPushNotificationHandler @Inject constructor(
    private val loginRepository: LoginRepository,
    private val openGameForUserRepository: OpenGameForPlayerRepository,
    private val messagingTemplate: SimpMessageSendingOperations
) : PushNotificationHandler<OpenGamesForPlayerUpdatedPushNotification> {

    @Async
    override fun handle(pushNotification: OpenGamesForPlayerUpdatedPushNotification) {
        val username = loginRepository.fetchUsernameByAggregateId(pushNotification.playerId)
        val allOpenGames = openGameForUserRepository.fetchAllOpenGamesForPlayer(pushNotification.playerId)
        messagingTemplate.convertAndSendToUser(username, MessagingConstants.OPEN_GAMES_FOR_USER, allOpenGames)
    }

}